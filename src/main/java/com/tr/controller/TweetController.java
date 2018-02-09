package com.tr.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.tr.exception.InvalidInputException;
import com.tr.model.AbstractTweet;
import com.tr.model.InputTweet;
import com.tr.model.Tweet;
import com.tr.service.TweetService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static com.tr.utils.Constants.CREATE_TWEET;
import static com.tr.utils.Constants.HEADER_USER_ID_PARAM;
import static com.tr.utils.Constants.TWEET_PATH;
import static com.tr.utils.Constants.USER_TWEETS;


@RestController
public class TweetController {

    @Autowired private TweetService tweetService;
    private static Logger logger = LoggerFactory.getLogger(TweetController.class);

    @RequestMapping(path = CREATE_TWEET, method = RequestMethod.POST,
            produces= MediaType.APPLICATION_JSON_UTF8_VALUE, consumes=MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Tweet> createTweet(@RequestHeader(value = HEADER_USER_ID_PARAM) String userid, @RequestBody InputTweet aTweet) {
        if (StringUtils.isBlank(userid)) {
            logger.error("Header does not contain User Id. Invalid Header send to create a Tweet.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Tweet createdTweet = null;
        try {
            createdTweet = tweetService.addTweet(userid, aTweet);
        } catch (InvalidInputException e) {
            logger.error("Unable to add Tweet." , e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(createdTweet, HttpStatus.OK);
    }

    @RequestMapping(path = TWEET_PATH, method = RequestMethod.GET)
    public ResponseEntity<AbstractTweet> getTweet(@PathVariable String id) {
        AbstractTweet tweet = null;
        try {
            tweet = tweetService.getTweet(UUID.fromString(id));
        } catch (InvalidInputException e) {
            logger.error("Unable to get Tweet. Invalid id - " + id);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(tweet, HttpStatus.OK);
    }

    @RequestMapping(path = USER_TWEETS, method = RequestMethod.GET)
    public ResponseEntity<List<AbstractTweet>> getUserTweets(@PathVariable String userId) {
        List<AbstractTweet> tweets = new ArrayList<>();
        try {
            tweets.addAll(tweetService.getUserTweets(UUID.fromString(userId)));
        } catch (InvalidInputException e) {
            logger.error("Unable to get Tweets for the given user. Invalid id - " + userId);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }


        return new ResponseEntity<>(tweets, HttpStatus.OK);
    }

}
