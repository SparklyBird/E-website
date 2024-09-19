package com.ecommerce.website.controller;

import com.ecommerce.website.dto.PaymentInfo;
import com.ecommerce.website.dto.Purchase;
import com.ecommerce.website.dto.PurchaseResponse;
import com.ecommerce.website.model.user.User;
import com.ecommerce.website.service.PaymentService;
import com.ecommerce.website.service.UserService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment/secure")
public class PaymentRestController {
    private final UserService userService;
    private final PaymentService paymentService;

    @Autowired
    public PaymentRestController(UserService userService, PaymentService paymentService) {
        this.userService = userService;
        this.paymentService = paymentService;
    }

    @PostMapping("/purchase")
    public PurchaseResponse placeOrder(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Purchase purchase) {
        User user = null;
        if (userDetails != null)
            user = userService.findByUsername(userDetails.getUsername());

        return paymentService.placeOrder(purchase, user);
    }

    @PostMapping("/payment-intent")
    public ResponseEntity<String> createPaymentIntent(@RequestBody PaymentInfo paymentInfo) throws StripeException {

        PaymentIntent paymentIntent = paymentService.createPaymentIntent(paymentInfo);

        String paymentStr = paymentIntent.toJson();

        return new ResponseEntity<>(paymentStr, HttpStatus.OK);
    }
}
