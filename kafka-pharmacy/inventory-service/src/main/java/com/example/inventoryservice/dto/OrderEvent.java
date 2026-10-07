package com.example.inventoryservice.dto;

/**
 * Object DTO khớp cấu trúc JSON gửi từ pharmacy-service (Producer).
 */
public class OrderEvent {
    private String orderId;
    private String medicineId;
    private Integer quantity;
    private String timestamp;

    public OrderEvent() {
    }

    public OrderEvent(String orderId, String medicineId, Integer quantity, String timestamp) {
        this.orderId = orderId;
        this.medicineId = medicineId;
        this.quantity = quantity;
        this.timestamp = timestamp;
    }

    public static OrderEventBuilder builder() {
        return new OrderEventBuilder();
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public static class OrderEventBuilder {
        private String orderId;
        private String medicineId;
        private Integer quantity;
        private String timestamp;

        public OrderEventBuilder orderId(String orderId) {
            this.orderId = orderId;
            return this;
        }

        public OrderEventBuilder medicineId(String medicineId) {
            this.medicineId = medicineId;
            return this;
        }

        public OrderEventBuilder quantity(Integer quantity) {
            this.quantity = quantity;
            return this;
        }

        public OrderEventBuilder timestamp(String timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public OrderEvent build() {
            return new OrderEvent(orderId, medicineId, quantity, timestamp);
        }
    }
}