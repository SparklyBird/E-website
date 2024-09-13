package com.ecommerce.website.dao.base;

import com.ecommerce.website.model.base.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByName(String name);
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);
}
