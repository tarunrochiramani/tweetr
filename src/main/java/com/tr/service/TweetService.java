package com.tr.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.UUID;

import com.tr.exception.InvalidInputException;
import com.tr.model.BasicTweet;
import com.tr.model.DetailedTweet;
import com.tr.model.InputTweet;
import com.tr.model.ReTweet;
import com.tr.model.Tweet;
import com.tr.model.User;
import com.tr.utils.InMemoryStore;
import com.tr.utils.TweetTimeStampDescendingOrderComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.tr.builder.DetailedTweetBuilder.aDetailTweetBuilder;
import static com.tr.builder.ReTweetBuilder.aReTweetBuilder;
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
        addTweetToUserTweets(tweet.getCreatedBy(), tweet.getId());

        tweetScanMentionService.scanAndAddMentions(tweet.getId());
        tweetNotificationService.sendNotification(tweet.getId());
        return tweet;
    }

    public ReTweet addReTweet(UUID userId, UUID tweetId) throws InvalidInputException {
        if (!validator.validateUserId(userId)) {
            logger.error("Unable to get user. Invalid User id - " + userId);
            throw new InvalidInputException("Invalid input");
        }

        if (!validator.validateTweetId(tweetId)) {
            logger.error("Unable to get Tweet. Invalid Tweet id - " + tweetId);
            throw new InvalidInputException("Invalid input");
        }

        BasicTweet basicTweet = inMemoryStore.getBasicTweetMap().get(tweetId);
        Tweet originalTweet = null;
        if(basicTweet instanceof ReTweet) {
            originalTweet = ((ReTweet) basicTweet).getOriginalTweet();
        } else {
            originalTweet = (Tweet) basicTweet;
        }

        ReTweet reTweet = aReTweetBuilder().withCreatedBy(inMemoryStore.getUserMap().get(userId)).withTweet(originalTweet).build();
        inMemoryStore.getBasicTweetMap().put(reTweet.getId(), reTweet);
        addTweetToUserTweets(reTweet.getCreatedBy(), reTweet.getId());

        tweetNotificationService.sendNotification(reTweet.getId());
        return reTweet;
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

    public List<BasicTweet> getUserFeed(UUID userId) throws InvalidInputException {
        if (!validator.validateUserId(userId)) {
            logger.error("Unable to get user. Invalid User id - " + userId);
            throw new InvalidInputException("Invalid input");
        }

        List<BasicTweet> tweets = new ArrayList<>();
        Stack<BasicTweet> basicTweets = inMemoryStore.getUserActivity().get(userId);
        while (!basicTweets.empty()) {
            BasicTweet tweet = basicTweets.pop();
            if (tweets.contains(tweet)) {
                continue;
            }
            tweets.add(tweet);
        }

        Collections.sort(tweets, new TweetTimeStampDescendingOrderComparator());
        return tweets;
    }

    private void addTweetToUserTweets(User createdBy, UUID id) {
        List<UUID> tweetIds = inMemoryStore.getUsersTweets().get(createdBy.getId());
        if (tweetIds == null) {
            tweetIds = new ArrayList<>();
            inMemoryStore.getUsersTweets().put(createdBy.getId(), tweetIds);
        }
        tweetIds.add(id);
    }
}
