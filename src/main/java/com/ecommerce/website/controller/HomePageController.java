package com.ecommerce.website.controller;

import com.ecommerce.website.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomePageController {
    private final ProductService productService;

    @Autowired
    public HomePageController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    public String homePage(Model theModel) {
        theModel.addAttribute("randomProducts", productService.getRandomProducts());
        return "homePage";
    }
}
