package com.ecommerce.website.controller;

import com.ecommerce.website.service.ProductService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class HomePageController {
    private static Logger logger = LogManager.getLogger(HomePageController.class);

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
