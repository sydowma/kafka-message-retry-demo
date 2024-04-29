package com.magaofei.kafkamessageretrydemo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreatePartitionsResult;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.admin.NewPartitions;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;

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
        int id = random.nextInt(1000000);
        int userId = random.nextInt(10);
        int botId = random.nextInt(10);
        int amount = random.nextInt(10);
        Bill bill = new Bill(String.valueOf(id), String.valueOf(botId), String.valueOf(userId), new BigDecimal(amount));
        ProducerRecord<String, Bill> producerRecord = new ProducerRecord<>("test", bill.botId(), bill);
        kafkaProducer.send(producerRecord);
    }
}