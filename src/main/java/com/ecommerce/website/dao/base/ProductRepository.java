package com.ecommerce.website.dao.base;

import com.ecommerce.website.model.base.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByName(String name);
}
