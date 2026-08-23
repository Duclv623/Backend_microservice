package payment_service.processor;

import org.springframework.stereotype.Component;

import payment_service.dto.BankCallbackRequest;
import payment_service.dto.BankPaymentResult;
import payment_service.entity.Payment;
import payment_service.service.BankGatewayService;

@Component
public class MockBankPaymentProcessor implements PaymentProcessor {

    private final BankGatewayService bankGatewayService;

    public MockBankPaymentProcessor(BankGatewayService bankGatewayService) {
        this.bankGatewayService = bankGatewayService;
    }

    @Override
    public BankPaymentResult createPayment(Payment payment) {
        return bankGatewayService.createPayment(payment);
    }

    @Override
    public boolean verifyCallback(BankCallbackRequest request) {
        // The mock bank has no signature. A real processor must verify it here.
        return true;
    }
}
