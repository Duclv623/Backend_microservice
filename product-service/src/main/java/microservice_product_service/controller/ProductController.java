package microservice_product_service.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @GetMapping
    public List<String> getProducts() {
        return List.of("Điện Thoại", "Laptop", "Tablet", "Tai Nghe", "Loa Bluetooth");
    }
    
}
