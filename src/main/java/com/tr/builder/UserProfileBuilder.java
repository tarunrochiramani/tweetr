package com.tr.builder;

import com.tr.model.User;
import com.tr.model.UserProfile;
import org.apache.commons.lang3.builder.Builder;

public class UserProfileBuilder implements Builder<UserProfile> {
    private UserProfile userProfile = new UserProfile();

    public static UserProfileBuilder anUserProfileBuilder() {
        return new UserProfileBuilder();
    }

    public UserProfileBuilder withUser(User user) {
        userProfile.setId(user.getId());
        userProfile.setFirstName(user.getFirstName());
        userProfile.setLastName(user.getLastName());
        userProfile.setUserName(user.getUserName());
        return this;
    }

    @Override
    public UserProfile build() {
        return userProfile;
    }
}
