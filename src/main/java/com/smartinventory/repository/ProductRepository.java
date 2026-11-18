
package com.smartinventory.repository;

/**
 *
 * @author justi
 */

import com.smartinventory.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}
