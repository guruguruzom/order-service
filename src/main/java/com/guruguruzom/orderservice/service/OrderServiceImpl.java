package com.guruguruzom.orderservice.service;

import com.guruguruzom.orderservice.dto.OrderDto;
import com.guruguruzom.orderservice.entity.OrderEntity;
import com.guruguruzom.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements  OrderService{

    private final OrderRepository orderRepository;
    public OrderDto createOrder(OrderDto orderDetails){
        return null;
    }
    public OrderDto getOrderByOrderId(String OrderId){
        return null;
    }
    public Iterable<OrderEntity> getOrderByUserId(String userId){
        return null;
    }
}
