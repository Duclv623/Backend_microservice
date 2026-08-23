package payment_service.processor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import payment_service.enums.PaymentMethod;

@Component
public class VnPayPaymentProcessor extends AbstractPaymentProcessor {

    public VnPayPaymentProcessor(@Value("${payment.public-url}") String paymentPublicUrl) {
        super(paymentPublicUrl);
    }

    @Override
    public PaymentMethod getSupportedMethod() {
        return PaymentMethod.VNPAY;
    }

    @Override
    protected String getTransactionPrefix() {
        return "VNPAY-";
    }
}
