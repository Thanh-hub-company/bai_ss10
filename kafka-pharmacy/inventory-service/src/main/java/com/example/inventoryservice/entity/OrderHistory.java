package com.example.inventoryservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_history")
public class OrderHistory {
    @Id
    private String orderId;     // Mã đơn hàng nhận từ Event
    private String medicineId;  // Mã thuốc
    private Integer quantity;   // Số lượng đã mua
    private String createdAt;   // Thời gian giao dịch

    public OrderHistory() {
    }

    public OrderHistory(String orderId, String medicineId, Integer quantity, String createdAt) {
        this.orderId = orderId;
        this.medicineId = medicineId;
        this.quantity = quantity;
        this.createdAt = createdAt;
    }

    public static OrderHistoryBuilder builder() {
        return new OrderHistoryBuilder();
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(String medicineId) {
        this.medicineId = medicineId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public static class OrderHistoryBuilder {
        private String orderId;
        private String medicineId;
        private Integer quantity;
        private String createdAt;

        public OrderHistoryBuilder orderId(String orderId) {
            this.orderId = orderId;
            return this;
        }

        public OrderHistoryBuilder medicineId(String medicineId) {
            this.medicineId = medicineId;
            return this;
        }

        public OrderHistoryBuilder quantity(Integer quantity) {
            this.quantity = quantity;
            return this;
        }

        public OrderHistoryBuilder createdAt(String createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public OrderHistory build() {
            return new OrderHistory(orderId, medicineId, quantity, createdAt);
        }
    }
}