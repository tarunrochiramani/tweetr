package com.tr.controller;

import com.tr.exception.InvalidInputException;
import com.tr.model.InputTweet;
import com.tr.model.Tweet;
import com.tr.service.TweetService;
import com.tr.utils.Constants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static com.tr.utils.Constants.CREATE_TWEET;
import static com.tr.utils.Constants.HEADER_USER_ID_PARAM;


@RestController
public class TweetController {

    @Autowired private TweetService tweetService;
    private static Logger logger = LoggerFactory.getLogger(TweetController.class);

    @RequestMapping(path = CREATE_TWEET, method = RequestMethod.POST,
            produces= MediaType.APPLICATION_JSON_UTF8_VALUE, consumes=MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Tweet> getUser(@RequestHeader(value = HEADER_USER_ID_PARAM) String userid, @RequestBody InputTweet aTweet) {
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
}
