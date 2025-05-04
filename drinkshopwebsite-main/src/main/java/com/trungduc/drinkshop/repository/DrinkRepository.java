package com.trungduc.drinkshop.repository;

import com.trungduc.drinkshop.entity.Drink;
import com.trungduc.drinkshop.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DrinkRepository extends JpaRepository<Drink, Long> {

    Page<Drink> findByTitleContainingAndActiveFlag(String keyword, boolean activeFlag, Pageable pageable);

    Drink findByTitleAndActiveFlag(String title, boolean activeFlag);

    Page<Drink> findByCategoryAndActiveFlag(Category category, boolean activeFlag, Pageable pageable);

    Page<Drink> findByCategoryIdAndOriginalPriceBetweenAndActiveFlag(Long categoryId, double minPrice, double maxPrice, boolean activeFlag, Pageable pageable);

    Page<Drink> findByCategory_IdAndTitleContainingAndOriginalPriceBetweenAndActiveFlag(Long categoryId, String keyword, double minPrice, double maxPrice, boolean activeFlag, Pageable pageable);

    Page<Drink> findByCategoryIdAndTitleContainingAndOriginalPriceBetweenAndActiveFlagOrderBySalePriceDesc(Long categoryId, String keyword, double minPrice, double maxPrice, boolean activeFlag, Pageable pageable);

    Page<Drink> findByCategoryIdAndTitleContainingAndOriginalPriceBetweenAndActiveFlagOrderBySalePriceAsc(Long categoryId, String keyword, double minPrice, double maxPrice, boolean activeFlag, Pageable pageable);

    Page<Drink> findByCategoryIdAndTitleContainingAndOriginalPriceBetweenAndActiveFlagOrderByCreatedAtAsc(Long categoryId, String keyword, double minPrice, double maxPrice, boolean activeFlag, Pageable pageable);

    Page<Drink> findByCategoryIdAndTitleContainingAndOriginalPriceBetweenAndActiveFlagOrderByCreatedAtDesc(Long categoryId, String keyword, double minPrice, double maxPrice, boolean activeFlag, Pageable pageable);

    Page<Drink> findByTitleContainingAndOriginalPriceBetweenAndActiveFlagOrderByCreatedAtAsc(String title, double minPrice, double maxPrice, boolean activeFlag, Pageable pageable);

    Page<Drink> findByTitleContainingAndOriginalPriceBetweenAndActiveFlagOrderByCreatedAtDesc(String title, double minPrice, double maxPrice, boolean activeFlag, Pageable pageable);

    Page<Drink> findByTitleContainingAndOriginalPriceBetweenAndActiveFlagOrderBySalePriceAsc(String title, double minPrice, double maxPrice, boolean activeFlag, Pageable pageable);

    Page<Drink> findByTitleContainingAndOriginalPriceBetweenAndActiveFlagOrderBySalePriceDesc(String title, double minPrice, double maxPrice, boolean activeFlag, Pageable pageable);

    List<Drink> findTop4ByActiveFlagOrderByBuyCountDesc(boolean activeFlag);

    List<Drink> findByActiveFlagOrderByCreatedAtDesc(boolean activeFlag);

    List<Drink> findTop10ByActiveFlagOrderByTotalRevenueDesc(boolean activeFlag);

    List<Drink> findAllByActiveFlag(boolean activeFlag);

    List<Drink> findAllByCategoryAndActiveFlag(Category category, boolean activeFlag);

    Page<Drink> findByCategoryIdAndActiveFlag(Long categoryId, boolean activeFlag, Pageable pageable);

    Page<Drink> findByCategory_IdAndTitleContainingAndActiveFlag(Long categoryId, String keyword, boolean activeFlag, Pageable pageable);

    @Query(value = "SELECT b.title, SUM(od.price * od.quantity) AS total_revenue FROM drinks b " +
            "JOIN order_details od ON b.id = od.drink_id " +
            "JOIN orders o ON od.order_id = o.id " +
            "WHERE MONTH(o.create_date) = :month " +
            "GROUP BY b.title " +
            "ORDER BY total_revenue " +
            "DESC " +
            "LIMIT 10", nativeQuery = true)
    List<Object[]> findTop10BestSellerByMonth(@Param("month") int month);

    @Query("SELECT MONTH(od.createdAt) AS month, SUM(od.totalPrice) AS totalRevenue " +
            "FROM Order od " +
            "WHERE YEAR(od.createdAt) = :year " +
            "GROUP BY MONTH(od.createdAt) " +
            "ORDER BY MONTH(od.createdAt)")
    List<Object[]> findMonthlyRevenue(@Param("year") int year);

    List<Drink> findTop4ByCategoryIdAndActiveFlag(Long id, boolean b);
}