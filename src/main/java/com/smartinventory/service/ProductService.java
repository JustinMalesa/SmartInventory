
package com.smartinventory.service;

/**
 *
 * @author justi
 */

import com.smartinventory.model.Product;
import com.smartinventory.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // Create Product
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    // Get All Products
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Get Product By ID
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    // Delete Product
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
    
    public Product updateProduct(Long id, Product updatedProduct) {

    Product product =
            productRepository.findById(id).orElse(null);

    if (product != null) {

        product.setProductName(
                updatedProduct.getProductName());

        product.setSku(
                updatedProduct.getSku());

        product.setCategory(
                updatedProduct.getCategory());

       product.setCurrentQuantity(
        updatedProduct.getCurrentQuantity());

        product.setMaxCapacity(
        updatedProduct.getMaxCapacity());

        product.setCostPrice(
                updatedProduct.getCostPrice());

        product.setRetailPrice(
                updatedProduct.getRetailPrice());

        product.setExpiryDate(
                updatedProduct.getExpiryDate());

        return productRepository.save(product);
    }

    return null;
}
    public double calculateInventoryValue() {

    double totalValue = 0;

    List<Product> products =
            productRepository.findAll();

    for (Product product : products) {

        totalValue +=
                product.getCurrentQuantity()
                * product.getCostPrice().doubleValue();
    }

    return totalValue;
}
    public List<Product> getLowStockProducts() {

    List<Product> lowStockProducts = new java.util.ArrayList<>();

    for (Product product : productRepository.findAll()) {

        if (product.getCurrentQuantity()
                <= com.smartinventory.config.Constants.MIN_STOCK_THRESHOLD) {

            lowStockProducts.add(product);
        }
    }

    return lowStockProducts;
}
    public List<Product> getExpiringProducts() {

    LocalDate today = LocalDate.now();

    LocalDate thirtyDaysLater = today.plusDays(30);

    return productRepository.findAll()
            .stream()
            .filter(product ->
                    product.getExpiryDate() != null
                    && !product.getExpiryDate().isBefore(today)
                    && !product.getExpiryDate().isAfter(thirtyDaysLater))
            .toList();
}
}
