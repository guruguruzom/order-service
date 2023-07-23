package com.guruguruzom.orderservice.controller;

import com.guruguruzom.orderservice.dto.OrderDto;
import com.guruguruzom.orderservice.entity.OrderEntity;
import com.guruguruzom.orderservice.messagequeue.KafkaProducer;
import com.guruguruzom.orderservice.messagequeue.OrderProducer;
import com.guruguruzom.orderservice.service.OrderService;
import com.guruguruzom.orderservice.vo.RequestOrder;
import com.guruguruzom.orderservice.vo.ResponseOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OrderController {

    private final Environment env;
    private final OrderService orderService;
    private final KafkaProducer kafkaProducer;
    private final OrderProducer orderProducer;

    @GetMapping("/health-check")
    public String status(){
        return String.format( "server check in order service on Port %s",
                env.getProperty("local.server.port"));
    }

    @PostMapping("/{userId}/orders")
    public ResponseEntity<ResponseOrder> createOrder(@PathVariable("userId") String userId, @RequestBody RequestOrder orderDetails){
        log.info("Before add order data");
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        OrderDto orderDto = mapper.map(orderDetails, OrderDto.class);
        orderDto.setUserId(userId);
        /* jpa */
        OrderDto createOrder = orderService.createOrder(orderDto);
        ResponseOrder responseOrder = mapper.map(createOrder, ResponseOrder.class);

        /* kafka */
//        orderDto.setOrderId(UUID.randomUUID().toString());
//        orderDto.setTotalPrice(orderDetails.getQty() * orderDetails.getUnitPrice());


        /* send this order to the kafka */
//        kafkaProducer.send("example-catalog-topic", orderDto);
//        orderProducer.send("orders", orderDto);
//        ResponseOrder responseOrder = mapper.map(orderDto, ResponseOrder.class);
        log.info("Affter added order data");
        return ResponseEntity.status(HttpStatus.CREATED).body(responseOrder);

    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<ResponseOrder>> getOrders(@PathVariable("userId") String userId) throws Exception{
        log.info("Before retrieve order data");

        Iterable<OrderEntity> orderList = orderService.getOrderByUserId(userId);

        List<ResponseOrder> result = new ArrayList<>();

        orderList.forEach(v->{
            result.add(new ModelMapper().map(v, ResponseOrder.class));
        });

//        try {
//            Thread.sleep(1000);
//            throw new Exception("forced exception");
//        } catch (InterruptedException ex){
//            log.warn(ex.getMessage());
//        }

        log.info("After retrieve order data");
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
