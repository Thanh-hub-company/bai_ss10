package com.example.inventoryservice;

import com.example.inventoryservice.consumer.InventoryConsumer;
import com.example.inventoryservice.dto.OrderEvent;
import com.example.inventoryservice.entity.Medicine;
import com.example.inventoryservice.entity.OrderHistory;
import com.example.inventoryservice.repository.MedicineRepository;
import com.example.inventoryservice.repository.OrderHistoryRepository;
import com.example.inventoryservice.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InventoryServiceApplicationTests {

    private MedicineRepository medicineRepository;
    private OrderHistoryRepository orderHistoryRepository;
    private InventoryService inventoryService;
    private InventoryConsumer inventoryConsumer;

    @BeforeEach
    void setUp() {
        medicineRepository = mock(MedicineRepository.class);
        orderHistoryRepository = mock(OrderHistoryRepository.class);
        inventoryService = new InventoryService(medicineRepository, orderHistoryRepository);
        inventoryConsumer = new InventoryConsumer(inventoryService);
    }

    @Test
    void testProcessOrderSuccess() {
        Medicine medicine = Medicine.builder()
                .id("PARA-500")
                .name("Paracetamol 500mg")
                .stock(100)
                .build();

        when(medicineRepository.findById("PARA-500")).thenReturn(Optional.of(medicine));
        when(medicineRepository.reduceStock("PARA-500", 10)).thenReturn(1);

        OrderEvent event = OrderEvent.builder()
                .orderId("ORD-12345")
                .medicineId("PARA-500")
                .quantity(10)
                .timestamp("2026-10-08T00:00:00")
                .build();

        inventoryService.processOrder(event);

        verify(medicineRepository, times(1)).findById("PARA-500");
        verify(medicineRepository, times(1)).reduceStock("PARA-500", 10);

        ArgumentCaptor<OrderHistory> historyCaptor = ArgumentCaptor.forClass(OrderHistory.class);
        verify(orderHistoryRepository, times(1)).save(historyCaptor.capture());

        OrderHistory savedHistory = historyCaptor.getValue();
        assertEquals("ORD-12345", savedHistory.getOrderId());
        assertEquals("PARA-500", savedHistory.getMedicineId());
        assertEquals(10, savedHistory.getQuantity());
        assertEquals("2026-10-08T00:00:00", savedHistory.getCreatedAt());
    }

    @Test
    void testProcessOrderInsufficientStock() {
        Medicine medicine = Medicine.builder()
                .id("PARA-500")
                .name("Paracetamol 500mg")
                .stock(5)
                .build();

        when(medicineRepository.findById("PARA-500")).thenReturn(Optional.of(medicine));

        OrderEvent event = OrderEvent.builder()
                .orderId("ORD-99999")
                .medicineId("PARA-500")
                .quantity(10)
                .timestamp("2026-10-08T00:00:00")
                .build();

        inventoryService.processOrder(event);

        verify(medicineRepository, times(1)).findById("PARA-500");
        verify(medicineRepository, never()).reduceStock(anyString(), anyInt());
        verify(orderHistoryRepository, never()).save(any());
    }

    @Test
    void testProcessOrderMedicineNotFound() {
        when(medicineRepository.findById("UNKNOWN")).thenReturn(Optional.empty());

        OrderEvent event = OrderEvent.builder()
                .orderId("ORD-00000")
                .medicineId("UNKNOWN")
                .quantity(1)
                .timestamp("2026-10-08T00:00:00")
                .build();

        assertThrows(RuntimeException.class, () -> inventoryService.processOrder(event));

        verify(medicineRepository, times(1)).findById("UNKNOWN");
        verify(medicineRepository, never()).reduceStock(anyString(), anyInt());
        verify(orderHistoryRepository, never()).save(any());
    }

    @Test
    void testConsumerDelegatesToService() {
        Medicine medicine = Medicine.builder()
                .id("PARA-500")
                .name("Paracetamol 500mg")
                .stock(100)
                .build();

        when(medicineRepository.findById("PARA-500")).thenReturn(Optional.of(medicine));
        when(medicineRepository.reduceStock("PARA-500", 5)).thenReturn(1);

        OrderEvent event = OrderEvent.builder()
                .orderId("ORD-CONSUMER-1")
                .medicineId("PARA-500")
                .quantity(5)
                .timestamp("2026-10-08T00:00:00")
                .build();

        inventoryConsumer.consumeOrderEvent(event);

        verify(medicineRepository, times(1)).reduceStock("PARA-500", 5);
        verify(orderHistoryRepository, times(1)).save(any(OrderHistory.class));
    }
}
