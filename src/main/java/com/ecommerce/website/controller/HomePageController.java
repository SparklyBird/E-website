package com.ecommerce.website.controller;

import com.ecommerce.website.service.ProductService;
import com.ecommerce.website.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomePageController {
    private final ProductService productService;
    private final ShoppingCartService shoppingCartService;

    @Autowired
    public HomePageController(ProductService productService, ShoppingCartService shoppingCartService) {
        this.productService = productService;
        this.shoppingCartService = shoppingCartService;
    }

    @GetMapping("/")
    public String homePage(Model theModel) {

        theModel.addAttribute("cartItemCount", shoppingCartService.count());
        theModel.addAttribute("randomProducts", productService.getRandomProducts());
        return "homePage";
    }
}
