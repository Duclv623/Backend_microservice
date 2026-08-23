package payment_service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import payment_service.enums.PaymentMethod;
import payment_service.enums.PaymentStatus;

public class PaymentResponse {
    private final Long id;
    private final Long orderId;
    private final BigDecimal amount;
    private final PaymentMethod method;
    private final PaymentStatus status;
    private final String transactionCode;
    private final String bankCode;
    private final String paymentUrl;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final LocalDateTime paidAt;

    public PaymentResponse(Long id, Long orderId, BigDecimal amount, PaymentMethod method,
                           PaymentStatus status, String transactionCode, String bankCode,
                           String paymentUrl, LocalDateTime createdAt,
                           LocalDateTime updatedAt, LocalDateTime paidAt) {
        this.id = id;
        this.orderId = orderId;
        this.amount = amount;
        this.method = method;
        this.status = status;
        this.transactionCode = transactionCode;
        this.bankCode = bankCode;
        this.paymentUrl = paymentUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.paidAt = paidAt;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public PaymentMethod getMethod() {
        return method;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public String getBankCode() {
        return bankCode;
    }

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }
}
