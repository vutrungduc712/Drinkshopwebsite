package com.trungduc.drinkshop.dto.chat;

import com.trungduc.drinkshop.entity.Drink;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatbotResponse {
    private String message;
    private List<Drink> recommendedDrinks;
}
