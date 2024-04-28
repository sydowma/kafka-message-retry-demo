package com.magaofei.kafkamessageretrydemo;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class KafkaConfiguration {

//    @Bean
//    public KafkaProducer<String, byte[]> kafkaProducerDemo() {
//        Map<String, Object> properties = getKafkaProperties();
//        return new KafkaProducer<>(properties);
//    }

    public static Map<String, Object> getKafkaProperties() {
        Map<String, Object> properties = new HashMap<>();
//        properties.put("bootstrap.servers", System.getenv("SPRING_KAFKA_BOOTSTRAP_SERVERS"));
        properties.put("bootstrap.servers", "kafka:9092");
        return properties;
    }
}
