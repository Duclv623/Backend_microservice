package payment_service.processor;

import payment_service.dto.BankCallbackRequest;
import payment_service.dto.BankPaymentResult;
import payment_service.entity.Payment;
import payment_service.enums.PaymentMethod;

public interface PaymentProcessor {

    PaymentMethod getSupportedMethod();

    BankPaymentResult createPayment(Payment payment);

    boolean verifyCallback(BankCallbackRequest request);
}
