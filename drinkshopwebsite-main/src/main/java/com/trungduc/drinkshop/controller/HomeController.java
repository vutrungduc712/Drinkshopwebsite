package com.trungduc.drinkshop.controller;

import com.trungduc.drinkshop.controller.common.BaseController;
import com.trungduc.drinkshop.entity.Drink;
import com.trungduc.drinkshop.entity.User;
import com.trungduc.drinkshop.service.DrinkService;
import com.trungduc.drinkshop.service.CategoryService;
import com.trungduc.drinkshop.service.RecommendationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@AllArgsConstructor
@Controller

public class HomeController extends BaseController {

    private DrinkService drinkService;
    private CategoryService categoryService;
    private RecommendationService recommendationService;

    @GetMapping("/")
    String getUserHomePage(Model model) {

        List<Drink> top4BestSeller = drinkService.getTop4BestSeller();
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("top4BestSeller", top4BestSeller);
        List<Drink> newProducts = drinkService.findAllOrderByCreatedDate();
        model.addAttribute("newProducts", newProducts);

        User currentUser = super.getCurrentUser();
        if (currentUser != null) {
            List<Drink> recommendations = recommendationService.getRecommendationsForUser(currentUser.getId(), 8);
            model.addAttribute("recommendedDrinks", recommendations);
        }

        return "user/index";
    }


}
