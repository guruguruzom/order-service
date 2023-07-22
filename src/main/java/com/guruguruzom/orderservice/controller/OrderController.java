package com.guruguruzom.orderservice.controller;

import com.guruguruzom.orderservice.dto.OrderDto;
import com.guruguruzom.orderservice.entity.OrderEntity;
import com.guruguruzom.orderservice.messagequeue.KafkaProducer;
import com.guruguruzom.orderservice.service.OrderService;
import com.guruguruzom.orderservice.vo.RequestOrder;
import com.guruguruzom.orderservice.vo.ResponseOrder;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final Environment env;
    private final OrderService orderService;
    private final KafkaProducer kafkaProducer;

    @GetMapping("/health-check")
    public String status(){
        return String.format( "server check in order service on Port %s",
                env.getProperty("local.server.port"));
    }

    @PostMapping("/{userId}/orders")
    public ResponseEntity<ResponseOrder> createOrder(@PathVariable("userId") String userId, @RequestBody RequestOrder orderDetails){
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        /* jpa */
        OrderDto orderDto = mapper.map(orderDetails, OrderDto.class);
        orderDto.setUserId(userId);
        OrderDto createOrder = orderService.createOrder(orderDto);

        ResponseOrder responseOrder = mapper.map(createOrder, ResponseOrder.class);

        /* send this order to the kafka */
        kafkaProducer.send("example-catalog-topic", orderDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseOrder);

    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<ResponseOrder>> getOrders(@PathVariable("userId") String userId){
        Iterable<OrderEntity> orderList = orderService.getOrderByUserId(userId);

        List<ResponseOrder> result = new ArrayList<>();

        orderList.forEach(v->{
            result.add(new ModelMapper().map(v, ResponseOrder.class));
        });

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

//    @GetMapping("/{orderId}/order")
//    public ResponseEntity<ResponseOrder> getOrder(@PathVariable("orderId") String orderId){
//        OrderDto orderDetails = orderService.getOrderByOrderId(orderId);
//
//        ModelMapper mapper = new ModelMapper();
//        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
//        OrderDto orderDto = mapper.map(orderDetails, OrderDto.class);
//
//        ResponseOrder responseOrder = mapper.map(orderDto, ResponseOrder.class);
//
//
//        return ResponseEntity.status(HttpStatus.CREATED).body(responseOrder);
//    }
}
