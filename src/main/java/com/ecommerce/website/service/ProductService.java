package com.ecommerce.website.service;

import com.ecommerce.website.dao.base.ProductRepository;
import com.ecommerce.website.model.base.Product;
import jakarta.persistence.EntityNotFoundException;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private static final Logger LOGGER = Logger.getLogger(ProductService.class.getName());
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Page<Product> getProductsByCategory(Long categoryId, Pageable pageable) {
        return productRepository.findByCategoryId(categoryId, pageable);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public Product getProductByIdWithAttributes(Long id) {
        return productRepository.findByIdWithAttributes(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public List<Product> getRandomProducts() {
        List<Product> allProducts = productRepository.findAll();
        Collections.shuffle(allProducts); // Shuffle to randomize
        return allProducts.stream().limit(8).collect(Collectors.toList()); // Return only 8
    }

    public Page<Product> searchProducts(String query, Pageable pageable) {
        Page<Product> initialResults = productRepository.searchByNameDescriptionCategoryOrAttribute(query, pageable);
        LevenshteinDistance distance = new LevenshteinDistance(2);

        List<Product> typoTolerantResults = initialResults.getContent().stream()
                .filter(product ->
                        distance.apply(product.getName().toLowerCase(), query.toLowerCase()) <= 2 ||
                                distance.apply(product.getDescription().toLowerCase(), query.toLowerCase()) <= 2 ||
                                (product.getCategory() != null && distance.apply(product.getCategory().getName().toLowerCase(), query.toLowerCase()) <= 2)
                )
                .collect(Collectors.toList());

        return new PageImpl<>(typoTolerantResults, pageable, initialResults.getTotalElements());
    }

    public List<String> getSuggestions(String query) {
        List<Product> initialResults = productRepository.findByNameContaining(query, PageRequest.of(0, 10)).getContent();
        LevenshteinDistance levenshtein = new LevenshteinDistance(2);

        LOGGER.info("User query: " + query);

        List<String> suggestions = initialResults.stream()
                .filter(product ->
                        levenshtein.apply(product.getName().toLowerCase(), query.toLowerCase()) <= 2 ||
                                levenshtein.apply(product.getDescription().toLowerCase(), query.toLowerCase()) <= 2 ||
                                (product.getCategory() != null && levenshtein.apply(product.getCategory().getName().toLowerCase(), query.toLowerCase()) <= 2)
                )
                .map(Product::getName)
                .distinct()
                .limit(5)
                .collect(Collectors.toList());

        LOGGER.info("Suggestions: " + suggestions);

        return suggestions;
    }

    public Product findById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + productId));
    }
}
