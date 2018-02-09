package com.tr.controller;

import java.lang.reflect.Type;
import java.util.List;

import com.fasterxml.jackson.databind.type.TypeFactory;
import com.tr.builder.UserBuilder;
import com.tr.model.User;
import com.tr.model.UserProfile;
import com.tr.utils.Constants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.tr.utils.Constants.USERS_PATH;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class UserControllerTest extends AbstractControllerTest {

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
    public void canGetUser() {
        getUser(userA);
        getUser(userB);
    }

    @Test
    public void canGetAllUsers() {
        ParameterizedTypeReference<List<User>> listUsersRef = new ParameterizedTypeReference<List<User>>() {
            @Override
            public Type getType() {
                return TypeFactory.defaultInstance().constructParametricType(List.class, User.class);
            }
        };

        ResponseEntity<List<User>> responseEntity = template.exchange(USERS_PATH, HttpMethod.GET, null, listUsersRef);
        assertNotNull(responseEntity);
        assertNotNull(responseEntity.getBody());
        assertEquals(2, responseEntity.getBody().size());
        assertTrue(responseEntity.getBody().contains(userA));
        assertTrue(responseEntity.getBody().contains(userB));
    }

    @Test
    public void canSetFollow(){
        template.postForObject(baseURL + Constants.USER_FOLLOW, HttpMethod.POST, String.class, userA.getId(), userB.getId());

        validateFollowersAndFollowing(userA, 1, 0);
        validateFollowersAndFollowing(userB, 0, 1);
    }


    @Test
    public void canUnFollow() {
        template.postForObject(baseURL + Constants.USER_FOLLOW, HttpMethod.POST, String.class, userA.getId(), userB.getId());

        template.delete(baseURL + Constants.USER_FOLLOW, userA.getId(), userB.getId());

        validateFollowersAndFollowing(userA, 0, 0);
        validateFollowersAndFollowing(userB, 0, 0);
    }


    @Test
    public void canDeleteUserAndUpdateFollowers() {
        User userC = UserBuilder.anUserBuilder().withUserName("userc").withFirstName("User").withLastName("C").build();
        createUser(userC);
        template.postForObject(baseURL + Constants.USER_FOLLOW, HttpMethod.POST, String.class, userA.getId(), userC.getId());
        template.postForObject(baseURL + Constants.USER_FOLLOW, HttpMethod.POST, String.class, userC.getId(), userB.getId());
        validateFollowersAndFollowing(userA, 1, 0);
        validateFollowersAndFollowing(userB, 0, 1);
        validateFollowersAndFollowing(userC, 1, 1);

        template.delete(baseURL + Constants.USER_PATH, userC.getId());

        ResponseEntity<UserProfile> userProfileResponseEntity = template.getForEntity(baseURL + Constants.USER_PATH, UserProfile.class, userC.getId());
        assertEquals(HttpStatus.BAD_REQUEST, userProfileResponseEntity.getStatusCode());
        validateFollowersAndFollowing(userA, 0, 0);
        validateFollowersAndFollowing(userB, 0, 0);
    }


    private void getUser(User user) {
        ResponseEntity<UserProfile> userProfileResponseEntity = template.getForEntity(baseURL + Constants.USER_PATH, UserProfile.class, user.getId());
        assertNotNull(userProfileResponseEntity);
        assertNotNull(userProfileResponseEntity.getBody());
        assertNotNull(userProfileResponseEntity.getBody().getId());
        validateUser(user, userProfileResponseEntity.getBody());
    }

    private void validateFollowersAndFollowing(User user, int expectedFollowing, int expectedFollowers) {
        ResponseEntity<UserProfile> userProfileResponseEntity = template.getForEntity(baseURL + Constants.USER_PATH, UserProfile.class, user.getId());
        assertEquals(expectedFollowing, userProfileResponseEntity.getBody().getFollowing());
        assertEquals(expectedFollowers, userProfileResponseEntity.getBody().getFollowers());
    }

}
