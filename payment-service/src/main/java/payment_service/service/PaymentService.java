package payment_service.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import payment_service.dto.BankCallbackRequest;
import payment_service.dto.BankPaymentResult;
import payment_service.dto.CreatePaymentRequest;
import payment_service.dto.OrderResponse;
import payment_service.dto.PaymentResponse;
import payment_service.entity.Payment;
import payment_service.enums.PaymentStatus;
import payment_service.exception.BusinessException;
import payment_service.exception.ResourceNotFoundException;
import payment_service.mapper.PaymentMapper;
import payment_service.processor.PaymentProcessor;
import payment_service.repository.PaymentRepository;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderClient orderClient;
    private final PaymentProcessor paymentProcessor;

    public PaymentService(PaymentRepository paymentRepository, OrderClient orderClient,
                          PaymentProcessor paymentProcessor) {
        this.paymentRepository = paymentRepository;
        this.orderClient = orderClient;
        this.paymentProcessor = paymentProcessor;
    }

    @Transactional
    public PaymentResponse createPayment(CreatePaymentRequest request) {
        OrderResponse order = orderClient.getOrder(request.getOrderId());
        validateOrder(order);

        boolean hasPendingPayment = paymentRepository.existsByOrderIdAndStatus(
                request.getOrderId(), PaymentStatus.CHO_THANH_TOAN);
        if (hasPendingPayment) {
            throw new BusinessException("Order already has a pending payment");
        }

        Payment payment = new Payment();
        payment.setOrderId(order.getId());
        payment.setAmount(order.getTotalAmount());
        payment.setMethod(request.getMethod());
        payment.setBankCode(request.getBankCode());
        payment.setStatus(PaymentStatus.CHO_THANH_TOAN);
        payment.setCreatedAt(LocalDateTime.now());
        payment.setUpdatedAt(LocalDateTime.now());

        BankPaymentResult bankResult = paymentProcessor.createPayment(payment);
        payment.setTransactionCode(bankResult.getTransactionCode());
        payment.setPaymentUrl(bankResult.getPaymentUrl());

        return PaymentMapper.toResponse(paymentRepository.save(payment));
    }

    @Transactional(readOnly = true)
    public List<PaymentResponse> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(PaymentMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PaymentResponse getPaymentById(Long id) {
        return PaymentMapper.toResponse(findPayment(id));
    }

    @Transactional(readOnly = true)
    public List<PaymentResponse> getPaymentsByOrderId(Long orderId) {
        return paymentRepository.findByOrderIdOrderByCreatedAtDesc(orderId).stream()
                .map(PaymentMapper::toResponse)
                .toList();
    }

    @Transactional
    public PaymentResponse processCallback(BankCallbackRequest request) {
        if (!paymentProcessor.verifyCallback(request)) {
            throw new BusinessException("Invalid bank callback signature");
        }

        Payment payment = paymentRepository.findByTransactionCode(request.getTransactionCode())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Payment not found with transaction code: " + request.getTransactionCode()));

        if (payment.getStatus() != PaymentStatus.CHO_THANH_TOAN) {
            return PaymentMapper.toResponse(payment);
        }

        if (Boolean.TRUE.equals(request.getSuccess())) {
            orderClient.markOrderAsPaid(payment.getOrderId());
            payment.setStatus(PaymentStatus.THANH_CONG);
            payment.setPaidAt(LocalDateTime.now());
        } else {
            payment.setStatus(PaymentStatus.THAT_BAI);
        }

        payment.setUpdatedAt(LocalDateTime.now());
        return PaymentMapper.toResponse(paymentRepository.save(payment));
    }

    @Transactional
    public PaymentResponse cancelPayment(Long id) {
        Payment payment = findPayment(id);
        if (payment.getStatus() != PaymentStatus.CHO_THANH_TOAN) {
            throw new BusinessException("Only a pending payment can be cancelled");
        }

        payment.setStatus(PaymentStatus.DA_HUY);
        payment.setUpdatedAt(LocalDateTime.now());
        return PaymentMapper.toResponse(paymentRepository.save(payment));
    }

    private Payment findPayment(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));
    }

    private void validateOrder(OrderResponse order) {
        if (order.getTotalAmount() == null || order.getTotalAmount().signum() <= 0) {
            throw new BusinessException("Order amount must be greater than zero");
        }
        if (!"PENDING_PAYMENT".equals(order.getStatus())) {
            throw new BusinessException("Order is not waiting for payment");
        }
        if (paymentRepository.existsByOrderIdAndStatus(order.getId(), PaymentStatus.THANH_CONG)) {
            throw new BusinessException("Order has already been paid");
        }
    }
}
