package com.ecommerce.website.service;

import com.ecommerce.website.model.base.Product;

import java.math.BigDecimal;
import java.util.Map;

public interface ShoppingCartService {
    void addProduct(Product product);

    void removeProduct(Product product);

    void decreaseProduct(Product product);

    Map<Product, Integer> getProductsInCart();

    void checkout();

    BigDecimal getTotalPrice();

    int count();
}
