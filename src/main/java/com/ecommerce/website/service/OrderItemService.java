package com.ecommerce.website.service;

import com.ecommerce.website.model.base.OrderItem;

import java.util.List;

public interface OrderItemService {
    List<OrderItem> findByOrderId(Long orderId);
}
