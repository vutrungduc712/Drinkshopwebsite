package com.trungduc.drinkshop.service;

import com.trungduc.drinkshop.entity.Drink;

import java.util.List;

public interface RecommendationService {

    List<Drink> getRecommendationsForUser(Long userId, int limit);

    List<Drink> getSimilarDrinks(Long drinkId, int limit);

}
