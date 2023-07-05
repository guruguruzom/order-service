package com.guruguruzom.orderservice.repository;

import com.guruguruzom.orderservice.entity.OrderEntity;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository  extends CrudRepository<OrderEntity, Long> {
    //CatalogEntity findByProductId(String productId);
    OrderEntity findByOrderId(String orderId);
    Iterable<OrderEntity> findById(String userId);
}