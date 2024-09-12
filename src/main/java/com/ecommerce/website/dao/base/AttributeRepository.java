package com.ecommerce.website.dao.base;

import com.ecommerce.website.model.base.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttributeRepository extends JpaRepository<Attribute, Long> {
}