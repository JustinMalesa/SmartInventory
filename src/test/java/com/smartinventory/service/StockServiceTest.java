
package com.smartinventory.service;

/**
 *
 * @author justi
 */

import com.smartinventory.enums.MovementType;
import com.smartinventory.exception.ResourceNotFoundException;
import com.smartinventory.model.Product;
import com.smartinventory.model.StockMovement;
import com.smartinventory.repository.ProductRepository;
import com.smartinventory.repository.StockMovementRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private StockMovementRepository stockMovementRepository;

    @InjectMocks
    private StockService stockService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setProductId(1L);
        product.setProductName("Laptop");
        product.setSku("LAP001");
        product.setCurrentQuantity(10);
        product.setMaxCapacity(50);
    }

    @Test
    void recordInboundShouldIncreaseStock() {

        when(productRepository.findByIdForUpdate(1L))
                .thenReturn(Optional.of(product));

        stockService.recordInbound(1L, 5);

        assertEquals(15, product.getCurrentQuantity());

        verify(productRepository).save(product);
        verify(stockMovementRepository).save(any(StockMovement.class));
    }

    @Test
    void recordOutboundShouldDecreaseStock() {

        when(productRepository.findByIdForUpdate(1L))
                .thenReturn(Optional.of(product));

        stockService.recordOutbound(1L, 3);

        assertEquals(7, product.getCurrentQuantity());

        verify(productRepository).save(product);
        verify(stockMovementRepository).save(any(StockMovement.class));
    }

    @Test
    void recordOutboundShouldRejectInsufficientStock() {

        when(productRepository.findByIdForUpdate(1L))
                .thenReturn(Optional.of(product));

        assertThrows(
                IllegalArgumentException.class,
                () -> stockService.recordOutbound(1L, 15)
        );

        assertEquals(10, product.getCurrentQuantity());
    }

    @Test
    void recordInboundShouldRejectStockAboveCapacity() {

        when(productRepository.findByIdForUpdate(1L))
                .thenReturn(Optional.of(product));

        assertThrows(
                IllegalArgumentException.class,
                () -> stockService.recordInbound(1L, 45)
        );

        assertEquals(10, product.getCurrentQuantity());
    }

    @Test
    void recordInboundShouldRejectZeroQuantity() {

        assertThrows(
                IllegalArgumentException.class,
                () -> stockService.recordInbound(1L, 0)
        );
    }

    @Test
    void recordOutboundShouldRejectNegativeQuantity() {

        assertThrows(
                IllegalArgumentException.class,
                () -> stockService.recordOutbound(1L, -2)
        );
    }

    @Test
    void recordInboundShouldRejectMissingProduct() {

        when(productRepository.findByIdForUpdate(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> stockService.recordInbound(1L, 5)
        );
    }
}