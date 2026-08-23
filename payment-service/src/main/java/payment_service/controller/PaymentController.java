package payment_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import payment_service.dto.BankCallbackRequest;
import payment_service.dto.CreatePaymentRequest;
import payment_service.dto.PaymentResponse;
import payment_service.service.PaymentService;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentResponse createPayment(@Valid @RequestBody CreatePaymentRequest request) {
        return paymentService.createPayment(request);
    }

    @GetMapping
    public List<PaymentResponse> getAllPayments() {
        return paymentService.getAllPayments();
    }

    @GetMapping("/{id}")
    public PaymentResponse getPaymentById(@PathVariable Long id) {
        return paymentService.getPaymentById(id);
    }

    @GetMapping("/order/{orderId}")
    public List<PaymentResponse> getPaymentsByOrderId(@PathVariable Long orderId) {
        return paymentService.getPaymentsByOrderId(orderId);
    }

    @PostMapping("/callback")
    public PaymentResponse processCallback(@Valid @RequestBody BankCallbackRequest request) {
        return paymentService.processPaymentCallback(request);
    }

    @PostMapping("/mock-bank/{transactionCode}")
    public PaymentResponse simulateBankResult(@PathVariable String transactionCode,
                                              @RequestParam Boolean success) {
        BankCallbackRequest request = new BankCallbackRequest();
        request.setTransactionCode(transactionCode);
        request.setSuccess(success);
        return paymentService.processPaymentCallback(request);
    }

    @PatchMapping("/{id}/cancel")
    public PaymentResponse cancelPayment(@PathVariable Long id) {
        return paymentService.cancelPayment(id);
    }
}
