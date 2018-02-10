package com.tr.kafka;

import org.springframework.stereotype.Component;

@Component
public class Notification {
    private String tweetId;
    private String userId;

    public String getTweetId() {
        return tweetId;
    }

    public void setTweetId(String tweetId) {
        this.tweetId = tweetId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String toString() {
        return "Notifying user - " + userId + " with Tweet -  " + tweetId;
    }
}
