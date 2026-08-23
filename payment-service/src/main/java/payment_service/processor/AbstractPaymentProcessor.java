package payment_service.processor;

import java.util.UUID;

import payment_service.dto.BankCallbackRequest;
import payment_service.dto.BankPaymentResult;
import payment_service.entity.Payment;

public abstract class AbstractPaymentProcessor implements PaymentProcessor {

    private final String paymentPublicUrl;

    protected AbstractPaymentProcessor(String paymentPublicUrl) {
        this.paymentPublicUrl = paymentPublicUrl;
    }

    protected abstract String getTransactionPrefix();

    @Override
    public BankPaymentResult createPayment(Payment payment) {
        String transactionCode = getTransactionPrefix()
                + UUID.randomUUID().toString().replace("-", "").toUpperCase();
        String paymentUrl = paymentPublicUrl
                + "/api/payments/simulate/"
                + transactionCode;

        return new BankPaymentResult(transactionCode, paymentUrl);
    }

    @Override
    public boolean verifyCallback(BankCallbackRequest request) {
        // Override this method to verify HMAC/checksum for a real provider.
        return true;
    }
}
