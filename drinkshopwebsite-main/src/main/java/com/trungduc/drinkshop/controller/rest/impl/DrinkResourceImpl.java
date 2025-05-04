package com.trungduc.drinkshop.controller.rest.impl;

import com.trungduc.drinkshop.controller.rest.base.RestApiV1;
import com.trungduc.drinkshop.controller.rest.base.VsResponseUtil;
import com.trungduc.drinkshop.entity.Drink;
import com.trungduc.drinkshop.service.DrinkService;
import com.trungduc.drinkshop.controller.rest.IDrinkResource;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestApiV1
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DrinkResourceImpl implements IDrinkResource {

    DrinkService drinkService;

    @Override
    public ResponseEntity<?> getDrinkById(Long drinkId) {
        return VsResponseUtil.ok(HttpStatus.OK, drinkService.getDrinkById(drinkId));
    }

    @Override
    public ResponseEntity<?> getDrinkListPaginatedAndSorted(String sortBy, Long categoryId, int page, int size) {
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
            case "price-high-to-low":
                sortedDrinkList = drinkList.stream()
                        .sorted(Comparator.comparing(Drink::getSalePrice).reversed())
                        .collect(Collectors.toList());
            case "newest":
                sortedDrinkList = drinkList.stream()
                        .sorted(Comparator.comparing(Drink::getPublishedDate))
                        .collect(Collectors.toList());
            case "oldest":
                sortedDrinkList = drinkList.stream()
                        .sorted(Comparator.comparing(Drink::getPublishedDate).reversed())
                        .collect(Collectors.toList());
            default:
                sortedDrinkList = drinkList;
        }
        return VsResponseUtil.ok(HttpStatus.OK, sortedDrinkList);
    }
}
