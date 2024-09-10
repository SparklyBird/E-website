package com.ecommerce.website.model;


import javax.persistence.*;
import java.math.BigDecimal;
import lombok.*;

@Getter
@Setter
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private BigDecimal price;

    @ManyToOne
    private Category category;
}
