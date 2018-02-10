package com.tr.service;

import java.util.List;
import java.util.UUID;

import com.tr.kafka.Notification;
import com.tr.kafka.Sender;
import com.tr.model.BasicTweet;
import com.tr.utils.Constants;
import com.tr.utils.InMemoryStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Async
public class ASyncTweetNotificationService {

    @Autowired private Validator validator;
    @Autowired private InMemoryStore inMemoryStore;
    @Autowired private Sender sender;

    public void sendNotification(UUID tweetId) {
        if (!validator.validateTweetId(tweetId)) {
            return;
        }

        BasicTweet basicTweet = inMemoryStore.getBasicTweetMap().get(tweetId);
        List<UUID> followedByIDs = inMemoryStore.getUserFollowedBy().get(basicTweet.getCreatedBy().getId());
        if (followedByIDs != null) {
            followedByIDs.parallelStream().forEach(uuid -> {
                Notification notification = new Notification();
                notification.setTweetId(basicTweet.getId().toString());
                notification.setUserId(uuid.toString());
                sender.send(Constants.KAFKA_NOTIFICATION_TOPIC, notification);
            });
        }

    }
}
