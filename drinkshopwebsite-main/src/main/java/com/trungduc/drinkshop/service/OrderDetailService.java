package com.trungduc.drinkshop.service;

import com.trungduc.drinkshop.entity.Order;
import com.trungduc.drinkshop.entity.OrderDetail;

import java.util.List;

public interface OrderDetailService {
    List<OrderDetail> getAllOrderDetailByOrder(Order order);
}
