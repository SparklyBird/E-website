package com.ecommerce.website.service.impl;

import com.ecommerce.website.dao.base.CustomerRepository;
import com.ecommerce.website.dao.base.OrderRepository;
import com.ecommerce.website.dto.PaymentInfo;
import com.ecommerce.website.dto.Purchase;
import com.ecommerce.website.dto.PurchaseResponse;
import com.ecommerce.website.model.base.Customer;
import com.ecommerce.website.model.base.Order;
import com.ecommerce.website.model.base.OrderItem;
import com.ecommerce.website.model.user.User;
import com.ecommerce.website.service.PaymentService;
import com.ecommerce.website.service.ShoppingCartService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final ShoppingCartService shoppingCartService;

    @Autowired
    public PaymentServiceImpl(CustomerRepository customerRepository,
                              @Value("${stripe.key.secret}") String secretKey, OrderRepository orderRepository, ShoppingCartService shoppingCartService) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
        this.shoppingCartService = shoppingCartService;

        // initialize Stripe API with secret key
        Stripe.apiKey = secretKey;
    }

    @Override
    @Transactional(transactionManager = "baseTransactionManager")
    public PurchaseResponse placeOrder(Purchase purchase, User user) {
        // retrieve the order info from dto
        Order order = purchase.getOrder();

        // generate tracking number
        String orderTrackingNumber = generateOrderTrackingNumber();
        order.setOrderTrackingNumber(orderTrackingNumber);

        // populate order with orderItems
        Set<OrderItem> orderItems = purchase.getOrderItems();
        orderItems.forEach(item -> order.add(item));

        // populate order with shippingAddress
        order.setShippingAddress(purchase.getShippingAddress());

        order.setStatus("Purchased");

        if (user != null) {
            order.setUserId(user.getId());
            orderRepository.save(order);

            shoppingCartService.checkout(user);
        } else {
            // populate customer with order
            Customer customer = purchase.getCustomer();
            customer.add(order);

            customerRepository.save(customer);

            shoppingCartService.checkout(null);
        }

        return new PurchaseResponse(orderTrackingNumber);
    }

    @Override
    public PaymentIntent createPaymentIntent(PaymentInfo paymentInfo) throws StripeException {
        List<String> paymentMethodTypes = new ArrayList<>();
        paymentMethodTypes.add("card");

        Map<String, Object> params = new HashMap<>();
        params.put("amount", paymentInfo.getAmount());
        params.put("currency", paymentInfo.getCurrency());
        params.put("payment_method_types", paymentMethodTypes);
        params.put("description", "Ecommerce website shop purchase");

        return PaymentIntent.create(params);
    }

    private String generateOrderTrackingNumber() {
        return UUID.randomUUID().toString();
    }
}
