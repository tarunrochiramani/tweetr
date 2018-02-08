package com.tr.builder;

import java.util.UUID;

import com.tr.model.InputTweet;
import com.tr.model.Tweet;
import com.tr.model.User;
import org.apache.commons.lang3.builder.Builder;

public class TweetBuilder implements Builder<Tweet> {
    private Tweet tweet = new Tweet();

    public static TweetBuilder aTweetBuilder() {
        return new TweetBuilder();
    }

    public TweetBuilder withInputTweet(InputTweet inputTweet) {
        tweet.setText(inputTweet.getText());
        return this;
    }

    public TweetBuilder withCreatedBy(User user) {
        tweet.setCreatedBy(user);
        return this;
    }

    @Override
    public Tweet build() {
        tweet.setId(UUID.randomUUID());
        tweet.setTimestamp(System.currentTimeMillis());
        return tweet;
    }
}
