package com.trungduc.drinkshop.controller;

import com.trungduc.drinkshop.service.DrinkService;
import com.trungduc.drinkshop.controller.common.BaseController;
import com.trungduc.drinkshop.dto.UserSearchDTO;
import com.trungduc.drinkshop.entity.Drink;
import com.trungduc.drinkshop.entity.Category;
import com.trungduc.drinkshop.service.CategoryService;
import com.trungduc.drinkshop.service.RecommendationService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@Controller
@RequestMapping("/shop")
public class ShopController extends BaseController {

    private CategoryService categoryService;
    private DrinkService drinkService;
    private RecommendationService recommendationService;

    @GetMapping
    public String getShopPage(
            @ModelAttribute("searchModel") UserSearchDTO searchModel,
            @RequestParam(name = "categoryId", required = false) Long categoryId,
            @RequestParam(name = "page", defaultValue = "1") int page,
            Model model) {

        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);

        Pageable pageable = PageRequest.of(page - 1, 6);

        Page<Drink> searchResult;

        if (searchModel.isEmpty()) {
            searchResult = drinkService.getAllDrinksForUsers(pageable);
        } else {
            searchResult = drinkService.searchDrinksUser(searchModel, pageable);
        }

        model.addAttribute("drinks", searchResult);


        model.addAttribute("totalPages", searchResult.getTotalPages());
        model.addAttribute("currentPage", searchResult.getNumber());
        model.addAttribute("sortBy", searchModel.getSortBy());
        model.addAttribute("sortBy", searchModel.getCategoryId());
        model.addAttribute("categoryId", searchModel.getCategoryId());
        model.addAttribute("amountGap", searchModel.getAmountGap());

        if(searchModel.getCategoryId() != null){
            Category category = categoryService.getCategoryById(searchModel.getCategoryId());
            model.addAttribute("categoryName", category.getName() + "'s Products");
        }
        else {
            model.addAttribute("categoryName", "All Product");
        }

        return "user/shop";
    }

    @GetMapping("/product/{product_id}")
    public String viewProductDetail(@PathVariable Long product_id, Model model) {
        Drink product = drinkService.getDrinkById(product_id);
        model.addAttribute("product", product);
        List<Drink> similarDrinks = recommendationService.getSimilarDrinks(product_id, 4);
        model.addAttribute("similarDrinks", similarDrinks);
        return "user/product_details";
    }
}
