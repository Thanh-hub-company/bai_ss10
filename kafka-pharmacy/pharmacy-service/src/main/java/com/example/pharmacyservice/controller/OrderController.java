package com.example.pharmacyservice.controller;

import com.example.pharmacyservice.dto.OrderEvent;
import com.example.pharmacyservice.dto.OrderRequest;
import com.example.pharmacyservice.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * API mô phỏng sự kiện bấm nút "Thanh toán"
     * POST http://localhost:8081/api/orders/checkout
     */
    @PostMapping("/checkout")
    public ResponseEntity<Map<String, Object>> checkout(@RequestBody(required = false) OrderRequest request) {
        try {
            if (request == null) {
                request = new OrderRequest();
            }
            if (request.getMedicineId() == null || request.getMedicineId().trim().isEmpty()) {
                request.setMedicineId("MED-DEFAULT");
            }
            if (request.getQuantity() == null || request.getQuantity() <= 0) {
                request.setQuantity(1);
            }

            // Gọi Service xử lý đóng gói Event và đẩy lên Kafka
            OrderEvent orderEvent = orderService.processCheckout(request);

            // Chuẩn bị Response phản hồi thành công cho giao diện
            Map<String, Object> response = new HashMap<>();
            response.put("status", "SUCCESS");
            response.put("message", "Thanh toán thành công và đã gửi sự kiện đến Kafka!");
            response.put("data", orderEvent);

            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "ERROR");
            errorResponse.put("message", "Không thể gửi dữ liệu đến Kafka Broker. Vui lòng đảm bảo Docker Kafka đang chạy (docker compose up -d): " + ex.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
}