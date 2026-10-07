package com.example.pharmacysystem.service;

import com.example.pharmacysystem.dto.OrderEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    @Autowired(required = false)
    private JavaMailSender mailSender;

    public void sendInvoiceEmail(OrderEvent orderEvent) {
        log.info("📧 [Email Service] Đang gửi email hóa đơn cho đơn hàng #{}, mã thuốc: {}, số lượng: {}",
                orderEvent.getOrderId(), orderEvent.getMedicineId(), orderEvent.getQuantity());

        if (mailSender != null && orderEvent.getCustomerEmail() != null && !orderEvent.getCustomerEmail().isBlank()) {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(orderEvent.getCustomerEmail());
                message.setSubject("Hóa đơn đơn hàng #" + orderEvent.getOrderId());
                message.setText("Cảm ơn bạn đã mua thuốc!\nChi tiết đơn hàng:\n- Mã thuốc: "
                        + orderEvent.getMedicineId() + "\n- Số lượng: " + orderEvent.getQuantity()
                        + "\n- Thời gian: " + orderEvent.getTimestamp());
                mailSender.send(message);
                log.info("✅ [Email Service] Gửi email thành công đến: {}", orderEvent.getCustomerEmail());
            } catch (Exception e) {
                log.error("❌ [Email Service] Gửi email thất bại: {}", e.getMessage());
            }
        } else {
            log.info("ℹ️ [Email Service] Mô phỏng gửi email thành công cho đơn hàng #{}", orderEvent.getOrderId());
        }
    }
}