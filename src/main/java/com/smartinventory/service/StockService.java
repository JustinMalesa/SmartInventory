
package com.smartinventory.service;

/**
 *
 * @author justi
 */

import com.smartinventory.exception.ResourceNotFoundException;
import com.smartinventory.enums.MovementType;
import com.smartinventory.model.Product;
import com.smartinventory.model.StockMovement;
import com.smartinventory.repository.ProductRepository;
import com.smartinventory.repository.StockMovementRepository;
import java.time.LocalDate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class StockService {

    private final ProductRepository productRepository;
    private final StockMovementRepository stockMovementRepository;

    // Constructor injection.
    public StockService(
            ProductRepository productRepository,
            StockMovementRepository stockMovementRepository) {

        this.productRepository = productRepository;
        this.stockMovementRepository = stockMovementRepository;
    }

    // Record incoming stock.
    @Transactional
    public String recordInbound(Long productId, Integer quantity) {

        validateQuantity(quantity);

        Product product = productRepository.findByIdForUpdate(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product with ID " + productId + " was not found."
                ));

        int currentQuantity = product.getCurrentQuantity();

        int newQuantity;

        try {
            newQuantity = Math.addExact(currentQuantity, quantity);
        } catch (ArithmeticException exception) {
            throw new IllegalArgumentException(
                    "Stock quantity exceeds the supported limit."
            );
        }

        if (newQuantity > product.getMaxCapacity()) {
            throw new IllegalArgumentException(
                    "Inbound stock exceeds maximum product capacity."
            );
        }

        product.setCurrentQuantity(newQuantity);
        productRepository.save(product);

        saveMovement(product, MovementType.INBOUND, quantity);

        return "Inbound stock recorded successfully.";
    }

    // Record outgoing stock.
    @Transactional
    public String recordOutbound(Long productId, Integer quantity) {

        validateQuantity(quantity);

        Product product = productRepository.findByIdForUpdate(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product with ID " + productId + " was not found."
                ));

        int currentQuantity = product.getCurrentQuantity();

        if (currentQuantity < quantity) {
            throw new IllegalArgumentException(
                    "Insufficient stock available."
            );
        }

        product.setCurrentQuantity(currentQuantity - quantity);
        productRepository.save(product);

        saveMovement(product, MovementType.OUTBOUND, quantity);

        return "Outbound stock recorded successfully.";
    }

    // Validate the requested movement quantity.
    private void validateQuantity(Integer quantity) {

        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than zero."
            );
        }
    }

    // Save the stock movement history.
    private void saveMovement(
            Product product,
            MovementType type,
            Integer quantity) {

        StockMovement movement = new StockMovement();

        movement.setMovementType(type);
        movement.setQuantity(quantity);
        movement.setMovementDate(LocalDate.now());
        movement.setProduct(product);

        stockMovementRepository.save(movement);
    }
}