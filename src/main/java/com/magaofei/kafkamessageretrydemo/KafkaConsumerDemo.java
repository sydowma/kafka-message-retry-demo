package com.magaofei.kafkamessageretrydemo;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

@Component
public class KafkaConsumerDemo {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerDemo.class);

    private KafkaConsumer<String, Bill>[] kafkaConsumers = new KafkaConsumer[6];
    private KafkaConsumer<String, Bill> retryKafkaConsumer;
    ExecutorService consumerExecutorService = Executors.newFixedThreadPool(6);

    private BillToQueue bill = new BillToQueue();

    ExecutorService executorService = Executors.newFixedThreadPool(1);

    private Random random = new SecureRandom();

    private final KafkaProducerDemo kafkaProducerDemo;

    public KafkaConsumerDemo(KafkaProducerDemo kafkaProducerDemo) {
        this.kafkaProducerDemo = kafkaProducerDemo;
        Map<String, Object> properties = KafkaConfiguration.getKafkaProperties(); // Ensure this returns Map<String, Object>
        retryKafkaConsumer = new KafkaConsumer<>(properties, new StringDeserializer(), new BillDeserializer());

        properties.put("group.id", "test_group");
        for (int i = 0; i < 6; i++) {
            this.kafkaConsumers[i] = new KafkaConsumer<>(properties, new StringDeserializer(), new BillDeserializer());
            kafkaConsumers[i].subscribe(List.of("test"));
        }

        for (int i = 0; i < 6; i++) {
            int finalI = i;
            consumerExecutorService.submit(() -> {
                while (true) {
                    kafkaConsumers[finalI].poll(Duration.ofSeconds(500)).forEach(item -> consume(item, finalI));
                }
            });
        }

        executorService.execute(consumeBill());
    }

    private Runnable consumeBill() {
        return () -> {
            while (true) {
                Bill bill = this.bill.getLast();
                if (bill != null) {

                    boolean apiResult = this.callApi(bill);
                    if (!apiResult) {
                        logger.info("Retry: {}", bill);

                        bill.setRetryTimes(bill.retryTimes() + 1);
                        this.bill.replace(bill);
                        // delay queue retry
                        this.kafkaProducerDemo.sendRetryMessages(bill);

                    } else {
                        this.bill.removeLast(bill);
                    }

                    logger.info("Bill: {}", bill);
                }
            }
        };
    }

    private boolean callApi(Bill bill) {
        LockSupport.parkNanos(Duration.ofMillis(50).toNanos());


        // retry logic
        int randomRetry = random.nextInt(10);
        return randomRetry != 0;
    }

    public void consume(ConsumerRecord<String, Bill> record, int consumeIndex) {

        Bill value = record.value();

        bill.addToFirst(value);
        // split by bot id

        logger.info("Received message: {}, consumeIndex = {}", value, consumeIndex);
    }




}
