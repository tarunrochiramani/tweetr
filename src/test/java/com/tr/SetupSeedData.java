package com.tr;

import java.util.UUID;

import com.tr.model.User;
import com.tr.utils.Constants;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.http.HttpMethod;

import static com.tr.builder.UserBuilder.anUserBuilder;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SetupSeedData extends AbstractSeedDataTest {

    @Test
    public void createAllUsers() {
        userA.setId(createUser(userA));
        userB.setId(createUser(userB));
    }

    @Test
    public void createUserFollowingRelation() {
        template.postForObject(baseURL + Constants.USER_FOLLOW, HttpMethod.POST, String.class, userA.getId(), userB.getId());
    }

    @Test
    public void createRandomUsers() {
        User previousUser = null;
        for(int i=1; i<=10000; i++) {
            User randomUser = anUserBuilder().withUserName("user-" + i).withFirstName("user_first_" + i).withLastName("user_last_" + i).build();
            UUID randomUserId = createUser(randomUser);

            randomUser.setId(randomUserId);
            if (i%2 == 0) {
                template.postForObject(baseURL + Constants.USER_FOLLOW, HttpMethod.POST, String.class, randomUserId, userB.getId());
            }

            template.postForObject(baseURL + Constants.USER_FOLLOW, HttpMethod.POST, String.class, randomUserId, userA.getId());

            if (previousUser != null) {
                template.postForObject(baseURL + Constants.USER_FOLLOW, HttpMethod.POST, String.class, randomUserId, previousUser.getId());
            }

            previousUser = randomUser;
        }
    }
}
