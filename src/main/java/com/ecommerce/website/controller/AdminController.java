package com.ecommerce.website.controller;

import com.ecommerce.website.model.*;
import com.ecommerce.website.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private  ProductRepository productRepository;
    private  CategoryRepository categoryRepository;

    @Autowired
    public AdminController(ProductRepository productRepository,
                           CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public String adminDashboard(Model model) {
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("categories", categoryRepository.findAll());
        return "admin/dashboard";
    }

    @GetMapping("/addProduct")
    public String addProductForm(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());  // Fetches all categories for the product form
        model.addAttribute("product", new Product());  // Adds an empty Product to the form
        return "admin/addProduct";
    }

    @PostMapping("/addProduct")
    public String addProductSubmit(@ModelAttribute Product product) {
        productRepository.save(product);  // Save the submitted product to the database
        return "redirect:/admin";
    }

    @GetMapping("/addCategory")
    public String addCategoryForm(Model model) {
        model.addAttribute("category", new Category());  // Adds an empty Category to the form
        return "admin/addCategory";
    }

    @PostMapping("/addCategory")
    public String addCategorySubmit(@ModelAttribute Category category) {
        categoryRepository.save(category);  // Save the submitted category to the database
        return "redirect:/admin";
    }
}