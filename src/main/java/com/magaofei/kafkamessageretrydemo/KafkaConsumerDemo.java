package com.magaofei.kafkamessageretrydemo;

import jakarta.annotation.PostConstruct;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

@Component
public class KafkaConsumerDemo {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerDemo.class);

    private KafkaConsumer<String, byte[]> kafkaConsumer;

    public KafkaConsumerDemo() {
        Map<String, Object> properties = KafkaConfiguration.getKafkaProperties(); // Ensure this returns Map<String, Object>
        properties.put("group.id", "test_group");
        this.kafkaConsumer = new KafkaConsumer<>(properties, new StringDeserializer(), new ByteArrayDeserializer());
        kafkaConsumer.subscribe(List.of("test"));
        Executors.newSingleThreadExecutor().execute(() -> {
            while (true) {
                kafkaConsumer.poll(Duration.ofSeconds(500)).forEach(this::consume);
            }
        });
    }

    public void consume(ConsumerRecord<String, byte[]> record) {

            byte[] value = record.value();
            logger.info("Received message: {}", Arrays.toString(record.value()));

    }

}
