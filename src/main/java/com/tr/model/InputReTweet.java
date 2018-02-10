package com.tr.model;

import org.springframework.stereotype.Component;

@Component
public class InputReTweet {
    private String tweetId;

    public String getTweetId() {
        return tweetId;
    }

    public void setTweetId(String tweetId) {
        this.tweetId = tweetId;
    }
}
