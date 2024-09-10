package com.ecommerce.website.repository;

import com.ecommerce.website.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}