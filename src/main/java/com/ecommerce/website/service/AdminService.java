package com.ecommerce.website.service;

import com.ecommerce.website.dao.base.CategoryRepository;
import com.ecommerce.website.dao.base.ProductRepository;
import com.ecommerce.website.dao.user.UserRepository;
import com.ecommerce.website.model.base.Category;
import com.ecommerce.website.model.base.Product;
import com.ecommerce.website.model.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    private static final Logger logger = LoggerFactory.getLogger(AdminService.class);
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Autowired
    public AdminService(ProductRepository productRepository, CategoryRepository categoryRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    public Product findProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public Category findCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public User findUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public void deleteCategory(Long id) {
        try {
            categoryRepository.deleteById(id);
        } catch (Exception e) {
            logger.error("Error deleting category with ID " + id, e);
        }
    }

    public void deleteProduct(Long id) {
        try {
            productRepository.deleteById(id);
        } catch (Exception e) {
            logger.error("Error deleting product with ID " + id, e);
        }
    }

    public void deleteUser(Long id) {
        try {
            userRepository.deleteById(id);
        } catch (Exception e) {
            logger.error("Error deleting user with ID " + id, e);
        }
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public void saveCategory(Category category) {
        categoryRepository.save(category);
    }

    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    public List<Category> findALLCategories() {
        return categoryRepository.findAll();
    }

    public Page<User> findUserByLoginContaining(String search, Pageable pageable) {
        return userRepository.findByLoginContaining(search, pageable);
    }

    public Page<User> findUserAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public Page<Category> findCategoriesAll(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    public Page<Category> findCategoriesByNameContaining(String search, Pageable pageable) {
        return categoryRepository.findByNameContaining(search, pageable);
    }

    public Page<Product> findProductAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Page<Product> findProductByNameContaining(String search, Pageable pageable) {
        return productRepository.findByNameContaining(search, pageable);
    }
}
