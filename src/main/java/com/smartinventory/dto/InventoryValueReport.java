
package com.smartinventory.dto;

/**
 *
 * @author justi
 */

import java.util.Map;

public class InventoryValueReport {

    private final String totalValue;
    private final Map<String, String> byCategory;

    public InventoryValueReport(String totalValue,
                                Map<String, String> byCategory) {
        this.totalValue = totalValue;
        this.byCategory = byCategory;
    }

    public String totalValue() {
        return totalValue;
    }

    public Map<String, String> byCategory() {
        return byCategory;
    }
}