package com.ecommerce.website.model.base;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sku;
    private String name;
    private String description;
    @Column(name = "image_url")
    private String imageUrl;
    private boolean active;
    @Column(name = "units_in_stock")
    private int unitsInStock;
    @Column(name = "unit_price")
    private double unitPrice;

    @ManyToOne
    private Category category;

    @OneToMany(mappedBy = "product")
    private List<ProductAttributeValue> attributeValues;
}