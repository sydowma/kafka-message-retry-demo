package com.magaofei.kafkamessageretrydemo;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartListener implements ApplicationListener<ApplicationReadyEvent> {
    private final KafkaProducerDemo kafkaProducerDemo;

    public ApplicationStartListener(KafkaProducerDemo kafkaProducerDemo) {
        this.kafkaProducerDemo = kafkaProducerDemo;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

        this.kafkaProducerDemo.sendRandomMessages();
    }
}
