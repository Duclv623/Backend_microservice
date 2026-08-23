package payment_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class BankCallbackRequest {

    @NotBlank(message = "transactionCode is required")
    private String transactionCode;

    @NotNull(message = "success is required")
    private Boolean success;

    public BankCallbackRequest() {
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
