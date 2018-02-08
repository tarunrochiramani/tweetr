package com.tr.model;

import org.springframework.stereotype.Component;

@Component
public class UserProfile extends User {
    private int following = 0;
    private int followers = 0;

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }
}
