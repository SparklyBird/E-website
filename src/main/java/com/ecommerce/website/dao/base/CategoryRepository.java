package com.ecommerce.website.dao.base;

import com.ecommerce.website.model.base.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);
}
