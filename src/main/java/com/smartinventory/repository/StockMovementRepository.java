package com.smartinventory.repository;

/**
 *
 * @author justi
 */

import com.smartinventory.enums.MovementType;
import com.smartinventory.model.StockMovement;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {

    // Retrieve all movements belonging to a specific product.
    List<StockMovement> findByProduct_ProductId(Long productId);

    // Retrieve a product's movement history, newest first.
    List<StockMovement> findByProduct_ProductIdOrderByMovementDateDesc(Long productId);

    // Retrieve movements of a specific type.
    List<StockMovement> findByMovementType(MovementType movementType);

    // Retrieve all movements, newest first.
    List<StockMovement> findAllByOrderByMovementDateDesc();
}