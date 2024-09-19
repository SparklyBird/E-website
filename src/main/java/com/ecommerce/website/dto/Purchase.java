package com.ecommerce.website.dto;

import com.ecommerce.website.model.base.Address;
import com.ecommerce.website.model.base.Customer;
import com.ecommerce.website.model.base.Order;
import com.ecommerce.website.model.base.OrderItem;
import jakarta.validation.Valid;
import lombok.Data;

import java.util.Set;

@Data
public class Purchase {
    @Valid
    private Customer customer;
    @Valid
    private Address shippingAddress;
    private Order order;
    private Set<OrderItem> orderItems;

    public Purchase() {
    }

    public Purchase(Customer customer) {
        this.customer = customer;
    }
}
