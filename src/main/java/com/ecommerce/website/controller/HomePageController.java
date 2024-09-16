package com.ecommerce.website.controller;

import com.ecommerce.website.service.ProductService;
import com.ecommerce.website.service.ShoppingCartService;
import jakarta.servlet.http.HttpServletRequest;
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
    public String homePage(HttpServletRequest request, Model theModel) {
        String currentUrl = request.getRequestURI();
        if (request.getQueryString() != null) {
            currentUrl += "?" + request.getQueryString();
        }
        theModel.addAttribute("currentUrl", currentUrl);

        theModel.addAttribute("cartItemCount", shoppingCartService.count());
        theModel.addAttribute("randomProducts", productService.getRandomProducts());
        return "homePage";
    }
}
