package com.ecommerce.website.controller;

import com.ecommerce.website.dao.base.CategoryRepository;
import com.ecommerce.website.dto.Purchase;
import com.ecommerce.website.model.base.Customer;
import com.ecommerce.website.model.base.OrderItem;
import com.ecommerce.website.model.base.Product;
import com.ecommerce.website.model.user.User;
import com.ecommerce.website.model.user.UserProfile;
import com.ecommerce.website.service.ShoppingCartService;
import com.ecommerce.website.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {
    private final ShoppingCartService shoppingCartService;
    private final UserService userService;
    private final CategoryRepository categoryRepository;
    @Value("${stripe.key.public}")
    private String stripePublicKey;
    @Value("${shipping.price}")
    private String shippingPrice;

    @Autowired
    public CheckoutController(ShoppingCartService shoppingCartService, UserService userService, CategoryRepository categoryRepository) {
        this.shoppingCartService = shoppingCartService;
        this.userService = userService;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public String checkout(@AuthenticationPrincipal UserDetails userDetails, Model theModel) {
        Purchase purchase = new Purchase();

        if (userDetails != null) {
            User user = userService.findByUsername(userDetails.getUsername());
            if (user != null) {
                UserProfile profile = user.getProfile();
                if (profile != null) {
                    // Populate Purchase with customer details from profile
                    Customer customer = new Customer(
                            profile.getFirstName(),
                            profile.getLastName(),
                            profile.getEmail(),
                            profile.getPhoneNumber()
                    );
                    purchase = new Purchase(customer);
                }
            }
        }
        theModel.addAttribute("purchase", purchase);

        Map<Product, Integer> cart = shoppingCartService.getProductsInCart();
        List<OrderItem> orderItems = new ArrayList<>();
        cart.forEach((product, quantity) -> {
            OrderItem orderItem = new OrderItem(
                    product.getUnitPrice(),
                    quantity,
                    product
            );
            orderItems.add(orderItem); // Add OrderItem to Order
        });
        theModel.addAttribute("orderItems", orderItems);
        theModel.addAttribute("cart", cart);

        theModel.addAttribute("productCount", shoppingCartService.count());


        BigDecimal price = new BigDecimal(shippingPrice);
        theModel.addAttribute("totalPrice", shoppingCartService.getTotalPrice().add(price));
        theModel.addAttribute("shippingPrice", shippingPrice);
        theModel.addAttribute("stripePublicKey", stripePublicKey);
        theModel.addAttribute("categories", categoryRepository.findAll());
        return "/checkout/checkout";
    }
}
