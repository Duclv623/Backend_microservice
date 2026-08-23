package payment_service.mapper;

import payment_service.dto.PaymentResponse;
import payment_service.entity.Payment;

public final class PaymentMapper {

    private PaymentMapper() {
    }

    public static PaymentResponse toResponse(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getOrderId(),
                payment.getAmount(),
                payment.getMethod(),
                payment.getStatus(),
                payment.getTransactionCode(),
                payment.getBankCode(),
                payment.getPaymentUrl(),
                payment.getCreatedAt(),
                payment.getUpdatedAt(),
                payment.getPaidAt());
    }
}
