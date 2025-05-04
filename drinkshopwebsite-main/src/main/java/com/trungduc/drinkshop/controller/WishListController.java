package com.trungduc.drinkshop.controller;

import com.trungduc.drinkshop.controller.common.BaseController;
import com.trungduc.drinkshop.entity.User;
import com.trungduc.drinkshop.service.DrinkService;
import com.trungduc.drinkshop.entity.Drink;
import com.trungduc.drinkshop.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Controller
@AllArgsConstructor
@RequestMapping("/wishlist")
public class WishListController extends BaseController {

    private final UserService userService;
    private final DrinkService drinkService;

    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<String> addToWishList(@RequestParam Long drinkId) {
        User currentUser = getCurrentUser();
        Drink drink = drinkService.getDrinkById(drinkId);

        if (drink != null) {
            userService.addDrinkToUser(currentUser.getId(), drinkId);
            return ResponseEntity.ok("ok");
        }

        return ResponseEntity.badRequest().body("Drink not found");
    }

    @PostMapping("/remove")
    @ResponseBody
    public ResponseEntity<String> removeFromWishList(@RequestParam Long drinkId) {
        User currentUser = getCurrentUser();
        Drink drink = drinkService.getDrinkById(drinkId);

        if (drink != null) {
            userService.removeDrinkFromUser(currentUser.getId(), drinkId);
            return ResponseEntity.ok("ok");
        }

        return ResponseEntity.badRequest().body("Drink not found");
    }

    @GetMapping
    public String getWishList(Model model) {
        Set<Drink> favoritesList = drinkService.getFavoriteDrinksByUserId(getCurrentUser().getId());
        model.addAttribute("favoritesList", favoritesList);
        return "user/wishlist";
    }


}
