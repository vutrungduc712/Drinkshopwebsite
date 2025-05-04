package com.trungduc.drinkshop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddToCartRequest {
    private Long productId;
    private int quantity;
}
