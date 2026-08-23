package payment_service.processor;

import payment_service.dto.BankCallbackRequest;
import payment_service.dto.BankPaymentResult;
import payment_service.entity.Payment;

public interface PaymentProcessor {

    BankPaymentResult createPayment(Payment payment);

    boolean verifyCallback(BankCallbackRequest request);
}
