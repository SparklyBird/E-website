package com.ecommerce.website.dao.base;

import com.ecommerce.website.model.base.ProductAttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductAttributeValueRepository extends JpaRepository<ProductAttributeValue, Long> {
}
