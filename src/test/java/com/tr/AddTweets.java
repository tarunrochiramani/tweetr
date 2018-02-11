package com.tr;

import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.type.TypeFactory;
import com.tr.model.InputReTweet;
import com.tr.model.InputTweet;
import com.tr.model.ReTweet;
import com.tr.model.Tweet;
import com.tr.utils.Constants;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static com.tr.utils.Constants.HEADER_USER_ID_PARAM;
import static com.tr.utils.Constants.USER_TWEETS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AddTweets extends AbstractSeedDataTest {

    @Test
    public void addTweetsForUserA() {
        for (int count=1; count<10; count++) {
            Tweet tweet = postTweet("Hello this is Tweet - " + count + " from " + userA.getUserName(), UUID.nameUUIDFromBytes(userA.getUserName().getBytes())).getBody();
            assertNotNull(tweet);
            assertNotNull(tweet.getId());
        }
    }

    @Test
    public void addTweetsForUserB() {
        ParameterizedTypeReference<List<Tweet>> listTweetsRef = new ParameterizedTypeReference<List<Tweet>>() {
            @Override
            public Type getType() {
                return TypeFactory.defaultInstance().constructParametricType(List.class, Tweet.class);
            }
        };

        ResponseEntity<List<Tweet>> responseEntity = template.exchange(USER_TWEETS, HttpMethod.GET, null, listTweetsRef, UUID.nameUUIDFromBytes(userA.getUserName().getBytes()));
        assertNotNull(responseEntity.getBody());
        assertFalse(responseEntity.getBody().isEmpty());
        for (Tweet tweet : responseEntity.getBody()) {
            ReTweet reTweet = postReTweet(tweet, UUID.nameUUIDFromBytes(userB.getUserName().getBytes())).getBody();
            assertNotNull(reTweet);
            assertNotEquals(tweet.getId(), reTweet.getId());
            assertEquals(tweet.getId(), reTweet.getOriginalTweet().getId());
        }
    }


    @Test
    public void addTweetsWithMentionsForUserA() {
        for (int count=1; count<10; count++) {
            Tweet tweet = postTweet("Hello this is Tweet - " + count + " from " + userA.getUserName() + " for @user-" + count, UUID.nameUUIDFromBytes(userA.getUserName().getBytes())).getBody();
            assertNotNull(tweet);
            assertNotNull(tweet.getId());
        }
    }

    @Test
    public void addTweetsWithMentionsForUserB() {
        for (int count=1; count<10; count++) {
            Tweet tweet = postTweet("Hello this is Tweet - " + count + " from " + userB.getUserName() + " for @user-" + count, UUID.nameUUIDFromBytes(userB.getUserName().getBytes())).getBody();
            assertNotNull(tweet);
            assertNotNull(tweet.getId());
        }
    }


    private ResponseEntity<Tweet> postTweet(String text, UUID userId) {
        InputTweet inputTweet = new InputTweet();
        inputTweet.setText(text);
        addUserIdHeader(userId);
        HttpEntity<InputTweet> request = new HttpEntity<>(inputTweet, headers);
        return template.postForEntity(Constants.CREATE_TWEET, request, Tweet.class);
    }

    private ResponseEntity<ReTweet> postReTweet(Tweet tweet, UUID userId) {
        InputReTweet inputReTweet = new InputReTweet();
        inputReTweet.setTweetId(tweet.getId().toString());
        addUserIdHeader(userId);
        HttpEntity<InputReTweet> request = new HttpEntity<>(inputReTweet, headers);
        return template.postForEntity(Constants.RETWEET_PATH, request, ReTweet.class);
    }

    private void addUserIdHeader(UUID userId) {
        if (headers.containsKey(HEADER_USER_ID_PARAM)) {
            headers.remove(HEADER_USER_ID_PARAM);
        }
        headers.add(HEADER_USER_ID_PARAM, userId.toString());
    }
}
