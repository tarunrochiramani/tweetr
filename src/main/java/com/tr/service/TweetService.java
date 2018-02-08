package com.tr.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.tr.builder.TweetBuilder;
import com.tr.exception.InvalidInputException;
import com.tr.model.InputTweet;
import com.tr.model.Tweet;
import com.tr.utils.InMemoryStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TweetService {

    @Autowired private Validator validator;
    @Autowired private InMemoryStore inMemoryStore;
    @Autowired private ASyncTweetScanMentionService tweetScanMentionService;
    private static Logger logger = LoggerFactory.getLogger(TweetService.class);


    public Tweet addTweet(String userId, InputTweet inputTweet) throws InvalidInputException {
        UUID userUUID = UUID.fromString(userId);
        if (!validator.validateUserId(userUUID)) {
            logger.error("Unable to get user. Invalid User id - " + userId);
            throw new InvalidInputException("Invalid input");
        }

        Tweet tweet = TweetBuilder.aTweetBuilder().withInputTweet(inputTweet).withCreatedBy(inMemoryStore.getUserMap().get(userUUID)).build();

        inMemoryStore.getTweetMap().put(tweet.getId(), tweet);
        List<UUID> tweetIds = inMemoryStore.getUsersTweets().get(tweet.getCreatedBy().getId());
        if (tweetIds == null) {
            tweetIds = new ArrayList<>();
            inMemoryStore.getUsersTweets().put(tweet.getCreatedBy().getId(), tweetIds);
        }
        tweetIds.add(tweet.getId());

        tweetScanMentionService.scanAndAddMentions(tweet.getId());
        return tweet;
    }
}
