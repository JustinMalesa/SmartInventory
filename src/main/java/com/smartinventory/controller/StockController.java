
package com.smartinventory.controller;

/**
 *
 * @author justi
 */

import com.smartinventory.service.StockService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stock")
public class StockController {

    private final StockService stockService;

    // Constructor injection.
    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    // Record incoming stock.
    @PostMapping("/inbound")
    public ResponseEntity<Map<String, String>> inbound(
            @Valid @RequestBody StockMovementRequest request) {

        String message = stockService.recordInbound(
                request.productId(),
                request.quantity()
        );

        return ResponseEntity.ok(Map.of("message", message));
    }

    // Record outgoing stock.
    @PostMapping("/outbound")
    public ResponseEntity<Map<String, String>> outbound(
            @Valid @RequestBody StockMovementRequest request) {

        String message = stockService.recordOutbound(
                request.productId(),
                request.quantity()
        );

        return ResponseEntity.ok(Map.of("message", message));
    }

    // Request body validation.
    public static class StockMovementRequest {

            @NotNull(message = "Product ID is required")
            private Long productId;

            @NotNull(message = "Quantity is required")
            @Positive(message = "Quantity must be greater than zero")
            private Integer quantity;

    public Long productId() {
        return productId;
    }

    public Integer quantity() {
        return quantity;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
            }
        {
    }
}