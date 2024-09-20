package com.ecommerce.website.controller;

import com.ecommerce.website.dao.base.CategoryRepository;
import com.ecommerce.website.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    private final ShoppingCartService shoppingCartService;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ErrorController(ShoppingCartService shoppingCartService, CategoryRepository categoryRepository) {
        this.shoppingCartService = shoppingCartService;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/access-denied")
    public String accessDenied(Model theModel) {
        theModel.addAttribute("cartItemCount", shoppingCartService.count());
        theModel.addAttribute("categories", categoryRepository.findAll());
        return "access-denied";
    }
}
