package com.magaofei.kafkamessageretrydemo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class KafkaProducerDemo {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerDemo.class);

    private KafkaProducer<String, Bill> kafkaProducer;

    private static final Random random = new SecureRandom();
    public KafkaProducerDemo() {
        this.kafkaProducer = new KafkaProducer<>(KafkaConfiguration.getKafkaProperties(), new StringSerializer(), new BillSerializer());
        try (AdminClient adminClient = AdminClient.create(KafkaConfiguration.getKafkaProperties())) {
            Map<String, NewPartitions> newPartitions = new HashMap<>();
            newPartitions.put("test", NewPartitions.increaseTo(6));
            CreatePartitionsResult partitions = adminClient.createPartitions(newPartitions);
            NewTopic retry5s = new NewTopic("retry-5s", 6, (short) 1);
            NewTopic retry30s = new NewTopic("retry-30s", 6, (short) 1);
            List<NewTopic> retryTopics = List.of(retry5s, retry30s);
            CreateTopicsResult topics = adminClient.createTopics(retryTopics);
            logger.info("Topics: {}", topics.all().get());
            logger.info("Partitions: {}", partitions.all().get());
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }

    }

    public void sendRandomMessages() {

        while (true) {
            try {
                Thread.sleep(100);
                send();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private void send() {
        int userId = random.nextInt(10);
        int botId = random.nextInt(10);
        int amount = random.nextInt(10);
        Bill bill = new Bill(String.valueOf(System.currentTimeMillis()), String.valueOf(botId), String.valueOf(userId),
                new BigDecimal(amount), 0);
        ProducerRecord<String, Bill> producerRecord = new ProducerRecord<>("test", bill.botId(), bill);
        kafkaProducer.send(producerRecord);
    }

    public void sendRetryMessages(Bill bill) {
        logger.info("Send retry message: {}", bill);
        ProducerRecord<String, Bill> producerRecord = new ProducerRecord<>("retry-5s", bill.botId(), bill);
        kafkaProducer.send(producerRecord);
    }




}