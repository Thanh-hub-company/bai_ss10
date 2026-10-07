package com.example.inventoryservice.consumer;

import com.example.inventoryservice.dto.OrderEvent;
import com.example.inventoryservice.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class InventoryConsumer {

    private static final Logger log = LoggerFactory.getLogger(InventoryConsumer.class);

    private final InventoryService inventoryService;

    public InventoryConsumer(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @KafkaListener(topics = "medicine-stock-events", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeOrderEvent(OrderEvent event) {
        log.info("==> Đã nhận được sự kiện đơn hàng từ Kafka: orderId={}, medicineId={}, quantity={}",
                event.getOrderId(), event.getMedicineId(), event.getQuantity());
        inventoryService.processOrder(event);
    }
}