package com.techrc.order_service.repositories;

import com.techrc.order_service.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
