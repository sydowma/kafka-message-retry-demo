package com.magaofei.kafkamessageretrydemo;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class KafkaRetryConsumer {
    private final KafkaConsumer<String, Bill> kafkaConsumer;
    private final ScheduledExecutorService executor;
    private final ExecutorService executors = Executors.newFixedThreadPool(1);

    public KafkaRetryConsumer() {
        Map<String, Object> props = KafkaConfiguration.getKafkaProperties();
        props.put("group.id", "test_group_retry");

        this.kafkaConsumer = new KafkaConsumer<>(props, new StringDeserializer(), new BillDeserializer());
        this.executor = Executors.newScheduledThreadPool(10);

        // Subscribe to retry topics
        kafkaConsumer.subscribe(Arrays.asList("retry-5s", "retry-30s", "retry-60s"));

        executors.execute(this::consumeRetry);

    }

    private long extractDelayTimeFromTopicName(String topicName) {
        // Assumes topic names are formatted as 'retry-5s', 'retry-30s', etc.
        return Long.parseLong(topicName.split("-")[1].replace("s", "")) * 1000;
    }

    public void consumeRetry() {
        try {
            while (true) {
                ConsumerRecords<String, Bill> records = kafkaConsumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, Bill> record : records) {
                    long delayTime = extractDelayTimeFromTopicName(record.topic());
                    long timeSinceProduced = System.currentTimeMillis() - record.timestamp();
                    long delayToApply = delayTime - timeSinceProduced;

                    if (delayToApply > 0) {
                        executor.schedule(() -> processBill(record.value()), delayToApply, TimeUnit.MILLISECONDS);
                    } else {
                        processBill(record.value());
                    }
                }
            }
        } finally {
            kafkaConsumer.close();
        }
    }

    private void processBill(Bill bill) {
        // Process the bill here, possibly attempting to reprocess it
        System.out.println("Processing bill: " + bill);
        // Implement the actual business logic here
    }
}

