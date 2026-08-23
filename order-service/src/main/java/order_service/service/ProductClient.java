package order_service.service;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import order_service.dto.ProductResponse;
import order_service.exception.BusinessException;

@Component
public class ProductClient {

    private final RestClient productRestClient;

    public ProductClient(RestClient productRestClient) {
        this.productRestClient = productRestClient;
    }

    public ProductResponse getProduct(Long productId) {
        try {
            ProductResponse product = productRestClient.get()
                    .uri("/api/products/{id}", productId)
                    .retrieve()
                    .body(ProductResponse.class);

            if (product == null) {
                throw new BusinessException("Product service returned empty data for product: " + productId);
            }
            return product;
        } catch (RestClientResponseException exception) {
            throw new BusinessException("Cannot get product with id: " + productId);
        } catch (BusinessException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new BusinessException("Product service is unavailable");
        }
    }
}
