
package com.smartinventory.controller;

/**
 *
 * @author justi
 */

import com.smartinventory.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stock")
public class StockController {

    @Autowired
    private StockService stockService;

    @PostMapping("/inbound")
    public String inbound(
            @RequestParam Long productId,
            @RequestParam Integer quantity) {

        return stockService.recordInbound(
                productId,
                quantity);
    }

    @PostMapping("/outbound")
    public String outbound(
            @RequestParam Long productId,
            @RequestParam Integer quantity) {

        return stockService.recordOutbound(
                productId,
                quantity);
    }
}
