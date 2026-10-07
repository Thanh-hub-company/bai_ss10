package com.example.pharmacyservice.config;

import com.example.pharmacyservice.dto.OrderEvent;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers:localhost:9094}")
    private String bootstrapServers;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(org.apache.kafka.clients.admin.AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        KafkaAdmin kafkaAdmin = new KafkaAdmin(configs);
        kafkaAdmin.setFatalIfBrokerNotAvailable(false);
        kafkaAdmin.setAutoCreate(true);
        return kafkaAdmin;
    }

    @Bean
    public NewTopic medicineStockEventsTopic() {
        return TopicBuilder.name("medicine-stock-events")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic medicinePriceUpdatesTopic() {
        return TopicBuilder.name("medicine-price-updates")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic pharmacyNotificationsTopic() {
        return TopicBuilder.name("pharmacy-notifications")
                .partitions(2)
                .replicas(1)
                .build();
    }

    @Bean
    public ProducerFactory<String, OrderEvent> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();

        // Địa chỉ kết nối Kafka Cluster
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        // Cấu hình Serializer cho Message Key (chuyển String medicineId thành Byte)
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        // Cấu hình Serializer cho Message Value (chuyển OrderEvent thành JSON)
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        // Đảm bảo không gắn header type cố định của Java class vào JSON payload (tương thích đa nền tảng)
        configProps.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);

        // Cấu hình retry và timeout để tránh treo luồng khi Kafka broker gặp sự cố
        configProps.put(ProducerConfig.RETRIES_CONFIG, 3);
        configProps.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 5000);
        configProps.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 5000);

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    /**
     * Bean KafkaTemplate cung cấp phương thức gửi tin nhắn lên Broker.
     * Key: String (medicineId)
     * Value: OrderEvent (Object)
     */
    @Bean
    public KafkaTemplate<String, OrderEvent> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}