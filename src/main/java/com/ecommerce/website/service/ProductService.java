package com.ecommerce.website.service;

import com.ecommerce.website.model.base.Product;
import com.ecommerce.website.dao.base.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.stream.Collectors;


import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Page<Product> getProductsByCategory(Long categoryId, Pageable pageable) {
        return productRepository.findByCategoryId(categoryId, pageable);
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public List<Product> getRandomProducts() {
        List<Product> allProducts = productRepository.findAll();
        Collections.shuffle(allProducts); // Shuffle to randomize
        return allProducts.stream().limit(8).collect(Collectors.toList()); // Return only 8
    }

    public Product findById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + productId));
    }
}

