package com.ecommerce.website.controller;

import com.ecommerce.website.dto.CategoryDTO;
import com.ecommerce.website.model.base.Product;
import com.ecommerce.website.service.CategoryService;
import com.ecommerce.website.service.ProductService;
import com.ecommerce.website.service.ShoppingCartService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final ShoppingCartService shoppingCartService;

    @Autowired
    public ProductController(ProductService productService, CategoryService categoryService, ShoppingCartService shoppingCartService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.shoppingCartService = shoppingCartService;
    }

    @GetMapping("/category/{id}")
    public String getProductsByCategory(@PathVariable Long id,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "24") int size,
                                        HttpServletRequest request, Model model) {
        String currentUrl = request.getRequestURI();
        if (request.getQueryString() != null) {
            currentUrl += "?" + request.getQueryString();
        }
        model.addAttribute("currentUrl", currentUrl);

        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productService.getProductsByCategory(id, pageable);

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("categoryId", id);

        String categoryName = categoryService.getCategoryNameById(id);
        model.addAttribute("categoryName", categoryName);

        model.addAttribute("cartItemCount", shoppingCartService.count());
        return "product/productList";
    }

    @GetMapping("/{id}")
    public String getProductDetails(@PathVariable Long id, Model model) {
        Product product = productService.getProductByIdWithAttributes(id);
        model.addAttribute("product", product);
        return "product/productDetails";
    }

    @GetMapping("/categories")
    @ResponseBody
    public List<CategoryDTO> getAllCategories() {
        return categoryService.getAllCategories().stream()
                .map(category -> new CategoryDTO(category.getId(), category.getName())).toList();
    }
}
