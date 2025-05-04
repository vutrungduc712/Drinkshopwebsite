package com.trungduc.drinkshop.service.impl;

import com.trungduc.drinkshop.entity.Order;
import com.trungduc.drinkshop.entity.OrderDetail;
import com.trungduc.drinkshop.service.OrderDetailService;
import com.trungduc.drinkshop.repository.OrderDetailRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class OrderDetailServiceImpl implements OrderDetailService {

    private OrderDetailRepository orderDetailRepository;

    @Override
    public List<OrderDetail> getAllOrderDetailByOrder(Order order) {
        return orderDetailRepository.findByOrder(order);
    }
}
