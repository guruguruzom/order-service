package com.guruguruzom.orderservice.service;

import com.guruguruzom.orderservice.dto.OrderDto;
import com.guruguruzom.orderservice.entity.OrderEntity;

public interface OrderService {
    OrderDto createOrder(OrderDto orderDetails);
    OrderDto getOrderByOrderId(String OrderId);
    Iterable<OrderEntity> getOrderByUserId(String userId);
}
