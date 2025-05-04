package com.trungduc.drinkshop.service;

import com.trungduc.drinkshop.dto.DrinkDto;
import com.trungduc.drinkshop.dto.CategoryDto;
import com.trungduc.drinkshop.dto.OrderDTO;
import com.trungduc.drinkshop.entity.User;

import java.util.List;

public interface ExportService {

    String exportOrderReport(User user, List<OrderDTO> orderDTOList, String keyword);

    String exportCategoryReport(User user, List<CategoryDto> categoryDTOList, String keyword);

    String exportDrinkReport(User user, List<DrinkDto> drinkDtoList, String keyword);

}
