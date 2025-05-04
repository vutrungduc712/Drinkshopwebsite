package com.trungduc.drinkshop.service;

import com.trungduc.drinkshop.dto.chat.ChatbotResponse;
import com.trungduc.drinkshop.entity.Drink;

import java.util.List;

public interface ChatbotService {
    ChatbotResponse processMessage(String message);

    List<Drink> searchDrinksByKeywords(List<String> keywords);
}
