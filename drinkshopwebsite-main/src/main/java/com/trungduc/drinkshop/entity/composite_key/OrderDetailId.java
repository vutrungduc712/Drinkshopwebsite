package com.trungduc.drinkshop.entity.composite_key;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailId implements Serializable {

    private Long order;
    private Long drink;

    // Constructors, equals, hashcode methods, and other necessary logic
}
