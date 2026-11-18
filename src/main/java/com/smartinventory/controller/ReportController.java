
package com.smartinventory.controller;

/**
 *
 * @author justi
 */

import com.smartinventory.dto.InventoryValueReport;
import com.smartinventory.service.InventoryReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final InventoryReportService inventoryReportService;

    public ReportController(InventoryReportService inventoryReportService) {
        this.inventoryReportService = inventoryReportService;
    }

    @GetMapping("/value")
    public ResponseEntity<InventoryValueReport> getInventoryValueReport() {

        InventoryValueReport report =
                inventoryReportService.generateInventoryValueReport();

        return ResponseEntity.ok(report);
    }
}
