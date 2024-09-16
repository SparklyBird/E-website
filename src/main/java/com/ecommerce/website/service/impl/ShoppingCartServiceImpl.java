package com.ecommerce.website.service.impl;

import com.ecommerce.website.dao.base.ProductRepository;
import com.ecommerce.website.model.base.Product;
import com.ecommerce.website.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(transactionManager = "baseTransactionManager")
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ProductRepository productRepository;

    private Map<Product, Integer> products = new HashMap<>();

    @Autowired
    public ShoppingCartServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void addProduct(Product product) {
        if (products.containsKey(product)) {
            products.replace(product, products.get(product) + 1);
        } else {
            products.put(product, 1);
        }
    }

    @Override
    public void removeProduct(Product product) {

    }

    @Override
    public Map<Product, Integer> getProductsInCart() {
        return null;
    }

    @Override
    public void checkout() {

    }

    @Override
    public BigDecimal getTotal() {
        return null;
    }

    @Override
    public int count() {
        return products.values().stream().mapToInt(Integer::intValue).sum();
    }
}
