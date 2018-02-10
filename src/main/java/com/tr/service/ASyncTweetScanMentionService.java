package com.tr.service;

import java.util.List;
import java.util.UUID;

import com.tr.kafka.Notification;
import com.tr.kafka.Sender;
import com.tr.model.Tweet;
import com.tr.utils.Constants;
import com.tr.utils.Helper;
import com.tr.utils.InMemoryStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Async
public class ASyncTweetScanMentionService {

    @Autowired private InMemoryStore inMemoryStore;
    @Autowired private Validator validator;
    @Autowired private Helper helper;
    @Autowired private Sender sender;

    public void scanAndAddMentions(UUID tweetId) {
        if (!validator.validateTweetId(tweetId)) {
            return;
        }

        Tweet tweet = (Tweet) inMemoryStore.getBasicTweetMap().get(tweetId);

        List<String> mentionedNames = helper.scanMentions(tweet.getText());
        for(String mention : mentionedNames) {
            if (inMemoryStore.getUserMapByUserName().containsKey(mention)) {
                tweet.getMentions().add(mention);
                Notification notification = new Notification();
                notification.setTweetId(tweetId.toString());
                notification.setUserId(inMemoryStore.getUserMapByUserName().get(mention).getId().toString());
                sender.send(Constants.KAFKA_NOTIFICATION_TOPIC, notification);
            }
        }
    }
}
