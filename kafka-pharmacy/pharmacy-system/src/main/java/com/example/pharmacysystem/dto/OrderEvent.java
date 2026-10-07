package com.example.pharmacysystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderEvent {
    private String orderId;
    private String medicineId;
    private Integer quantity;
    private String timestamp;
    private String customerEmail; // Có thể bổ sung trường này từ đầu vào API nếu muốn
}
