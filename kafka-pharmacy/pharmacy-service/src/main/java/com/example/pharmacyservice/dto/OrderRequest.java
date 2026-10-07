package com.example.pharmacyservice.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderRequest {
    @JsonProperty("medicineId")
    @JsonAlias({"medicine_id", "medicineID", "medicine", "id"})
    private String medicineId;

    @JsonProperty("quantity")
    @JsonAlias({"amount", "count", "qty"})
    private Integer quantity;

    public OrderRequest() {
    }

    public OrderRequest(String medicineId, Integer quantity) {
        this.medicineId = medicineId;
        this.quantity = quantity;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String medicineId;
        private Integer quantity;

        public Builder medicineId(String medicineId) {
            this.medicineId = medicineId;
            return this;
        }

        public Builder quantity(Integer quantity) {
            this.quantity = quantity;
            return this;
        }

        public OrderRequest build() {
            return new OrderRequest(medicineId, quantity);
        }
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

    @Override
    public String toString() {
        return "OrderRequest{" +
                "medicineId='" + medicineId + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
