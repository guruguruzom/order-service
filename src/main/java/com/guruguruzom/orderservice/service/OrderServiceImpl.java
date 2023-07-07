package com.guruguruzom.orderservice.service;

import com.guruguruzom.orderservice.dto.OrderDto;
import com.guruguruzom.orderservice.entity.OrderEntity;
import com.guruguruzom.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Slf4j
@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements  OrderService{

    private final OrderRepository orderRepository;

    public OrderDto createOrder(OrderDto orderDto){
        orderDto.setOrderId(UUID.randomUUID().toString());
        orderDto.setTotalPrice(orderDto.getUnitPrice() * orderDto.getQty());

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT); //자료형이 딱맞아 떨어져야 작동하도록
        OrderEntity orderEntity = mapper.map(orderDto, OrderEntity.class);


        orderRepository.save(orderEntity);

        OrderDto returnUserDto = mapper.map(orderEntity, OrderDto.class);

        return returnUserDto;
    }

    @Override
    public OrderDto getOrderByOrderId(String OrderId){
        OrderEntity orderEntity = orderRepository.findByUserId(OrderId);
        OrderDto orderDto = new ModelMapper().map(orderEntity, OrderDto.class);

        return orderDto;

    }

    @Override
    public Iterable<OrderEntity> getOrderByUserId(String userId){

        return orderRepository.findByOrderId(userId);
    }

}
