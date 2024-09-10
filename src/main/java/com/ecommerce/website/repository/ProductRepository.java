package com.ecommerce.website.repository;

import com.ecommerce.website.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
