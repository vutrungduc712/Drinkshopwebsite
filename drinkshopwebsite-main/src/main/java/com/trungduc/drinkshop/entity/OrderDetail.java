package com.trungduc.drinkshop.entity;

import com.trungduc.drinkshop.entity.composite_key.OrderDetailId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@IdClass(OrderDetailId.class) // Thêm định nghĩa IdClass
@Table(name = "order_details")
public class OrderDetail implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Id
    @ManyToOne
    @JoinColumn(name = "drink_id")
    private Drink drink;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "price")
    private Double price;


}
