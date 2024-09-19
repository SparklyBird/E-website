package com.ecommerce.website.service;

import com.ecommerce.website.dto.PaymentInfo;
import com.ecommerce.website.dto.Purchase;
import com.ecommerce.website.dto.PurchaseResponse;
import com.ecommerce.website.model.user.User;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

public interface PaymentService {
    PurchaseResponse placeOrder(Purchase purchase, User user);

    PaymentIntent createPaymentIntent(PaymentInfo paymentInfo) throws StripeException;
}
