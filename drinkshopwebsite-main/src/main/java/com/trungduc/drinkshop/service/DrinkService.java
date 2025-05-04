package com.trungduc.drinkshop.service;

import com.trungduc.drinkshop.entity.Drink;
import com.trungduc.drinkshop.dto.DrinkDto;
import com.trungduc.drinkshop.dto.DrinkSearchDTO;
import com.trungduc.drinkshop.dto.MonthlyRevenueDTO;
import com.trungduc.drinkshop.dto.UserSearchDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface DrinkService {

    List<Drink> findAll();

    List<Drink> findAllActive();

    void addDrink(Drink drink, MultipartFile coverImage) throws IOException;

    void editDrink(Drink drink, MultipartFile coverImage) throws IOException;

    void deleteDrink(Long id) throws Exception;

    void setActiveFlag(Long id, boolean activeFlag) throws Exception;

    Drink getDrinkById(Long id);

    Drink getDrinkByName(String name);

    Page<Drink> searchDrinks(DrinkSearchDTO search, Pageable pageable);

    Page<Drink> searchDrinksUser(UserSearchDTO search, Pageable pageable);

    Page<Drink> getAllDrinksForUsers(Pageable pageable);

    List<Drink> getTop4BestSeller();

    List<Drink> getTop10BestSeller();

    List<DrinkDto> getTop10BestSellerByMonth(int month);

    List<MonthlyRevenueDTO> getMonthRevenuePerYear(int year);

    List<Drink> findAllOrderByCreatedDate();

    Set<Drink> getFavoriteDrinksByUserId(Long id);

    Long countDrink();

    List<Drink> getAllDrinksByCategoryId(Long id);

    Page<Drink> getAllDrinksPaginatedByCategoryId(Long categoryId, Pageable pageable);


}
