package com.tr.kafka;



import com.tr.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class Receiver {
    private static Logger logger = LoggerFactory.getLogger(Receiver.class);

    @KafkaListener(topics = Constants.KAFKA_NOTIFICATION_TOPIC, group = Constants.KAFKA_NOTIFICATION_GROUP)
    public void listen(Notification notification) {
        logger.info("Received Messasge in group foo: " + notification);
    }
}
