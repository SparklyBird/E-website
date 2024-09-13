package com.ecommerce.website.service;

import com.ecommerce.website.dao.base.ProductRepository;
import com.ecommerce.website.dao.base.CategoryRepository;
import com.ecommerce.website.dao.user.UserRepository;
import com.ecommerce.website.model.base.Product;
import com.ecommerce.website.model.base.Category;
import com.ecommerce.website.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Autowired
    public AdminService(ProductRepository productRepository, CategoryRepository categoryRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    public Page<Product> searchProducts(String search, Pageable pageable) {
        return productRepository.findByNameContaining(search, pageable);
    }

    public Page<Category> searchCategories(String search, Pageable pageable) {
        return categoryRepository.findByNameContaining(search, pageable);
    }

    public Page<User> searchUsers(String search, Pageable pageable) {
        return userRepository.findByLoginContaining(search, pageable);
    }
}
