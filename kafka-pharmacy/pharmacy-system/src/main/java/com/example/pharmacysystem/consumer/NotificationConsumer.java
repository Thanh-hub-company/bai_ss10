package com.example.pharmacysystem.consumer;

import com.example.pharmacysystem.dto.OrderEvent;
import com.example.pharmacysystem.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationConsumer {

    private final EmailService emailService;

    @KafkaListener(topics = "medicine-stock-events", groupId = "${spring.kafka.consumer.group-id}")
    public void handleNotification(OrderEvent orderEvent) {
        log.info("📩 [Notification Service] Đã nhận sự kiện bán hàng. Đang chuẩn bị gửi hóa đơn cho đơn hàng: {}", orderEvent.getOrderId());

        // Kích hoạt tiến trình gửi email
        emailService.sendInvoiceEmail(orderEvent);
    }
}