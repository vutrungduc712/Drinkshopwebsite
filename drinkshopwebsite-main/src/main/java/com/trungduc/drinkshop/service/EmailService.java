package com.trungduc.drinkshop.service;

public interface EmailService {
    void sendSimpleEmail(String to, String subject, String text);
}
