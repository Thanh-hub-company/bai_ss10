package com.example.inventoryservice;

import com.example.inventoryservice.entity.Medicine;
import com.example.inventoryservice.repository.MedicineRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }

    /**
     * Thêm sẵn dữ liệu mẫu thuốc PARA-500 với số lượng 100 viên vào Database khi ứng dụng khởi chạy.
     */
    @Bean
    CommandLineRunner initDatabase(MedicineRepository repository) {
        return args -> {
            repository.save(Medicine.builder()
                    .id("MED-PANADOL")
                    .name("Panadol Extra")
                    .stock(100)
                    .build());
            repository.save(Medicine.builder()
                    .id("PARA-500")
                    .name("Paracetamol 500mg")
                    .stock(100)
                    .build());
            repository.save(Medicine.builder()
                    .id("MED-DEFAULT")
                    .name("Default Medicine")
                    .stock(50)
                    .build());
        };
    }
}
