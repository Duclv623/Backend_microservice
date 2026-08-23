package payment_service.service;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import payment_service.dto.OrderResponse;
import payment_service.exception.BusinessException;

@Component
public class OrderClient {

    private final RestClient orderRestClient;

    public OrderClient(RestClient orderRestClient) {
        this.orderRestClient = orderRestClient;
    }

    public OrderResponse getOrder(Long orderId) {
        try {
            OrderResponse order = orderRestClient.get()
                    .uri("/api/orders/{id}", orderId)
                    .retrieve()
                    .body(OrderResponse.class);

            if (order == null) {
                throw new BusinessException("Order service returned empty data");
            }
            return order;
        } catch (RestClientResponseException exception) {
            throw new BusinessException("Cannot get order with id: " + orderId);
        } catch (BusinessException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new BusinessException("Order service is unavailable");
        }
    }

    public void markOrderAsPaid(Long orderId) {
        try {
            orderRestClient.patch()
                    .uri("/api/orders/{id}/status", orderId)
                    .body(Map.of("status", "PAID"))
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception exception) {
            throw new BusinessException("Payment succeeded but cannot update order status");
        }
    }
}
