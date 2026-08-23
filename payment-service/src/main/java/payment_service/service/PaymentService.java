package payment_service.service;

import java.util.List;

import payment_service.dto.BankCallbackRequest;
import payment_service.dto.CreatePaymentRequest;
import payment_service.dto.PaymentResponse;

public interface PaymentService {
    
    PaymentResponse createPayment(CreatePaymentRequest paymentRequest);

    List<PaymentResponse> getAllPayments();

    PaymentResponse getPaymentById(Long id);

    List<PaymentResponse> getPaymentsByOrderId(Long orderId);

    PaymentResponse processPaymentCallback(BankCallbackRequest request);

    PaymentResponse cancelPayment(Long paymentId);
}
