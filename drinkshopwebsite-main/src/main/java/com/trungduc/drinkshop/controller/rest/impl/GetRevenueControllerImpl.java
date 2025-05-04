package com.trungduc.drinkshop.controller.rest.impl;

import com.trungduc.drinkshop.controller.rest.IGetRevenueController;
import com.trungduc.drinkshop.controller.rest.base.RestApiV1;
import com.trungduc.drinkshop.controller.rest.base.VsResponseUtil;
import com.trungduc.drinkshop.service.DrinkService;
import com.trungduc.drinkshop.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestApiV1
@AllArgsConstructor
public class GetRevenueControllerImpl implements IGetRevenueController {

    private DrinkService drinkService;
    private CategoryService categoryService;

    @Override
    public ResponseEntity<?> getProductRevenueByMonth(@PathVariable("selectedMonth") int selectedMonth) throws UnsupportedEncodingException {
        return VsResponseUtil.ok(HttpStatus.OK, drinkService.getTop10BestSellerByMonth(selectedMonth));
    }

    @Override
    public ResponseEntity<?> getMonthRevenueByYear(@PathVariable("selectedYear") int selectedYear) throws UnsupportedEncodingException {
        return VsResponseUtil.ok(HttpStatus.OK, drinkService.getMonthRevenuePerYear(selectedYear));
    }

}
