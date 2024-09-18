package com.ecommerce.website.dao.base;

import com.ecommerce.website.model.base.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);
    Page<Category> findByNameContaining(String name, Pageable pageable);
}

