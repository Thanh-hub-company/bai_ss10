package com.example.inventoryservice.service;

import com.example.inventoryservice.dto.OrderEvent;
import com.example.inventoryservice.entity.Medicine;
import com.example.inventoryservice.entity.OrderHistory;
import com.example.inventoryservice.repository.MedicineRepository;
import com.example.inventoryservice.repository.OrderHistoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryService {

    private static final Logger log = LoggerFactory.getLogger(InventoryService.class);

    private final MedicineRepository medicineRepository;
    private final OrderHistoryRepository orderHistoryRepository;

    public InventoryService(MedicineRepository medicineRepository, OrderHistoryRepository orderHistoryRepository) {
        this.medicineRepository = medicineRepository;
        this.orderHistoryRepository = orderHistoryRepository;
    }

    /**
     * Xử lý nguyên tử (Atomic Transaction):
     * 1. Trừ số lượng tồn kho của thuốc trong bảng medicines.
     * 2. Thêm mới bản ghi lịch sử vào bảng order_history.
     */
    @Transactional
    public void processOrder(OrderEvent event) {
        log.info("==> Đang xử lý sự kiện đơn hàng ID: {} cho thuốc: {}", event.getOrderId(), event.getMedicineId());

        // 1. Kiểm tra sự tồn tại của Thuốc
        Medicine medicine = medicineRepository.findById(event.getMedicineId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thuốc trong kho: " + event.getMedicineId()));

        if (medicine.getStock() < event.getQuantity()) {
            log.error("❌ Số lượng tồn kho không đủ! Tồn: {}, Yêu cầu: {}", medicine.getStock(), event.getQuantity());
            return;
        }

        // 2. Thực hiện trừ tồn kho
        int updatedRows = medicineRepository.reduceStock(event.getMedicineId(), event.getQuantity());

        if (updatedRows > 0) {
            // 3. Thêm bản ghi đơn hàng mới vào DB
            OrderHistory history = OrderHistory.builder()
                    .orderId(event.getOrderId())
                    .medicineId(event.getMedicineId())
                    .quantity(event.getQuantity())
                    .createdAt(event.getTimestamp())
                    .build();
            orderHistoryRepository.save(history);

            log.info("✅ Cập nhật kho thành công! Thuốc: {}, Tồn mới: {}",
                    event.getMedicineId(), (medicine.getStock() - event.getQuantity()));
        } else {
            log.error("❌ Cập nhật tồn kho thất bại!");
        }
    }
}
