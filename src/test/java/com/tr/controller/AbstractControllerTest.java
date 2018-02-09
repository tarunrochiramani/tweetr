package com.tr.controller;

import java.net.URL;

import com.tr.model.User;
import com.tr.model.UserProfile;
import com.tr.utils.Constants;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AbstractControllerTest {

    @LocalServerPort
    protected int port;

    protected URL base;
    protected String baseURL;
    protected HttpHeaders headers;

    @Before
    public void setUpBase() throws Exception {
        this.base = new URL("http://localhost:" + port + "/rest");
        this.baseURL = "http://localhost:" + port;
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @Autowired
    protected TestRestTemplate template;

    protected void createUser(User user) {
        ResponseEntity<UserProfile> userResponse = template.postForEntity(baseURL + Constants.USER_TEMPLATE_PATH, user, UserProfile.class);
        validateUser(user, userResponse.getBody());
        user.setId(userResponse.getBody().getId());
    }

    protected void validateUser(User expectedUser, User actualUser) {
        assertEquals(expectedUser.getFirstName(), actualUser.getFirstName());
        assertEquals(expectedUser.getLastName(), actualUser.getLastName());
        assertEquals(expectedUser.getUserName(), actualUser.getUserName());
    }
}
