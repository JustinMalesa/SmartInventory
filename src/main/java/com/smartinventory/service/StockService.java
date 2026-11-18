
package com.smartinventory.service;

/**
 *
 * @author justi
 */

import com.smartinventory.enums.MovementType;
import com.smartinventory.model.Product;
import com.smartinventory.model.StockMovement;
import com.smartinventory.repository.ProductRepository;
import com.smartinventory.repository.StockMovementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class StockService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockMovementRepository stockMovementRepository;

    @Transactional
    public String recordInbound(Long productId, Integer quantity) {

        if (quantity == null || quantity <= 0) {
            return "Quantity must be greater than zero";
        }

        Product product = productRepository.findById(productId).orElse(null);

        if (product == null) {
            return "Product not found";
        }

        int current = product.getCurrentQuantity() == null ? 0 : product.getCurrentQuantity();
        int newQuantity = current + quantity;

        if (product.getMaxCapacity() != null && newQuantity > product.getMaxCapacity()) {
            return "Exceeds maximum capacity";
        }

        product.setCurrentQuantity(newQuantity);
        productRepository.save(product);
        saveMovement(product, MovementType.INBOUND, quantity);

        return "Inbound stock recorded";
    }

    @Transactional
    public String recordOutbound(Long productId, Integer quantity) {

        if (quantity == null || quantity <= 0) {
            return "Quantity must be greater than zero";
        }

        Product product = productRepository.findById(productId).orElse(null);

        if (product == null) {
            return "Product not found";
        }

        int current = product.getCurrentQuantity() == null ? 0 : product.getCurrentQuantity();

        if (current < quantity) {
            return "Insufficient stock";
        }

        product.setCurrentQuantity(current - quantity);
        productRepository.save(product);
        saveMovement(product, MovementType.OUTBOUND, quantity);

        return "Outbound stock recorded";
    }

    private void saveMovement(Product product, MovementType type, Integer quantity) {
        StockMovement movement = new StockMovement();
        movement.setMovementType(type);
        movement.setQuantity(quantity);
        movement.setMovementDate(LocalDate.now());
        movement.setProduct(product);
        stockMovementRepository.save(movement);
    }
}