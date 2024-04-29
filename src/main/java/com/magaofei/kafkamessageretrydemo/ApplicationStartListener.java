package com.magaofei.kafkamessageretrydemo;

import com.esotericsoftware.kryo.Kryo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartListener implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationStartListener.class);

    private final KafkaProducerDemo kafkaProducerDemo;

    public ApplicationStartListener(KafkaProducerDemo kafkaProducerDemo) {
        this.kafkaProducerDemo = kafkaProducerDemo;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

        logger.info("Application started");

        this.kafkaProducerDemo.sendRandomMessages();

    }
}
