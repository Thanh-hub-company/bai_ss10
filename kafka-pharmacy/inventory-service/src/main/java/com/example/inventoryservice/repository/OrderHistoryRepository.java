package com.example.inventoryservice.repository;

import com.example.inventoryservice.entity.OrderHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderHistoryRepository extends JpaRepository<OrderHistory, String> {
}
