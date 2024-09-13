package com.ecommerce.website.controller;

import com.ecommerce.website.model.base.Category;
import com.ecommerce.website.model.base.Product;
import com.ecommerce.website.model.user.User;
import com.ecommerce.website.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
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
                adminService.findProductByNameContaining(search, pageable) :
                adminService.findProductAll(pageable);
        Page<Category> categoryPage = (search != null && !search.isEmpty()) ?
                adminService.findCategoriesByNameContaining(search, pageable) :
                adminService.findCategoriesAll(pageable);
        Page<User> userPage = (search != null && !search.isEmpty()) ?
                adminService.findUserByLoginContaining(search, pageable) :
                adminService.findUserAll(pageable);

        model.addAttribute("productPage", productPage);
        model.addAttribute("categoryPage", categoryPage);
        model.addAttribute("userPage", userPage);
        model.addAttribute("search", search);

        return "admin/dashboard";
    }

    @PostMapping("/delete")
    public String deleteItem(@RequestParam String type, @RequestParam Long id) {
        try {
            switch (type.toLowerCase()) {
                case "product":
                    adminService.deleteProduct(id);
                    break;
                case "category":
                    adminService.deleteCategory(id);
                    break;
                case "user":
                    adminService.deleteUser(id);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid type: " + type);
            }
        } catch (Exception e) {
            logger.error("Error deleting " + type + " with ID " + id, e);
            return "error";
        }
        return "redirect:/admin";
    }


    @GetMapping("/addProduct")
    public String addProductForm(Model model) {
        model.addAttribute("categories", adminService.findALLCategories());
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
            adminService.saveProduct(product);
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
        adminService.saveCategory(category);
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
            adminService.saveUser(user);
            return "redirect:/admin";
        } catch (Exception e) {
            logger.error("Error adding user", e);
            return "error";
        }
    }

    @PostMapping("/deleteUser")
    public String deleteUser(@RequestParam Long id) {
        adminService.deleteUser(id);
        return "redirect:/admin";
    }

    @PostMapping("/deleteProduct")
    public String deleteProduct(@RequestParam Long id) {
        adminService.deleteProduct(id);
        return "redirect:/admin";
    }

    @PostMapping("/deleteCategory")
    public String deleteCategory(@RequestParam Long id) {
        adminService.deleteCategory(id);
        return "redirect:/admin";
    }
}
