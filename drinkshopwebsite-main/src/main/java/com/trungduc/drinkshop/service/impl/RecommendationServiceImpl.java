package com.trungduc.drinkshop.service.impl;

import com.trungduc.drinkshop.entity.Drink;
import com.trungduc.drinkshop.entity.User;
import com.trungduc.drinkshop.repository.DrinkRepository;
import com.trungduc.drinkshop.repository.UserRepository;
import com.trungduc.drinkshop.service.RecommendationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class RecommendationServiceImpl implements RecommendationService {

    DrinkRepository drinkRepository;
    UserRepository userRepository;
    @Override
    public List<Drink> getRecommendationsForUser(Long userId, int limit) {
        // Get the user
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return Collections.emptyList();
        }

        // Get drinks the user has interacted with (favorites, purchases, etc.)
        Set<Drink> userDrinks = new HashSet<>();

        // Add favorite drinks
        if (user.getFavoriteDrinks() != null) {
            userDrinks.addAll(user.getFavoriteDrinks());
        }

        // If user hasn't interacted with any drinks, return popular drinks
        if (userDrinks.isEmpty()) {
            return drinkRepository.findTop4ByActiveFlagOrderByBuyCountDesc(true);
        }

        // Extract features from user's drinks to build a user profile
        Map<String, Integer> categoryFrequency = new HashMap<>();
        Map<String, Integer> authorFrequency = new HashMap<>();
        Map<String, Integer> publisherFrequency = new HashMap<>();

        for (Drink drink : userDrinks) {
            // Count category occurrences
            String categoryName = drink.getCategory() != null ? drink.getCategory().getName() : "Unknown";
            categoryFrequency.put(categoryName, categoryFrequency.getOrDefault(categoryName, 0) + 1);

            // Count author occurrences
            String author = drink.getAuthor() != null ? drink.getAuthor() : "Unknown";
            authorFrequency.put(author, authorFrequency.getOrDefault(author, 0) + 1);

            // Count publisher occurrences
            String publisher = drink.getPublisher() != null ? drink.getPublisher() : "Unknown";
            publisherFrequency.put(publisher, publisherFrequency.getOrDefault(publisher, 0) + 1);
        }

        // Get all active drinks
        List<Drink> allDrinks = drinkRepository.findAllByActiveFlag(true);

        // Filter out drinks the user already has
        List<Drink> candidateDrinks = allDrinks.stream()
                .filter(drink -> !userDrinks.contains(drink))
                .collect(Collectors.toList());

        // Calculate similarity scores for each candidate drink
        Map<Drink, Double> drinkScores = new HashMap<>();

        for (Drink candidateDrink : candidateDrinks) {
            double score = 0.0;

            String drinkCategory = candidateDrink.getCategory() != null ? candidateDrink.getCategory().getName() : "Unknown";
            if (categoryFrequency.containsKey(drinkCategory)) {
                score += 3.0 * categoryFrequency.get(drinkCategory);
            }

            String drinkAuthor = candidateDrink.getAuthor() != null ? candidateDrink.getAuthor() : "Unknown";
            if (authorFrequency.containsKey(drinkAuthor)) {
                score += 2.0 * authorFrequency.get(drinkAuthor);
            }

            // Publisher similarity (lowest weight)
            String drinkPublisher = candidateDrink.getPublisher() != null ? candidateDrink.getPublisher() : "Unknown";
            if (publisherFrequency.containsKey(drinkPublisher)) {
                score += 1.0 * publisherFrequency.get(drinkPublisher);
            }

            drinkScores.put(candidateDrink, score);
        }

        return drinkScores.entrySet().stream()
                .sorted(Map.Entry.<Drink, Double>comparingByValue().reversed())
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    @Override
    public List<Drink> getSimilarDrinks(Long drinkId, int limit) {
        Drink referenceDrink = drinkRepository.findById(drinkId).orElse(null);
        if (referenceDrink == null) {
            return Collections.emptyList();
        }

        List<Drink> allDrinks = drinkRepository.findAllByActiveFlag(true).stream()
                .filter(drink -> !drink.getId().equals(drinkId))
                .collect(Collectors.toList());

        Map<Drink, Double> similarityScores = new HashMap<>();

        for (Drink drink : allDrinks) {
            double score = 0.0;

            if (referenceDrink.getCategory() != null && drink.getCategory() != null &&
                    referenceDrink.getCategory().getId().equals(drink.getCategory().getId())) {
                score += 3.0;
            }

            if (referenceDrink.getAuthor() != null && drink.getAuthor() != null &&
                    referenceDrink.getAuthor().equals(drink.getAuthor())) {
                score += 2.0;
            }

            if (referenceDrink.getPublisher() != null && drink.getPublisher() != null &&
                    referenceDrink.getPublisher().equals(drink.getPublisher())) {
                score += 1.0;
            }

            if (referenceDrink.getSalePrice() != null && drink.getSalePrice() != null) {
                double priceDiff = Math.abs(referenceDrink.getSalePrice() - drink.getSalePrice());
                double maxPrice = Math.max(referenceDrink.getSalePrice(), drink.getSalePrice());
                if (priceDiff / maxPrice < 0.2) { // If price difference is less than 20%
                    score += 0.5;
                }
            }

            similarityScores.put(drink, score);
        }

        return similarityScores.entrySet().stream()
                .sorted(Map.Entry.<Drink, Double>comparingByValue().reversed())
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

}
