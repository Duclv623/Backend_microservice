package payment_service.dto;

import jakarta.validation.constraints.NotNull;
import payment_service.enums.PaymentMethod;

public class CreatePaymentRequest {

    @NotNull(message = "orderId is required")
    private Long orderId;

    @NotNull(message = "method is required")
    private PaymentMethod method;

    private String bankCode;

    public CreatePaymentRequest() {
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public PaymentMethod getMethod() {
        return method;
    }

    public void setMethod(PaymentMethod method) {
        this.method = method;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
}
