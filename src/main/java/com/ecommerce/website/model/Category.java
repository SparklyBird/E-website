package com.ecommerce.website.model;

import javax.persistence.*;
import java.util.Set;
import lombok.*;

@Getter
@Setter
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "category")
    private Set<Product> products;
}