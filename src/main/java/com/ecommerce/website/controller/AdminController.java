package com.ecommerce.website.controller;

import com.ecommerce.website.dao.base.CategoryRepository;
import com.ecommerce.website.dao.base.ProductRepository;
import com.ecommerce.website.dao.base.UserRepository;
import com.ecommerce.website.model.base.Category;
import com.ecommerce.website.model.base.Product;
import com.ecommerce.website.model.base.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Autowired
    public AdminController(ProductRepository productRepository, CategoryRepository categoryRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String adminDashboard(Model model) {
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("categories", categoryRepository.findAll());
        return "admin/dashboard";
    }

    @GetMapping("/addUser")
    public String addUserForm(Model model) {
        model.addAttribute("user", new User());  // Adds an empty User object to the form
        return "admin/addUser";  // Returns the user creation form
    }

    @PostMapping("/addUser")
    public String addUserSubmit(@ModelAttribute User user) {
        userRepository.save(user);  // Save the submitted user to the database
        return "redirect:/admin";  // Redirect back to admin dashboard
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