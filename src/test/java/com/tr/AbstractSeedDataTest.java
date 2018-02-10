package com.tr;

import java.util.UUID;

import com.tr.model.User;
import com.tr.model.UserProfile;
import com.tr.utils.Constants;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static com.tr.builder.UserBuilder.anUserBuilder;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class AbstractSeedDataTest {
    protected String baseURL;
    protected HttpHeaders headers;
    static TestRestTemplate template;

    protected static User userA = anUserBuilder().withUserName("tarun").withFirstName("Tarun").withLastName("Rochiramani").build();
    protected static User userB = anUserBuilder().withUserName("ameya").withFirstName("Ameya").withLastName("Vasani").build();

    @BeforeClass
    public static void setupRestTemplate() {
        template = new TestRestTemplate(new RestTemplateBuilder().rootUri("http://localhost:8080").build());
    }

        @Before
    public void setUpBase() throws Exception {
        this.baseURL = "http://localhost:8080";
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }


    protected UUID createUser(User user) {
        ResponseEntity<UserProfile> userResponse = template.postForEntity(baseURL + Constants.USER_TEMPLATE_PATH, user, UserProfile.class);
        validateUser(user, userResponse.getBody());
        return userResponse.getBody().getId();
    }

    protected void validateUser(User expectedUser, User actualUser) {
        assertEquals(expectedUser.getFirstName(), actualUser.getFirstName());
        assertEquals(expectedUser.getLastName(), actualUser.getLastName());
        assertEquals(expectedUser.getUserName(), actualUser.getUserName());
    }
}
