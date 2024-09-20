package com.ecommerce.website.controller;

import com.ecommerce.website.dao.base.CategoryRepository;
import com.ecommerce.website.model.base.Product;
import com.ecommerce.website.service.CategoryService;
import com.ecommerce.website.service.ProductService;
import com.ecommerce.website.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final CategoryRepository categoryRepository;

    @Autowired
    public ProductController(ProductService productService, CategoryService categoryService, ShoppingCartService shoppingCartService, CategoryRepository categoryRepository) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.shoppingCartService = shoppingCartService;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/category/{id}")
    public String getProductsByCategory(@PathVariable Long id,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "24") int size,
                                        @RequestParam(required = false) String sortField,
                                        @RequestParam(required = false) String sortDirection,
                                        Model model) {

        Pageable pageable;

        if (sortField != null && sortDirection != null) {
            Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortField);
            pageable = PageRequest.of(page, size, sort);
        } else {
            pageable = PageRequest.of(page, size);
        }

        Page<Product> productPage = productService.getProductsByCategory(id, pageable);

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("categoryId", id);
        model.addAttribute("sortField", sortField);  // Will be null if no sorting applied
        model.addAttribute("sortDirection", sortDirection);  // Will be null if no sorting applied

        String categoryName = categoryService.getCategoryNameById(id);
        model.addAttribute("categoryName", categoryName);

        model.addAttribute("cartItemCount", shoppingCartService.count());
        model.addAttribute("categories", categoryRepository.findAll());
        return "product/productList";
    }

//    @GetMapping("/suggest")
//    public ResponseEntity<List<String>> getSuggestions(@RequestParam("query") String query) {
//        List<String> suggestions = productService.getSuggestions(query);
//        return ResponseEntity.ok(suggestions);
//    }

    @GetMapping("/products/suggestions")
    @ResponseBody
    public List<String> getSuggestions(@RequestParam("query") String query) {
        return productService.getSuggestions(query);
    }

    @GetMapping("/search")
    public String searchProducts(@RequestParam("query") String query,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "24") int size,
                                 Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productService.searchProducts(query, pageable);

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("query", query);
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("cartItemCount", shoppingCartService.count());
        return "product/searchResults";
    }

    @GetMapping("/{id}")
    public String getProductDetails(@PathVariable Long id, Model model) {
        Product product = productService.getProductByIdWithAttributes(id);
        model.addAttribute("product", product);
        model.addAttribute("cartItemCount", shoppingCartService.count());
        model.addAttribute("categories", categoryRepository.findAll());
        return "product/productDetails";
    }
}
