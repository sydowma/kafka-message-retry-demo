package com.magaofei.kafkamessageretrydemo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class KafkaProducerDemo {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerDemo.class);

    private KafkaProducer<String, Bill> kafkaProducer;
    public KafkaProducerDemo() {
        this.kafkaProducer = new KafkaProducer<>(KafkaConfiguration.getKafkaProperties(), new StringSerializer(), new BillSerializer());
    }

    public void sendRandomMessages() {

        while (true) {
            try {
                Thread.sleep(1000);
                send();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private void send() {
        Bill bill = new Bill("Bill", "1", "1", new BigDecimal("2"));
        ProducerRecord<String, Bill> producerRecord = new ProducerRecord<>("test", bill.id(), bill);
        kafkaProducer.send(producerRecord);
    }
}