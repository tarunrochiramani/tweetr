package com.tr.builder;

import java.util.UUID;

import com.tr.model.User;
import org.apache.commons.lang3.builder.Builder;

public class UserBuilder implements Builder<User> {

    private User user = new User();

    public static UserBuilder anUserBuilder() {
        return new UserBuilder();
    }

    public UserBuilder withUserName(String userName) {
        user.setUserName(userName);
        return this;
    }

    public UserBuilder withFirstName(String firstName) {
        user.setFirstName(firstName);
        return this;
    }

    public UserBuilder withLastName(String lastName) {
        user.setLastName(lastName);
        return this;
    }

    @Override
    public User build() {
        return user;
    }
}
