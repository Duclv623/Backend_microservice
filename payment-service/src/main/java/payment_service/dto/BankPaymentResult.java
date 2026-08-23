package payment_service.dto;

public class BankPaymentResult {
    private final String transactionCode;
    private final String paymentUrl;

    public BankPaymentResult(String transactionCode, String paymentUrl) {
        this.transactionCode = transactionCode;
        this.paymentUrl = paymentUrl;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public String getPaymentUrl() {
        return paymentUrl;
    }
}
