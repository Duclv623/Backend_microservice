package payment_service.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import payment_service.dto.BankPaymentResult;
import payment_service.entity.Payment;

@Service
public class BankGatewayService {

    private final String paymentPublicUrl;

    public BankGatewayService(@Value("${payment.public-url}") String paymentPublicUrl) {
        this.paymentPublicUrl = paymentPublicUrl;
    }

    public BankPaymentResult createPayment(Payment payment) {
        String transactionCode = "PAY-" + UUID.randomUUID().toString().replace("-", "").toUpperCase();
        String paymentUrl = paymentPublicUrl + "/api/payments/mock-bank/" + transactionCode;
        return new BankPaymentResult(transactionCode, paymentUrl);
    }
}
