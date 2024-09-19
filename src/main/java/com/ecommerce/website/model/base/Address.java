package com.ecommerce.website.model.base;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "address")
@Getter
@Setter
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "street")
    @NotBlank(message = "*is required")
    private String street;

    @Column(name = "city")
    @NotBlank(message = "*is required")
    private String city;

    @Column(name = "state")
    @NotBlank(message = "*is required")
    private String state;

    @Column(name = "country")
    @NotBlank(message = "*is required")
    private String country;

    @Column(name = "zip_code")
    @NotBlank(message = "*is required")
    private String zipCode;

    @OneToOne
    @PrimaryKeyJoinColumn
    private Order order;

}
