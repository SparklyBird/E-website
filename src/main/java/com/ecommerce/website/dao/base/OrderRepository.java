package com.ecommerce.website.dao.base;

import com.ecommerce.website.model.base.Customer;
import com.ecommerce.website.model.base.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}