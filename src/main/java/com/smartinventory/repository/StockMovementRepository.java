package com.smartinventory.repository;

/**
 *
 * @author justi
 */
import com.smartinventory.model.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {
}
