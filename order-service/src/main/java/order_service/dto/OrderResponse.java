package order_service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import order_service.enums.OrderStatus;

public class OrderResponse {
    private final Long id;
    private final Long userId;
    private final BigDecimal totalAmount;
    private final OrderStatus status;
    private final String shippingAddress;
    private final List<OrderItemResponse> items;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public OrderResponse(Long id, Long userId, BigDecimal totalAmount, OrderStatus status,
                         String shippingAddress, List<OrderItemResponse> items,
                         LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.status = status;
        this.shippingAddress = shippingAddress;
        this.items = items;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public OrderStatus getStatus() { return status; }
    public String getShippingAddress() { return shippingAddress; }
    public List<OrderItemResponse> getItems() { return items; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
