package com.trungduc.drinkshop.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRequest {
    private String model;
    private List<ChatMessage> messages;
    private double temperature;

    public ChatRequest(String model, String prompt) {
        this.model = model;
        this.messages = new ArrayList<>();
        this.messages.add(new ChatMessage("system", "You are a helpful assistant for a drinkstore called 'Rooks Drinks'. Your job is to recommend drinks to customers based on their queries. Only recommend drinks that are likely to be in our inventory."));
        this.messages.add(new ChatMessage("user", prompt));
        this.temperature = 0.7;
    }
}
