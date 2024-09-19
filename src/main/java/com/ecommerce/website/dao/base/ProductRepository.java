package com.ecommerce.website.dao.base;

import com.ecommerce.website.model.base.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByName(String name);
    Page<Product> findByNameContaining(String name, Pageable pageable);
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);
    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.attributeValues WHERE p.id = :id")
    Optional<Product> findByIdWithAttributes(@Param("id") Long id);
}

