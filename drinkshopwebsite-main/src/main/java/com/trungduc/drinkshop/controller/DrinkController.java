package com.trungduc.drinkshop.controller;

import com.trungduc.drinkshop.entity.Drink;
import com.trungduc.drinkshop.service.DrinkService;
import com.trungduc.drinkshop.service.RecommendationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class DrinkController {

    DrinkService drinkService;
    RecommendationService recommendationService;

    @GetMapping("/sort-drinks")
    public ModelAndView sortDrinks(@RequestParam("sortBy") String sortBy,
                                  @RequestParam("categoryId") Long categoryId,
                                  Model model){
        List<Drink> drinkList;
        if(categoryId != null){
            drinkList = drinkService.getAllDrinksByCategoryId(categoryId);
        }
        else {
            drinkList = drinkService.findAll();
        }

        List<Drink> sortedDrinkList;

        switch (sortBy){
            case "price-low-to-high":
                sortedDrinkList = drinkList.stream()
                        .sorted(Comparator.comparing(Drink::getSalePrice))
                        .collect(Collectors.toList());
                break;
            case "price-high-to-low":
                sortedDrinkList = drinkList.stream()
                        .sorted(Comparator.comparing(Drink::getSalePrice).reversed())
                        .collect(Collectors.toList());
                break;
            case "newest":
                sortedDrinkList = drinkList.stream()
                        .sorted(Comparator.comparing(Drink::getPublishedDate))
                        .collect(Collectors.toList());
                break;
            case "oldest":
                sortedDrinkList = drinkList.stream()
                        .sorted(Comparator.comparing(Drink::getPublishedDate).reversed())
                        .collect(Collectors.toList());
                break;
            default:
                sortedDrinkList = drinkList;
        }
        model.addAttribute("drinkList", sortedDrinkList);
        return new ModelAndView("fragments/drinkListFragment :: drinkList", model.asMap());
    }

}
