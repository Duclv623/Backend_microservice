package payment_service.processor;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import payment_service.enums.PaymentMethod;
import payment_service.exception.BusinessException;

@Component
public class PaymentProcessorFactory {

    private final Map<PaymentMethod, PaymentProcessor> processors;

    public PaymentProcessorFactory(List<PaymentProcessor> paymentProcessors) {
        this.processors = new EnumMap<>(PaymentMethod.class);

        for (PaymentProcessor processor : paymentProcessors) {
            processors.put(processor.getSupportedMethod(), processor);
        }
    }

    public PaymentProcessor getProcessor(PaymentMethod method) {
        PaymentProcessor processor = processors.get(method);

        if (processor == null) {
            throw new BusinessException("Payment method is not supported: " + method);
        }

        return processor;
    }
}
