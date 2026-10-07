package com.example.pharmacyservice.dto;

public class OrderEvent {
    private String orderId;
    private String medicineId;
    private int quantity;
    private String timestamp;

    public OrderEvent() {
    }

    public OrderEvent(String orderId, String medicineId, int quantity, String timestamp) {
        this.orderId = orderId;
        this.medicineId = medicineId;
        this.quantity = quantity;
        this.timestamp = timestamp;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String orderId;
        private String medicineId;
        private int quantity;
        private String timestamp;

        public Builder orderId(String orderId) {
            this.orderId = orderId;
            return this;
        }

        public Builder medicineId(String medicineId) {
            this.medicineId = medicineId;
            return this;
        }

        public Builder quantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder timestamp(String timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public OrderEvent build() {
            return new OrderEvent(orderId, medicineId, quantity, timestamp);
        }
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "OrderEvent{" +
                "orderId='" + orderId + '\'' +
                ", medicineId='" + medicineId + '\'' +
                ", quantity=" + quantity +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
