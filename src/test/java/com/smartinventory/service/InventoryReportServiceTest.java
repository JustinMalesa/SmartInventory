
package com.smartinventory.service;

/**
 *
 * @author justi
 */

import com.smartinventory.dto.InventoryValueReport;
import com.smartinventory.enums.Category;
import com.smartinventory.model.Product;
import com.smartinventory.repository.ProductRepository;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InventoryReportServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private InventoryReportService inventoryReportService;

    @Test
    void generateReportShouldCalculateInventoryValue() {

        Product laptop = new Product();
        laptop.setCostPrice(new BigDecimal("8000.00"));
        laptop.setCurrentQuantity(2);
        laptop.setCategory(Category.ELECTRONICS);

        Product mouse = new Product();
        mouse.setCostPrice(new BigDecimal("200.00"));
        mouse.setCurrentQuantity(5);
        mouse.setCategory(Category.ELECTRONICS);

        when(productRepository.findAll())
                .thenReturn(List.of(laptop, mouse));

        InventoryValueReport report =
                inventoryReportService.generateInventoryValueReport();

        assertEquals("17000.00", report.totalValue());
        assertEquals("17000.00", report.byCategory().get("ELECTRONICS"));
    }

    @Test
    void generateReportShouldHandleEmptyInventory() {

        when(productRepository.findAll())
                .thenReturn(List.of());

        InventoryValueReport report =
                inventoryReportService.generateInventoryValueReport();

        assertEquals("0.00", report.totalValue());
        assertEquals(0, report.byCategory().size());
    }
}