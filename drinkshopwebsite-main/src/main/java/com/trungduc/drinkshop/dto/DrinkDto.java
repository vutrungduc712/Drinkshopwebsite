package com.trungduc.drinkshop.dto;

import com.trungduc.drinkshop.entity.Drink;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link Drink}
 */
@Data
@Value
public class DrinkDto implements Serializable {
    String title;
    Double totalRevenue;

}