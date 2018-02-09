package com.tr.controller;

import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.type.TypeFactory;
import com.tr.builder.UserBuilder;
import com.tr.model.InputTweet;
import com.tr.model.Tweet;
import com.tr.model.User;
import com.tr.utils.Constants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.tr.utils.Constants.HEADER_USER_ID_PARAM;
import static com.tr.utils.Constants.TWEET_PATH;
import static com.tr.utils.Constants.USER_TWEETS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TweetControllerTest extends AbstractControllerTest {

    private User userA = UserBuilder.anUserBuilder().withUserName("tarun").withFirstName("Tarun").withLastName("Rochiramani").build();
    private User userB = UserBuilder.anUserBuilder().withUserName("ameya").withFirstName("Ameya").withLastName("Vasani").build();


    @Before
    public void setUp() {
        createUser(userA);
        createUser(userB);
    }

    @After
    public void cleanUp() {
        template.delete(baseURL + Constants.USER_PATH, userA.getId());
        template.delete(baseURL + Constants.USER_PATH, userB.getId());
    }

    @Test
    public void canCreateTweet() {

        ResponseEntity<Tweet> tweetResponseEntity = postTweet("Hello lets Tweet", userA.getId());

        assertNotNull(tweetResponseEntity);
        assertEquals(HttpStatus.OK, tweetResponseEntity.getStatusCode());
        assertNotNull(tweetResponseEntity.getBody());
        assertNotNull(tweetResponseEntity.getBody().getId());
        assertNotNull(tweetResponseEntity.getBody().getText());
        assertNotNull(tweetResponseEntity.getBody().getCreatedBy());
        assertNotNull(tweetResponseEntity.getBody().getTimestamp());
        assertNotNull(tweetResponseEntity.getBody().getMentions());
        assertTrue(tweetResponseEntity.getBody().getMentions().isEmpty());
    }

    @Test
    public void canGetATweet() {
        Tweet createdTweet = postTweet("Hello lets Tweet", userA.getId()).getBody();

        ResponseEntity<Tweet> retrievedTweetResponse = template.getForEntity(TWEET_PATH, Tweet.class, createdTweet.getId());
        assertEquals(HttpStatus.OK, retrievedTweetResponse.getStatusCode());
        assertNotNull(retrievedTweetResponse.getBody());
        assertEquals(createdTweet.getId(), retrievedTweetResponse.getBody().getId());
        assertEquals(createdTweet.getText(), retrievedTweetResponse.getBody().getText());
        assertEquals(createdTweet.getCreatedBy(), retrievedTweetResponse.getBody().getCreatedBy());
        assertEquals(createdTweet.getTimestamp(), retrievedTweetResponse.getBody().getTimestamp());
        assertEquals(createdTweet.getMentions().size(), retrievedTweetResponse.getBody().getMentions().size());
    }


    @Test
    public void canGetAUsersTweets() {
        postTweet("Hello lets Tweet", userA.getId());
        postTweet("Hello this is the second tweet", userA.getId());
        ParameterizedTypeReference<List<Tweet>> listTweetsRef = new ParameterizedTypeReference<List<Tweet>>() {
            @Override
            public Type getType() {
                return TypeFactory.defaultInstance().constructParametricType(List.class, Tweet.class);
            }
        };

        ResponseEntity<List<Tweet>> responseEntity = template.exchange(USER_TWEETS, HttpMethod.GET, null, listTweetsRef, userA.getId());
        assertNotNull(responseEntity);
        assertEquals("Hello this is the second tweet", responseEntity.getBody().get(0).getText());
        assertEquals("Hello lets Tweet", responseEntity.getBody().get(1).getText());

    }

    private ResponseEntity<Tweet> postTweet(String text, UUID userId) {
        InputTweet inputTweet = new InputTweet();
        inputTweet.setText(text);
        if (headers.containsKey(HEADER_USER_ID_PARAM)) {
            headers.remove(HEADER_USER_ID_PARAM);
        }
        headers.add(HEADER_USER_ID_PARAM, userId.toString());
        HttpEntity<InputTweet> request = new HttpEntity<>(inputTweet, headers);
        return template.postForEntity(Constants.CREATE_TWEET, request, Tweet.class);
    }
}
