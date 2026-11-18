
package com.smartinventory.service;

/**
 *
 * @author justi
 */

import com.smartinventory.config.Constants;
import com.smartinventory.exception.ResourceNotFoundException;
import com.smartinventory.model.Product;
import com.smartinventory.repository.ProductRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    // Constructor injection.
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Create a new product.
    @Transactional
    public Product saveProduct(Product product) {

        if (productRepository.existsBySku(product.getSku())) {
            throw new IllegalArgumentException(
                    "A product with this SKU already exists."
            );
        }

        if (product.getCurrentQuantity() > product.getMaxCapacity()) {
            throw new IllegalArgumentException(
                    "Current quantity cannot exceed maximum capacity."
            );
        }

        return productRepository.save(product);
    }

    // Retrieve all products.
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Retrieve a product by ID.
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    // Delete a product.
    @Transactional
    public void deleteProduct(Long id) {

        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Product with ID " + id + " was not found."
            );
        }

        productRepository.deleteById(id);
    }

    // Update product details.
    @Transactional
    public Product updateProduct(Long id, Product updatedProduct) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product with ID " + id + " was not found."
                ));

        if (productRepository.existsBySkuAndProductIdNot(
                updatedProduct.getSku(), id)) {
            throw new IllegalArgumentException(
                    "Another product already uses this SKU."
            );
        }

        if (updatedProduct.getMaxCapacity() < product.getCurrentQuantity()) {
            throw new IllegalArgumentException(
                    "Maximum capacity cannot be lower than current stock."
            );
        }

        product.setProductName(updatedProduct.getProductName());
        product.setSku(updatedProduct.getSku());
        product.setCategory(updatedProduct.getCategory());
        product.setMaxCapacity(updatedProduct.getMaxCapacity());
        product.setCostPrice(updatedProduct.getCostPrice());
        product.setRetailPrice(updatedProduct.getRetailPrice());
        product.setExpiryDate(updatedProduct.getExpiryDate());

        // Current quantity is deliberately not changed here.
        // Stock changes must go through inbound/outbound movements.

        return productRepository.save(product);
    }

    // Retrieve products at or below the low-stock threshold.
    public List<Product> getLowStockProducts() {

        return productRepository.findByCurrentQuantityLessThanEqual(
                Constants.MIN_STOCK_THRESHOLD
        );
    }

    // Retrieve products expiring within the next 30 days.
    public List<Product> getExpiringProducts() {

        LocalDate today = LocalDate.now();
        LocalDate thirtyDaysLater = today.plusDays(30);

        return productRepository.findByExpiryDateBetween(
                today, thirtyDaysLater
        );
    }
}