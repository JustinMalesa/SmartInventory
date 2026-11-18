
package com.smartinventory.controller;

/**
 *
 * @author justi
 */

import com.smartinventory.model.Product;
import com.smartinventory.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // Create Product
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }

    // Get All Products
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    // Get Product By ID
    @GetMapping("/{id}")
    public Optional<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    // Delete Product
    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable Long id) {

        productService.deleteProduct(id);

        return "Product deleted successfully";
    }
    @PutMapping("/{id}")
public Product updateProduct(
        @PathVariable Long id,
        @RequestBody Product product) {

    return productService.updateProduct(id, product);
}
@GetMapping("/inventory-value")
public double getInventoryValue() {

    return productService.calculateInventoryValue();
}
@GetMapping("/low-stock")
public List<Product> getLowStockProducts() {

    return productService.getLowStockProducts();
}
@GetMapping("/expiring-soon")
public List<Product> getExpiringProducts() {

    return productService.getExpiringProducts();
}
}
