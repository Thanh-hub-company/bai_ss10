package com.example.inventoryservice.repository;

import com.example.inventoryservice.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MedicineRepository extends JpaRepository<Medicine, String> {

    /**
     * Lệnh SQL trực tiếp trừ số lượng kho an toàn (Atomic Update trong DB).
     */
    @Modifying
    @Query("UPDATE Medicine m SET m.stock = m.stock - :quantity WHERE m.id = :medicineId AND m.stock >= :quantity")
    int reduceStock(@Param("medicineId") String medicineId, @Param("quantity") Integer quantity);
}