package com.ecommerce.website.service.impl;

import com.ecommerce.website.dao.base.CartItemRepository;
import com.ecommerce.website.dao.base.CartRepository;
import com.ecommerce.website.model.base.Cart;
import com.ecommerce.website.model.base.CartItem;
import com.ecommerce.website.model.base.Product;
import com.ecommerce.website.model.user.User;
import com.ecommerce.website.service.ShoppingCartService;
import com.ecommerce.website.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(transactionManager = "baseTransactionManager")
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserService userService;
    private Map<Product, Integer> products = new HashMap<>();

    @Autowired
    public ShoppingCartServiceImpl(CartRepository cartRepository, CartItemRepository cartItemRepository, UserService userService) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userService = userService;
    }

    @EventListener
    public void initializeCart(InteractiveAuthenticationSuccessEvent event) {
        init();
    }

    private void init() {
        User user = getCurrentUser();
        if (user != null) {
            Cart cart = cartRepository.findByUserId(user.getId()).orElseGet(() -> {
                Cart newCart = new Cart();
                newCart.setCartItem(new ArrayList<>());
                newCart.setUserId(user.getId());
                return newCart;
            });

            if (!this.products.isEmpty()) {
                if (cart.getCartItem().isEmpty()) {
                    addingProductsToCart(cart);
                } else {
                    addingProductsToExistingCart(cart, true);
                }
            }
            if (!cart.getCartItem().isEmpty()) {
                this.products = cart.getCartItem().stream()
                        .collect(Collectors.toMap(CartItem::getProduct, CartItem::getQuantity));
            }
        }
    }

    @Override
    public void addProduct(Product product) {
        if (products.containsKey(product)) {
            products.replace(product, products.get(product) + 1);
        } else {
            products.put(product, 1);
        }
        saveCart();
    }

    @Override
    public void removeProduct(Product product) {
        products.remove(product);
        removeCart(product);
    }

    @Override
    public void decreaseProduct(Product product) {
        if (products.containsKey(product)) {
            if (products.get(product) > 1) {
                products.replace(product, products.get(product) - 1);
                saveCart();
            } else {
                products.remove(product);
                removeCart(product);
            }
        }
    }

    @Override
    public Map<Product, Integer> getProductsInCart() {
        return Collections.unmodifiableMap(products);
    }

    @Override
    public void checkout(User user) {
        this.products.clear();
        if (user != null) {
            Cart cart = getOrCreateCart(user);
            cartRepository.delete(cart);
        }
    }

    @Override
    public BigDecimal getTotalPrice() {
        return products.entrySet().stream()
                .map(entry -> entry.getKey().getUnitPrice().multiply(BigDecimal.valueOf(entry.getValue())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    @Override
    public int count() {
        return products.values().stream().mapToInt(Integer::intValue).sum();
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            return userService.findByUsername(userDetails.getUsername());
        }
        return null;
    }

    private Cart getOrCreateCart(User user) {
        return cartRepository.findByUserId(user.getId()).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setCartItem(new ArrayList<>());
            newCart.setUserId(user.getId());
            return newCart;
        });
    }

    private void addingProductsToCart(Cart cart) {
        products.forEach((product, quantity) -> {
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setCart(cart);
            cart.getCartItem().add(cartItem);
        });
        cartRepository.save(cart);
    }

    private void addingProductsToExistingCart(Cart cart, Boolean init) {
        Map<Product, CartItem> existingCartItems = cart.getCartItem().stream()
                .collect(Collectors.toMap(CartItem::getProduct, cartItem -> cartItem));

        products.forEach((product, quantity) -> {
            CartItem cartItem = existingCartItems.get(product);
            if (cartItem != null) {
                if (init)
                    cartItem.setQuantity(quantity + cartItem.getQuantity());
                else
                    cartItem.setQuantity(quantity);
            } else {
                cartItem = new CartItem();
                cartItem.setProduct(product);
                cartItem.setQuantity(quantity);
                cartItem.setCart(cart);
                cart.getCartItem().add(cartItem);
            }
        });

        this.products.clear();
        this.products = cart.getCartItem().stream()
                .collect(Collectors.toMap(CartItem::getProduct, CartItem::getQuantity));
        cartRepository.save(cart);
    }

    private void saveCart() {
        User user = getCurrentUser();
        if (user == null) {
            return;
        }
        Cart cart = getOrCreateCart(user);
        if (cart.getCartItem().isEmpty()) {
            addingProductsToCart(cart);
        } else {
            addingProductsToExistingCart(cart, false);
        }
    }

    private void removeCart(Product productRemoved) {
        User user = getCurrentUser();
        if (user == null) {
            return;
        }
        Cart cart = cartRepository.findByUserId(user.getId()).orElse(null);
        if (cart == null || cart.getCartItem().isEmpty()) {
            return;
        }

        CartItem cartItemToRemove = cart.getCartItem().stream()
                .filter(cartItem -> cartItem.getProduct().equals(productRemoved))
                .findFirst()
                .orElse(null);

        if (cartItemToRemove != null) {
            cart.getCartItem().remove(cartItemToRemove);
            cartItemRepository.delete(cartItemToRemove);
            System.out.println("deleting cartitem");
            cartRepository.save(cart);
        }
    }
}
