package com.example.pharmacyservice;

import com.example.pharmacyservice.controller.OrderController;
import com.example.pharmacyservice.dto.OrderEvent;
import com.example.pharmacyservice.dto.OrderRequest;
import com.example.pharmacyservice.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class PharmacyServiceApplicationTests {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderController orderController;

    @Test
    void testProcessCheckout() {
        OrderRequest request = new OrderRequest("MED-TEST", 5);
        OrderEvent event = orderService.processCheckout(request);
        assertNotNull(event);
        assertEquals("MED-TEST", event.getMedicineId());
        assertEquals(5, event.getQuantity());
    }

    @Test
    void testControllerCheckout() {
        OrderRequest request = new OrderRequest("MED-123", 2);
        ResponseEntity<Map<String, Object>> response = orderController.checkout(request);
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("SUCCESS", response.getBody().get("status"));
    }

}
