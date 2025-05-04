package com.trungduc.drinkshop.service.impl;

import com.trungduc.drinkshop.constant.SortType;
import com.trungduc.drinkshop.dto.DrinkDto;
import com.trungduc.drinkshop.dto.DrinkSearchDTO;
import com.trungduc.drinkshop.dto.MonthlyRevenueDTO;
import com.trungduc.drinkshop.dto.UserSearchDTO;
import com.trungduc.drinkshop.entity.Drink;
import com.trungduc.drinkshop.entity.Category;
import com.trungduc.drinkshop.entity.OrderDetail;
import com.trungduc.drinkshop.entity.User;
import com.trungduc.drinkshop.repository.DrinkRepository;
import com.trungduc.drinkshop.repository.CategoryRepository;
import com.trungduc.drinkshop.repository.OrderDetailRepository;
import com.trungduc.drinkshop.repository.UserRepository;
import com.trungduc.drinkshop.service.DrinkService;
import com.trungduc.drinkshop.service.CategoryService;
import com.trungduc.drinkshop.service.FileUploadService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor
@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class DrinkServiceImpl implements DrinkService {

    DrinkRepository drinkRepository;
    CategoryRepository categoryRepository;
    UserRepository userRepository;
    CategoryService categoryService;
    FileUploadService fileUploadService;
    OrderDetailRepository orderDetailRepository;

    @Override
    public List<Drink> findAll() {
        return drinkRepository.findAll();
    }

    @Override
    public List<Drink> findAllActive() {
        return drinkRepository.findAllByActiveFlag(true);
    }

    @Override
    public void addDrink(Drink drink, MultipartFile coverImage) throws IOException {
        Drink savedDrink = drinkRepository.save(drink);
        savedDrink.setCoverImage(fileUploadService.uploadFile(coverImage));
        drinkRepository.save(drink);
    }

    @Override
    public void editDrink(Drink drink, MultipartFile coverImage) throws IOException {
        Drink savedDrink = drinkRepository.save(drink);
        if (!coverImage.isEmpty()) {
            savedDrink.setCoverImage(fileUploadService.uploadFile(coverImage));
            drinkRepository.save(drink);
        }
    }

    @Override
    public void deleteDrink(Long id) throws Exception {
        List<OrderDetail> orderDetailsFindByDrinkId = orderDetailRepository.findByDrinkId((id));
        if(!orderDetailsFindByDrinkId.isEmpty()){
            throw new Exception("Đồ Uống đã có trong các đơn hàng , vui lòng xoá các đơn hàng có đồ uống trước");
        }
        drinkRepository.deleteById(id);
    }

    @Override
    public void setActiveFlag(Long id, boolean activeFlag) throws Exception {
        Drink drinkById = drinkRepository.findById(id).orElse(null);
        if(drinkById == null){
            throw new Exception("Không tìm thấy đồ uống với id này");
        }
        drinkById.setActiveFlag(activeFlag);
        drinkRepository.save(drinkById);
    }

    @Override
    public Drink getDrinkById(Long id) {
        Optional<Drink> drinkOptional = drinkRepository.findById(id);
        return drinkOptional.orElse(null);
    }

    @Override
    public Drink getDrinkByName(String name) {
        return drinkRepository.findByTitleAndActiveFlag(name, true);
    }
    private String generateUniqueFileName(String originalFileName) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        return timestamp + "_" + originalFileName;
    }


    @Override
    public Page<Drink> searchDrinks(DrinkSearchDTO search, Pageable pageable) {
        Long categoryId = search.getCategoryId();
        String keyword = search.getKeyword();

        // Lấy dữ liệu phân trang dựa trên categoryId, keyword và các điều kiện tìm kiếm khác (nếu có)
        if (categoryId != null && keyword != null) {
            return drinkRepository.findByCategory_IdAndTitleContainingAndActiveFlag(categoryId, keyword, true, pageable);
        } else if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId).orElse(null);
            return drinkRepository.findByCategoryAndActiveFlag(category, true, pageable);

        } else if (keyword != null) {
            return drinkRepository.findByTitleContainingAndActiveFlag(keyword, true, pageable);
        } else {
            return drinkRepository.findAll(pageable);
        }
    }

    @Override
    public Page<Drink> searchDrinksUser(UserSearchDTO search, Pageable pageable) {
        Long categoryId = search.getCategoryId();
        String keyword = search.getKeyword();
        if (keyword == null) keyword = "";
        String sortBy = search.getSortBy();
        String amountGap = search.getAmountGap();
        String[] temp = amountGap.split(" ");
        Double startAmount = Double.parseDouble(temp[0].replace(".", ""));
        Double endAmount = Double.parseDouble(temp[3].replace(".", ""));


        Page<Drink> drinksPage = drinkRepository.findAll(pageable);
        if (categoryId != null) {
            if (sortBy.equals(SortType.oldest)) {
                drinksPage = drinkRepository.findByCategoryIdAndTitleContainingAndOriginalPriceBetweenAndActiveFlagOrderByCreatedAtAsc(categoryId, keyword, startAmount, endAmount, true, pageable);
            } else if (sortBy.equals(SortType.newest)) {
                drinksPage = drinkRepository.findByCategoryIdAndTitleContainingAndOriginalPriceBetweenAndActiveFlagOrderByCreatedAtDesc(categoryId, keyword, startAmount, endAmount, true, pageable);

            } else if (sortBy.equals(SortType.priceLowToHigh)) {
                drinksPage = drinkRepository.findByCategoryIdAndTitleContainingAndOriginalPriceBetweenAndActiveFlagOrderBySalePriceAsc(categoryId, keyword, startAmount, endAmount, true, pageable);

            } else {
                drinksPage = drinkRepository.findByCategoryIdAndTitleContainingAndOriginalPriceBetweenAndActiveFlagOrderBySalePriceDesc(categoryId, keyword, startAmount, endAmount, true, pageable);
            }
        } else {
            if (sortBy.equals(SortType.oldest)) {
                drinksPage = drinkRepository.findByTitleContainingAndOriginalPriceBetweenAndActiveFlagOrderByCreatedAtAsc(keyword, startAmount, endAmount, true, pageable);
            } else if (sortBy.equals(SortType.newest)) {
                drinksPage = drinkRepository.findByTitleContainingAndOriginalPriceBetweenAndActiveFlagOrderByCreatedAtDesc(keyword, startAmount, endAmount, true, pageable);

            } else if (sortBy.equals(SortType.priceLowToHigh)) {
                drinksPage = drinkRepository.findByTitleContainingAndOriginalPriceBetweenAndActiveFlagOrderBySalePriceAsc(keyword, startAmount, endAmount, true, pageable);

            } else {
                drinksPage = drinkRepository.findByTitleContainingAndOriginalPriceBetweenAndActiveFlagOrderBySalePriceDesc(keyword, startAmount, endAmount, true, pageable);
            }
        }
        if (sortBy == null) {
            drinksPage = drinkRepository.findByCategoryIdAndTitleContainingAndOriginalPriceBetweenAndActiveFlagOrderByCreatedAtDesc(categoryId, keyword, startAmount, endAmount, true, pageable);
        }
        return drinksPage;
    }


    @Override
    public Page<Drink> getAllDrinksForUsers(Pageable pageable) {
        return drinkRepository.findAll(pageable);
    }

    @Override
    public List<Drink> getTop4BestSeller() {
        return drinkRepository.findTop4ByActiveFlagOrderByBuyCountDesc(true);
    }

    @Override
    public List<Drink> getTop10BestSeller() {
        return drinkRepository.findTop10ByActiveFlagOrderByTotalRevenueDesc(true);
    }

    @Override
    public List<DrinkDto> getTop10BestSellerByMonth(int month) {
        List<Object[]> result = drinkRepository.findTop10BestSellerByMonth(month);
        List<DrinkDto> resultConvertedToDto = new ArrayList<>();
        for (Object[] item : result) {
            resultConvertedToDto.add(new DrinkDto(item[0].toString(), Double.parseDouble(item[1].toString())));
        }
        return resultConvertedToDto;
    }

    @Override
    public List<Drink> findAllOrderByCreatedDate() {
        return drinkRepository.findByActiveFlagOrderByCreatedAtDesc(true);
    }

    @Override
    public Set<Drink> getFavoriteDrinksByUserId(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            return user.getFavoriteDrinks();
        }
        return Collections.emptySet();
    }

    @Override
    public Long countDrink() {
        return drinkRepository.count();
    }

    @Override
    public List<Drink> getAllDrinksByCategoryId(Long id) {
        Category category = categoryRepository.findById(id).orElse(null);
        return drinkRepository.findAllByCategoryAndActiveFlag(category, true);
    }

    @Override
    public Page<Drink> getAllDrinksPaginatedByCategoryId(Long categoryId, Pageable pageable) {
        return drinkRepository.findByCategoryIdAndActiveFlag(categoryId, true, pageable);
    }

    @Override
    public List<MonthlyRevenueDTO> getMonthRevenuePerYear(int year) {
        List<Object[]> result = drinkRepository.findMonthlyRevenue(year);
        List<MonthlyRevenueDTO> resultConvertedToDto = new ArrayList<>();
        for (Object[] item : result) {
            resultConvertedToDto.add(new MonthlyRevenueDTO(Integer.parseInt(item[0].toString()), Double.parseDouble(item[1].toString())));
        }
        return resultConvertedToDto;
    }


}
