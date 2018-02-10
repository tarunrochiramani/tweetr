package com.tr.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class Sender {
    private static final Logger logger = LoggerFactory.getLogger(Sender.class);

    @Autowired
    private KafkaTemplate<String, Notification> kafkaTemplate;

    public void send(String topic, Notification notification) {
        logger.info("sending payload='{}' to topic='{}'", notification, topic);
        kafkaTemplate.sendDefault(notification);
    }
}

