package com.tr.kafka;

import java.util.UUID;

import com.tr.model.BasicTweet;
import com.tr.utils.Constants;
import com.tr.utils.InMemoryStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class Receiver {
    private static Logger logger = LoggerFactory.getLogger(Receiver.class);

    @Autowired InMemoryStore inMemoryStore;

    @KafkaListener(topics = Constants.KAFKA_NOTIFICATION_TOPIC, group = Constants.KAFKA_NOTIFICATION_GROUP)
    public void listen(Notification notification) {
        logger.debug("Received Messasge in group foo: " + notification);
        BasicTweet basicTweet = inMemoryStore.getBasicTweetMap().get(UUID.fromString(notification.getTweetId()));
        inMemoryStore.getUserActivity().get(UUID.fromString(notification.getUserId())).push(basicTweet);
    }
}
