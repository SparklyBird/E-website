package com.ecommerce.website.controller;

import com.ecommerce.website.dao.base.CategoryRepository;
import com.ecommerce.website.dao.base.ProductRepository;
import com.ecommerce.website.dao.user.UserRepository;
import com.ecommerce.website.service.AdminService;
import com.ecommerce.website.model.base.Category;
import com.ecommerce.website.model.base.Product;
import com.ecommerce.website.model.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final AdminService adminService;

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    public AdminController(ProductRepository productRepository, CategoryRepository categoryRepository, UserRepository userRepository, AdminService adminService) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.adminService = adminService;
    }

    @GetMapping
    public String adminDashboard(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = (search != null && !search.isEmpty()) ?
                productRepository.findByNameContaining(search, pageable) :
                productRepository.findAll(pageable);
        Page<Category> categoryPage = (search != null && !search.isEmpty()) ?
                categoryRepository.findByNameContaining(search, pageable) :
                categoryRepository.findAll(pageable);
        Page<User> userPage = (search != null && !search.isEmpty()) ?
                userRepository.findByLoginContaining(search, pageable) :
                userRepository.findAll(pageable);

        model.addAttribute("productPage", productPage);
        model.addAttribute("categoryPage", categoryPage);
        model.addAttribute("userPage", userPage);
        model.addAttribute("search", search);

        return "admin/dashboard";
    }

    @GetMapping("/addProduct")
    public String addProductForm(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("product", new Product());
        return "admin/addProduct";
    }

    @PostMapping("/addProduct")
    public String addProductSubmit(@ModelAttribute Product product, @RequestParam("image") MultipartFile file) {
        try {
            if (!file.isEmpty()) {
                // Handle file upload
                String fileName = file.getOriginalFilename();
                String filePath = "static/images/" + fileName;
                File targetFile = new File(filePath);
                file.transferTo(targetFile);
                product.setImageUrl(filePath);
            }

            productRepository.save(product);
            return "redirect:/admin";
        } catch (IOException e) {
            logger.error("Error uploading product image", e);
            return "error";
        }
    }

    @GetMapping("/addCategory")
    public String addCategoryForm(Model model) {
        model.addAttribute("category", new Category());
        return "admin/addCategory";
    }

    @PostMapping("/addCategory")
    public String addCategorySubmit(@ModelAttribute Category category) {
        categoryRepository.save(category);
        return "redirect:/admin";
    }

    @GetMapping("/addUser")
    public String addUserForm(Model model) {
        model.addAttribute("user", new User());
        return "admin/addUser";
    }

    @PostMapping("/addUser")
    public String addUserSubmit(@ModelAttribute User user) {
        try {
            userRepository.save(user);
            return "redirect:/admin";
        } catch (Exception e) {
            logger.error("Error adding user", e);
            return "error";
        }
    }

    @PostMapping("/deleteUser")
    public String deleteUser(@RequestParam Long id) {
        try {
            userRepository.deleteById(id);
        } catch (Exception e) {
            logger.error("Error deleting user with ID " + id, e);
        }
        return "redirect:/admin";
    }

    @PostMapping("/deleteProduct")
    public String deleteProduct(@RequestParam Long id) {
        try {
            productRepository.deleteById(id);
        } catch (Exception e) {
            logger.error("Error deleting product with ID " + id, e);
        }
        return "redirect:/admin";
    }

    @PostMapping("/deleteCategory")
    public String deleteCategory(@RequestParam Long id) {
        try {
            categoryRepository.deleteById(id);
        } catch (Exception e) {
            logger.error("Error deleting category with ID " + id, e);
        }
        return "redirect:/admin";
    }
}
