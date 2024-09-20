package com.ecommerce.website.controller;

import com.ecommerce.website.dao.base.CategoryRepository;
import com.ecommerce.website.model.base.CartItem;
import com.ecommerce.website.model.base.Product;
import com.ecommerce.website.service.ProductService;
import com.ecommerce.website.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;
    private final ProductService productService;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ShoppingCartController(ShoppingCartService shoppingCartService, ProductService productService, CategoryRepository categoryRepository) {
        this.shoppingCartService = shoppingCartService;
        this.productService = productService;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public String shoppingCart(Model theModel) {
        theModel.addAttribute("categories", categoryRepository.findAll());

        theModel.addAttribute("cartItemCount", shoppingCartService.count());

        Map<Product, Integer> productsInCart = shoppingCartService.getProductsInCart();
        List<CartItem> cartItemList = productsInCart.entrySet().stream()
                .map(entry -> {
                    CartItem cartItem = new CartItem();
                    cartItem.setProduct(entry.getKey());
                    cartItem.setQuantity(entry.getValue());
                    return cartItem;
                })
                .collect(Collectors.toList());

        theModel.addAttribute("cartItemList", cartItemList);

        theModel.addAttribute("totalPrice", shoppingCartService.getTotalPrice());
        return "shoppingCart/shoppingCart";
    }

    @PostMapping("/addProductToCart/{productId}")
    @ResponseBody
    public Map<String, Object> addProductToCart(@PathVariable("productId") Long productId) {
        shoppingCartService.addProduct(productService.findById(productId));
        Map<String, Object> response = new HashMap<>();
        response.put("cartItemCount", shoppingCartService.count());
        return response;
    }

    @PostMapping("/addProduct/{productId}")
    @ResponseBody
    public Map<String, Object> addProduct(@PathVariable("productId") Long productId) {
        shoppingCartService.addProduct(productService.findById(productId));
        return getCartUpdateResponse();
    }

    @PostMapping("/removeProduct/{productId}")
    @ResponseBody
    public Map<String, Object> removeProduct(@PathVariable("productId") Long productId) {
        shoppingCartService.removeProduct(productService.findById(productId));
        return getCartUpdateResponse();
    }

    @PostMapping("/decreaseProduct/{productId}")
    @ResponseBody
    public Map<String, Object> decreaseProduct(@PathVariable("productId") Long productId) {
        shoppingCartService.decreaseProduct(productService.findById(productId));
        return getCartUpdateResponse();
    }

    private Map<String, Object> getCartUpdateResponse() {
        Map<String, Object> response = new HashMap<>();
        System.out.println(shoppingCartService.count());
        response.put("cartItemCount", shoppingCartService.count());
        response.put("totalPrice", shoppingCartService.getTotalPrice());
        response.put("cartItems", shoppingCartService.getProductsInCart().entrySet().stream()
                .map(entry -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("id", entry.getKey().getId());
                    item.put("name", entry.getKey().getName());
                    item.put("imageUrl", entry.getKey().getImageUrl());
                    item.put("price", entry.getKey().getUnitPrice());
                    item.put("quantity", entry.getValue());
                    item.put("total", entry.getKey().getUnitPrice().multiply(BigDecimal.valueOf(entry.getValue())));
                    item.put("unitsInStock", entry.getKey().getUnitsInStock());
                    return item;
                })
                .collect(Collectors.toList()));
        return response;
    }

}
