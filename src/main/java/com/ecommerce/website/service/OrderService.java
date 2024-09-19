package com.ecommerce.website.service;

import com.ecommerce.website.model.base.Order;

import java.util.List;

public interface OrderService {
    List<Order> getOrdersByUserId(Long userId);

    Order getOrderById(Long orderId);
}
