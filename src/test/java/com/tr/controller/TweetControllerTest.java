package com.tr.controller;

import com.tr.builder.UserBuilder;
import com.tr.model.InputTweet;
import com.tr.model.Tweet;
import com.tr.model.User;
import com.tr.utils.Constants;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.tr.utils.Constants.HEADER_USER_ID_PARAM;
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
        InputTweet inputTweet = new InputTweet();
        inputTweet.setText("Hello lets Tweet");
        headers.add(HEADER_USER_ID_PARAM, userA.getId().toString());
        HttpEntity<InputTweet> request = new HttpEntity<>(inputTweet, headers);

        ResponseEntity<Tweet> tweetResponseEntity = template.postForEntity(Constants.CREATE_TWEET, request, Tweet.class);

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
}
