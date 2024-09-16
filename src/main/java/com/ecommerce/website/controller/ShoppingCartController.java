package com.ecommerce.website.controller;

import com.ecommerce.website.service.ProductService;
import com.ecommerce.website.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;
    private final ProductService productService;

    @Autowired
    public ShoppingCartController(ShoppingCartService shoppingCartService, ProductService productService) {
        this.shoppingCartService = shoppingCartService;
        this.productService = productService;
    }

    @GetMapping("/addProduct/{productId}")
    public String addProductToCart(@PathVariable("productId") Long productId,
                                   @RequestParam("redirectUrl") String redirectUrl) {
        shoppingCartService.addProduct(productService.findById(productId));
        return "redirect:" + redirectUrl;
    }

}
