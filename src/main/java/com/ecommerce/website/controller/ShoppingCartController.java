package com.ecommerce.website.controller;

import com.ecommerce.website.model.base.CartItem;
import com.ecommerce.website.model.base.Product;
import com.ecommerce.website.service.ProductService;
import com.ecommerce.website.service.ShoppingCartService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;
    private final ProductService productService;

    @Autowired
    public ShoppingCartController(ShoppingCartService shoppingCartService, ProductService productService) {
        this.shoppingCartService = shoppingCartService;
        this.productService = productService;
    }

    @GetMapping
    public String shoppingCart(HttpServletRequest request, Model theModel) {
        theModel.addAttribute("cartItemCount", shoppingCartService.count());

        String currentUrl = request.getRequestURI();
        if (request.getQueryString() != null) {
            currentUrl += "?" + request.getQueryString();
        }
        theModel.addAttribute("currentUrl", currentUrl);

        Map<Product, Integer> productsInCart = shoppingCartService.getProductsInCart();
        List<CartItem> cartItemList = productsInCart.entrySet().stream()
                .map(entry -> {
                    CartItem cartItem = new CartItem();
                    cartItem.setProduct(entry.getKey());
                    cartItem.setQuantity(entry.getValue());
                    return cartItem;
                })
                .collect(Collectors.toList());

        theModel.addAttribute("cartItemList", cartItemList);

        theModel.addAttribute("totalPrice", shoppingCartService.getTotalPrice());
        return "shoppingCart/shoppingCart";
    }

    @GetMapping("/addProduct/{productId}")
    public String addProductToCart(@PathVariable("productId") Long productId,
                                   @RequestParam("redirectUrl") String redirectUrl) {
        shoppingCartService.addProduct(productService.findById(productId));
        return "redirect:" + redirectUrl;
    }

    @GetMapping("/removeProduct/{productId}")
    public String removeProductFromCart(@PathVariable("productId") Long productId,
                                        @RequestParam("redirectUrl") String redirectUrl) {
        shoppingCartService.removeProduct(productService.findById(productId));
        return "redirect:" + redirectUrl;
    }

    @GetMapping("/decreaseProduct/{productId}")
    public String decreaseroductFromCart(@PathVariable("productId") Long productId,
                                         @RequestParam("redirectUrl") String redirectUrl) {
        shoppingCartService.decreaseProduct(productService.findById(productId));
        return "redirect:" + redirectUrl;
    }

}
