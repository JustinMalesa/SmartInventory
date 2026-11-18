package com.smartinventory.model;

/**
 *
 * @author justi
 */

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import com.smartinventory.enums.Category;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @NotBlank
    private String productName;

    @NotBlank
    private String sku;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Min(0)
    private Integer currentQuantity;

    @Min(1)
    private Integer maxCapacity;
    
    @DecimalMin("0.0")
    private BigDecimal costPrice;

    @DecimalMin("0.0")
    private BigDecimal retailPrice;

    private LocalDate expiryDate;

    public Product() {
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Integer getCurrentQuantity() {
    return currentQuantity;
}

public void setCurrentQuantity(Integer currentQuantity) {
    this.currentQuantity = currentQuantity;
}

public Integer getMaxCapacity() {
    return maxCapacity;
}

public void setMaxCapacity(Integer maxCapacity) {
    this.maxCapacity = maxCapacity;
}

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public BigDecimal getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(BigDecimal retailPrice) {
        this.retailPrice = retailPrice;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }
}
