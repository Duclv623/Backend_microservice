package microservice_product_service.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import microservice_product_service.dto.ProductRequest;
import microservice_product_service.dto.ProductResponse;
import microservice_product_service.entity.Product;
import microservice_product_service.mapper.ProductMapper;
import microservice_product_service.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts()
        .stream()
        .map(ProductMapper::toResponse)
        .toList();
    }

     @GetMapping("/{id}")
    public ProductResponse getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return ProductMapper.toResponse(product);
    }

    @PostMapping
    public ProductResponse createProduct(@RequestBody ProductRequest request) {
        Product product = ProductMapper.toEntity(request);
        Product savedProduct = productService.createProduct(product);
        return ProductMapper.toResponse(savedProduct);
    }

    @PutMapping("/{id}")
    public ProductResponse updateProduct(@PathVariable Long id, @RequestBody ProductRequest request) {
        Product product = ProductMapper.toEntity(request);
        Product updatedProduct = productService.updateProduct(id, product);
        return ProductMapper.toResponse(updatedProduct);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    @GetMapping("/{id}/available")
    public boolean checkProductAvailable(@PathVariable Long id, @RequestParam Integer quantity) {
        return productService.checkProductAvailable(id, quantity);
    }
}
    
    

