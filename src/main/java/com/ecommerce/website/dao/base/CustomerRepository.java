package com.ecommerce.website.dao.base;

import com.ecommerce.website.model.base.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}