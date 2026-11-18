
package com.smartinventory.service;

/**
 *
 * @author justi
 */

import com.smartinventory.dto.InventoryValueReport;
import com.smartinventory.model.Product;
import com.smartinventory.repository.ProductRepository;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class InventoryReportService {

    private final ProductRepository productRepository;

    public InventoryReportService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public InventoryValueReport generateInventoryValueReport() {

        BigDecimal totalValue = BigDecimal.ZERO;

        Map<String, BigDecimal> categoryValues = new LinkedHashMap<>();

        for (Product product : productRepository.findAll()) {

            BigDecimal productValue = product.getCostPrice()
                    .multiply(BigDecimal.valueOf(product.getCurrentQuantity()));

            totalValue = totalValue.add(productValue);

            String category = product.getCategory().name();

            categoryValues.merge(
                    category,
                    productValue,
                    BigDecimal::add
            );
        }

        Map<String, String> formattedCategoryValues = new LinkedHashMap<>();

        categoryValues.forEach((category, value) ->
                formattedCategoryValues.put(category, value.setScale(2, java.math.RoundingMode.HALF_UP).toPlainString())
        );

        return new InventoryValueReport(
                totalValue.setScale(2, java.math.RoundingMode.HALF_UP).toPlainString(),
                formattedCategoryValues
        );
    }
}
