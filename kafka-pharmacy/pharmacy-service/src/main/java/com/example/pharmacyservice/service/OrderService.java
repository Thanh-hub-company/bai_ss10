package com.example.pharmacyservice.service;

import com.example.pharmacyservice.dto.OrderEvent;
import com.example.pharmacyservice.dto.OrderRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    private static final String TOPIC_NAME = "medicine-stock-events";

    // Inject KafkaTemplate được cấu hình ở lớp KafkaProducerConfig
    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    public OrderService(KafkaTemplate<String, OrderEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public OrderEvent processCheckout(OrderRequest request) {
        // 1. Tạo đối tượng OrderEvent với mã đơn hàng và thời gian tự động
        OrderEvent orderEvent = OrderEvent.builder()
                .orderId("ORD-" + UUID.randomUUID().toString().substring(0, 8))
                .medicineId(request.getMedicineId())
                .quantity(request.getQuantity())
                .timestamp(LocalDateTime.now().toString())
                .build();

        /*
         * 2. Giải quyết THỬ THÁCH: Sử dụng Message Key là medicineId
         * Phương thức kafkaTemplate.send(topic, key, data)
         * - Argument 1: Tên topic ("medicine-stock-events")
         * - Argument 2: Key (orderEvent.getMedicineId()) -> Giúp Kafka Hash và gán toàn bộ đơn hàng
         *   cùng loại thuốc vào duy nhất 1 Partition cố định.
         * - Argument 3: Payload (đối tượng orderEvent)
         */
        CompletableFuture<SendResult<String, OrderEvent>> future =
                kafkaTemplate.send(TOPIC_NAME, orderEvent.getMedicineId(), orderEvent);

        // 3. Xử lý Callback lắng nghe phản hồi từ Kafka Broker
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Gửi sự kiện đơn hàng thành công! OrderID: {}, Partition: {}, Offset: {}",
                        orderEvent.getOrderId(),
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
            } else {
                log.error("Gửi sự kiện đơn hàng thất bại! OrderID: {}, Lỗi: {}",
                        orderEvent.getOrderId(), ex.getMessage());
            }
        });

        return orderEvent;
    }
}
