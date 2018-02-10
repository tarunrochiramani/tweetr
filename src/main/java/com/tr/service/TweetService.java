package com.tr.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.UUID;

import com.tr.builder.DetailedTweetBuilder;
import com.tr.builder.TweetBuilder;
import com.tr.exception.InvalidInputException;
import com.tr.model.BasicTweet;
import com.tr.model.DetailedTweet;
import com.tr.model.InputTweet;
import com.tr.model.Tweet;
import com.tr.utils.InMemoryStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.tr.builder.DetailedTweetBuilder.aDetailTweetBuilder;
import static com.tr.builder.TweetBuilder.aTweetBuilder;

@Component
public class TweetService {

    @Autowired private Validator validator;
    @Autowired private InMemoryStore inMemoryStore;
    @Autowired private ASyncTweetScanMentionService tweetScanMentionService;
    @Autowired private ASyncTweetNotificationService tweetNotificationService;
    private static Logger logger = LoggerFactory.getLogger(TweetService.class);


    public Tweet addTweet(String userId, InputTweet inputTweet) throws InvalidInputException {
        UUID userUUID = UUID.fromString(userId);
        if (!validator.validateUserId(userUUID)) {
            logger.error("Unable to get user. Invalid User id - " + userId);
            throw new InvalidInputException("Invalid input");
        }

        Tweet tweet = aTweetBuilder().withInputTweet(inputTweet).withCreatedBy(inMemoryStore.getUserMap().get(userUUID)).build();

        inMemoryStore.getBasicTweetMap().put(tweet.getId(), tweet);
        inMemoryStore.getDetailedTweetMap().put(tweet.getId(), aDetailTweetBuilder().withTweet(tweet).build());
        List<UUID> tweetIds = inMemoryStore.getUsersTweets().get(tweet.getCreatedBy().getId());
        if (tweetIds == null) {
            tweetIds = new Stack<>();
            inMemoryStore.getUsersTweets().put(tweet.getCreatedBy().getId(), tweetIds);
        }
        tweetIds.add(tweet.getId());

        tweetScanMentionService.scanAndAddMentions(tweet.getId());
        tweetNotificationService.sendNotification(tweet.getId());
        return tweet;
    }

    public DetailedTweet getTweet(UUID tweetId) throws InvalidInputException {
        if (!validator.validateTweetId(tweetId)) {
            logger.error("Unable to get Tweet. Invalid Tweet id - " + tweetId);
            throw new InvalidInputException("Invalid input");
        }

        return inMemoryStore.getDetailedTweetMap().get(tweetId);
    }

    public List<BasicTweet> getUserTweets(UUID userId) throws InvalidInputException {
        if (!validator.validateUserId(userId)) {
            logger.error("Unable to get user. Invalid User id - " + userId);
            throw new InvalidInputException("Invalid input");
        }

        List<BasicTweet> tweets = new ArrayList<>();
        List<UUID> tweetIds = inMemoryStore.getUsersTweets().get(userId);


        //TODO: Pagination
        if (tweetIds != null && !tweetIds.isEmpty()) {
            for (int count = tweetIds.size() - 1; count >= 0; count--) {
                tweets.add(inMemoryStore.getBasicTweetMap().get(tweetIds.get(count)));
            }
        }


        return tweets;
    }
}
