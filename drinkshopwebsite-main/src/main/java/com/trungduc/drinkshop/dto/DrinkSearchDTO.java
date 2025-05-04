package com.trungduc.drinkshop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DrinkSearchDTO {
    private Long categoryId;
    private String keyword;

}

