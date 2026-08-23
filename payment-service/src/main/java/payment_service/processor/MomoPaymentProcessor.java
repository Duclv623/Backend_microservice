package payment_service.processor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import payment_service.enums.PaymentMethod;

@Component
public class MomoPaymentProcessor extends AbstractPaymentProcessor {

    public MomoPaymentProcessor(@Value("${payment.public-url}") String paymentPublicUrl) {
        super(paymentPublicUrl);
    }

    @Override
    public PaymentMethod getSupportedMethod() {
        return PaymentMethod.MOMO;
    }

    @Override
    protected String getTransactionPrefix() {
        return "MOMO-";
    }
}
