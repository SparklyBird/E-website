package com.ecommerce.website.model.base;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "order_item")
@Getter
@Setter
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "image_url")
    private String imageUrl;
    @Column(name = "unit_price")
    private BigDecimal unitPrice;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "product_id")
    private Long productId;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    public OrderItem(String imageUrl, BigDecimal unitPrice, int quantity, Long productId) {
        this.imageUrl = imageUrl;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.productId = productId;
    }

}
