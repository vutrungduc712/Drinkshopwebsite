package com.trungduc.drinkshop.service.impl;

import com.trungduc.drinkshop.dto.CategoryDto;
import com.trungduc.drinkshop.entity.Category;
import com.trungduc.drinkshop.service.CategoryService;
import com.trungduc.drinkshop.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public Page<Category> getAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryById(Long categoryId) {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        return categoryOptional.orElse(null);
    }

    @Override
    public void addCategory(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public void updateCategory(Long categoryId, Category updatedCategory) {
        Category existingCategory = getCategoryById(categoryId);
        if (existingCategory != null) {
            existingCategory.setName(updatedCategory.getName());
            existingCategory.setDescription(updatedCategory.getDescription());
            categoryRepository.save(existingCategory);
        }
    }

    @Override
    public void deleteCategory(Long categoryId) {
        Category category = getCategoryById(categoryId);
        if (category != null) {
            categoryRepository.delete(category);
        }
    }

    @Override
    public List<CategoryDto> getTop10BestSellerByMonth(int month) {
        List<Object[]> result = categoryRepository.findTop10BestSellerByMonth(month);
        List<CategoryDto> resultConvertedToDto = new ArrayList<>();
        for (Object[] item : result) {
            resultConvertedToDto.add(new CategoryDto(item[0].toString(), Double.parseDouble(item[1].toString())));
        }
        return resultConvertedToDto;
    }
}
