package com.ecommerce.website.service;

import com.ecommerce.website.model.base.Category;
import com.ecommerce.website.dao.base.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public String getCategoryNameById(Long categoryId) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        return category.map(Category::getName).orElse("Unknown Category");
    }
    public List<Category> categoryList(){
        return categoryRepository.findAll();
    }

}
