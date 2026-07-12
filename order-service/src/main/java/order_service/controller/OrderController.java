package order_service.controller;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class OrderController {
    /**
     * @return
     */
    @GetMapping("/api/orders")
    public List<String> getOrders() {
        return List.of("Đơn hàng 1", "Đơn hàng 2", "Đơn hàng 3");
    }
}