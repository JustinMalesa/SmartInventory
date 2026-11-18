
package com.smartinventory.service;

/**
 *
 * @author justi
 */

import com.smartinventory.exception.ResourceNotFoundException;
import com.smartinventory.model.Product;
import com.smartinventory.repository.ProductRepository;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setProductId(1L);
        product.setProductName("Laptop");
        product.setSku("LAP001");
        product.setCurrentQuantity(10);
        product.setMaxCapacity(50);
        product.setCostPrice(new BigDecimal("8000.00"));
        product.setRetailPrice(new BigDecimal("9500.00"));
    }

    @Test
    void saveProductShouldSaveValidProduct() {

        when(productRepository.existsBySku("LAP001"))
                .thenReturn(false);

        when(productRepository.save(product))
                .thenReturn(product);

        Product savedProduct = productService.saveProduct(product);

        assertEquals("LAP001", savedProduct.getSku());

        verify(productRepository).save(product);
    }

    @Test
    void saveProductShouldRejectDuplicateSku() {

        when(productRepository.existsBySku("LAP001"))
                .thenReturn(true);

        assertThrows(
                IllegalArgumentException.class,
                () -> productService.saveProduct(product)
        );
    }

    @Test
    void saveProductShouldRejectQuantityAboveCapacity() {

        product.setCurrentQuantity(60);

        when(productRepository.existsBySku("LAP001"))
                .thenReturn(false);

        assertThrows(
                IllegalArgumentException.class,
                () -> productService.saveProduct(product)
        );
    }

    @Test
    void updateProductShouldUpdateProductDetails() {

        Product updatedProduct = new Product();
        updatedProduct.setProductName("Updated Laptop");
        updatedProduct.setSku("LAP001");
        updatedProduct.setMaxCapacity(60);
        updatedProduct.setCostPrice(new BigDecimal("8500.00"));
        updatedProduct.setRetailPrice(new BigDecimal("10000.00"));

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        when(productRepository.existsBySkuAndProductIdNot("LAP001", 1L))
                .thenReturn(false);

        when(productRepository.save(product))
                .thenReturn(product);

        Product result = productService.updateProduct(1L, updatedProduct);

        assertEquals("Updated Laptop", result.getProductName());
        assertEquals(10, result.getCurrentQuantity());
        assertEquals(60, result.getMaxCapacity());
    }

    @Test
    void updateProductShouldRejectMissingProduct() {

        when(productRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> productService.updateProduct(1L, product)
        );
    }

    @Test
    void deleteProductShouldRejectMissingProduct() {

        when(productRepository.existsById(1L))
                .thenReturn(false);

        assertThrows(
                ResourceNotFoundException.class,
                () -> productService.deleteProduct(1L)
        );
    }
}