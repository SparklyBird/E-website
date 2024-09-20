package com.ecommerce.website.controller;

import com.ecommerce.website.model.base.Category;
import com.ecommerce.website.model.base.Product;
import com.ecommerce.website.model.user.User;
import com.ecommerce.website.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
    private final AdminService adminService;

    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping
    public String adminDashboard() {
        return "admin/dashboard";
    }

    @GetMapping("/productTable")
    public String getProductTable(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = (search != null && !search.isEmpty()) ?
                adminService.findProductByNameContaining(search, pageable) :
                adminService.findProductAll(pageable);

        model.addAttribute("productPage", productPage);
        model.addAttribute("search", search);

        return "admin/products-table :: productTable";
    }

    @GetMapping("/categoryTable")
    public String getCategoryTable(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Category> categoryPage = (search != null && !search.isEmpty()) ?
                adminService.findCategoriesByNameContaining(search, pageable) :
                adminService.findCategoriesAll(pageable);

        model.addAttribute("categoryPage", categoryPage);
        model.addAttribute("search", search);

        return "admin/categories-table :: categoryTable";
    }

    @GetMapping("/userTable")
    public String getUserTable(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage = (search != null && !search.isEmpty()) ?
                adminService.findUserByLoginContaining(search, pageable) :
                adminService.findUserAll(pageable);

        model.addAttribute("userPage", userPage);
        model.addAttribute("search", search);

        return "admin/users-table :: userTable";
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
    public String addProductSubmit(@ModelAttribute Product product, @RequestParam("image") MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            if (!file.isEmpty()) {
                Path uploadDir = Paths.get(uploadPath);
                if (!Files.exists(uploadDir)) {
                    Files.createDirectories(uploadDir);
                }

                String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                Path filePath = uploadDir.resolve(fileName);
                Files.copy(file.getInputStream(), filePath);

                product.setImageUrl("/images/" + fileName);
            }
            product.setActive(true);
            product.setUnitsInStock(1);
            adminService.saveProduct(product);
            redirectAttributes.addFlashAttribute("message", "Product added successfully");
            return "redirect:/admin";

        } catch (CannotAcquireLockException e) {
            logger.error("Database lock error", e);
            redirectAttributes.addFlashAttribute("error", "Unable to add product. Please try again.");
            return "redirect:/admin/addProduct";

        } catch (IOException e) {
            logger.error("Error uploading product image", e);
            redirectAttributes.addFlashAttribute("error", "Error uploading image. Please try again.");
            return "redirect:/admin/addProduct";

        } catch (Exception e) {
            logger.error("Error adding product", e);
            redirectAttributes.addFlashAttribute("error", "An unexpected error occurred. Please try again.");
            return "redirect:/admin/addProduct";
        }
    }


    @GetMapping("/updateProduct")
    public String showUpdateProductForm(@RequestParam Long id, Model model) {
        Product product = adminService.findProductById(id);
        model.addAttribute("categories", adminService.findALLCategories());
        model.addAttribute("product", product);
        return "admin/updateProduct";
    }

    @PostMapping("/updateProduct")
    public String updateProductSubmit(@ModelAttribute Product product, @RequestParam("image") MultipartFile file) {
        try {
            Product existingProduct = adminService.findProductById(product.getId());

            if (!file.isEmpty()) {
                Path uploadDir = Paths.get(uploadPath);
                if (!Files.exists(uploadDir)) {
                    Files.createDirectories(uploadDir);
                }

                String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                Path filePath = uploadDir.resolve(fileName);
                Files.copy(file.getInputStream(), filePath);

                product.setImageUrl("/images/" + fileName);
            } else {
                // If no new file is uploaded, keep the existing image URL
                product.setImageUrl(existingProduct.getImageUrl());
            }
            product.setActive(existingProduct.isActive());
            product.setSku(existingProduct.getSku());
            product.setUnitsInStock(existingProduct.getUnitsInStock());

            adminService.saveProduct(product);
            return "redirect:/admin";
        } catch (IOException e) {
            logger.error("Error updating product image", e);
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

    @GetMapping("/updateCategory")
    public String showUpdateCategoryForm(@RequestParam Long id, Model model) {
        Category category = adminService.findCategoryById(id);
        model.addAttribute("category", category);
        return "admin/updateCategory";
    }

    @PostMapping("/updateCategory")
    public String updateCategorySubmit(@ModelAttribute Category category) {
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

    @GetMapping("/updateUser")
    public String showUpdateUserForm(@RequestParam Long id, Model model) {
        User user = adminService.findUserById(id);
        model.addAttribute("user", user);
        return "admin/updateUser";
    }

    @PostMapping("/updateUser")
    public String updateUserSubmit(@ModelAttribute User user) {
        try {
            adminService.saveUser(user);
            return "redirect:/admin";
        } catch (Exception e) {
            logger.error("Error updating user", e);
            return "error";
        }
    }

    @GetMapping("/deleteProduct")
    public String showDeleteProductForm(@RequestParam Long id, Model model) {
        Product product = adminService.findProductById(id);
        model.addAttribute("product", product);
        return "admin/deleteProduct";
    }

    @PostMapping("/deleteProduct")
    public String deleteProduct(@RequestParam Long id) {
        try {
            adminService.deleteProduct(id);
        } catch (Exception e) {
            logger.error("Error deleting product with ID " + id, e);
            return "error";
        }
        return "redirect:/admin";
    }

    @GetMapping("/deleteCategory")
    public String showDeleteCategoryForm(@RequestParam Long id, Model model) {
        Category category = adminService.findCategoryById(id);
        model.addAttribute("category", category);
        return "admin/deleteCategory";
    }

    @PostMapping("/deleteCategory")
    public String deleteCategory(@RequestParam Long id) {
        try {
            adminService.deleteCategory(id);
        } catch (Exception e) {
            logger.error("Error deleting category with ID " + id, e);
            return "error";
        }
        return "redirect:/admin";
    }

    @GetMapping("/deleteUser")
    public String showDeleteUserForm(@RequestParam Long id, Model model) {
        User user = adminService.findUserById(id);
        model.addAttribute("user", user);
        return "admin/deleteUser";
    }

    @PostMapping("/deleteUser")
    public String deleteUser(@RequestParam Long id) {
        try {
            adminService.deleteUser(id);
        } catch (Exception e) {
            logger.error("Error deleting user with ID " + id, e);
            return "error";
        }
        return "redirect:/admin";
    }
}