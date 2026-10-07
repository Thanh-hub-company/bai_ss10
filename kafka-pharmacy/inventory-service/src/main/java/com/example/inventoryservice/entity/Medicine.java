package com.example.inventoryservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "medicines")
public class Medicine {
    @Id
    private String id;        // Mã thuốc (vd: PARA-500)
    private String name;      // Tên thuốc
    private Integer stock;    // Số lượng tồn kho hiện tại

    public Medicine() {
    }

    public Medicine(String id, String name, Integer stock) {
        this.id = id;
        this.name = name;
        this.stock = stock;
    }

    public static MedicineBuilder builder() {
        return new MedicineBuilder();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public static class MedicineBuilder {
        private String id;
        private String name;
        private Integer stock;

        public MedicineBuilder id(String id) {
            this.id = id;
            return this;
        }

        public MedicineBuilder name(String name) {
            this.name = name;
            return this;
        }

        public MedicineBuilder stock(Integer stock) {
            this.stock = stock;
            return this;
        }

        public Medicine build() {
            return new Medicine(id, name, stock);
        }
    }
}