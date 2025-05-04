package com.trungduc.drinkshop.repository;

import com.trungduc.drinkshop.entity.Order;
import com.trungduc.drinkshop.entity.OrderDetail;
import com.trungduc.drinkshop.entity.composite_key.OrderDetailId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, OrderDetailId> {
    List<OrderDetail> findByOrder(Order order);

    @Query("SELECT od.quantity FROM OrderDetail od WHERE od.order.id = :orderId AND od.drink.id = :drinkId")
    int findByDrinkAndOrOrder(Long orderId, Long drinkId);

    List<OrderDetail> findByDrinkId(long drinkId);
}
