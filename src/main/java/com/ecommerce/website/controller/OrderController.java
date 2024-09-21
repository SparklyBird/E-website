package com.ecommerce.website.controller;

import com.ecommerce.website.dao.base.CategoryRepository;
import com.ecommerce.website.model.base.Order;
import com.ecommerce.website.model.user.User;
import com.ecommerce.website.service.OrderItemService;
import com.ecommerce.website.service.OrderService;
import com.ecommerce.website.service.ShoppingCartService;
import com.ecommerce.website.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static java.util.Optional.ofNullable;

@Controller
@RequestMapping("/order")
public class OrderController {
    private final ShoppingCartService shoppingCartService;
    private final OrderService orderService;
    private final UserService userService;
    private final CategoryRepository categoryRepository;

    @Autowired
    public OrderController(ShoppingCartService shoppingCartService, OrderService orderService, UserService userService, CategoryRepository categoryRepository) {
        this.shoppingCartService = shoppingCartService;
        this.orderService = orderService;
        this.userService = userService;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public String order(@AuthenticationPrincipal UserDetails userDetails, Model theModel) {
        User user = userService.findByUsername(ofNullable(userDetails.getUsername()).orElseThrow(()
                -> new RuntimeException("User not found")));

        List<Order> orders = orderService.getOrdersByUserId(user.getId());

        theModel.addAttribute("orders", orders);
        theModel.addAttribute("cartItemCount", shoppingCartService.count());
        theModel.addAttribute("categories", categoryRepository.findAll());
        return "order/order";
    }

    @GetMapping("/{orderId}")
    public String orderItem(@PathVariable int orderId,
                            @AuthenticationPrincipal UserDetails userDetails, Model theModel) {
        User user = null;
        if (userDetails != null)
            user = userService.findByUsername(userDetails.getUsername());

        Order order = orderService.getOrderById((long) orderId);
        if (user.getId() != order.getUserId()) {
            return "redirect:/access-denied";
        }
        theModel.addAttribute("totalPrice", order.getTotalPrice());
        theModel.addAttribute("orderItems", order.getOrderItems());
        theModel.addAttribute("cartItemCount", shoppingCartService.count());
        theModel.addAttribute("categories", categoryRepository.findAll());
        return "order/order-item";
    }
}
