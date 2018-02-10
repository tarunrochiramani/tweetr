package com.tr.builder;

import java.util.UUID;

import com.tr.model.ReTweet;
import com.tr.model.Tweet;
import com.tr.model.User;
import org.apache.commons.lang3.builder.Builder;

public class ReTweetBuilder implements Builder<ReTweet> {
    private ReTweet reTweet = new ReTweet();

    public static ReTweetBuilder aReTweetBuilder() {
        return new ReTweetBuilder();
    }

    public ReTweetBuilder withTweet(Tweet tweet) {
        this.reTweet.setOriginalTweet(new Tweet());
        this.reTweet.getOriginalTweet().setId(tweet.getId());
        this.reTweet.getOriginalTweet().setTimestamp(tweet.getTimestamp());
        this.reTweet.getOriginalTweet().setCreatedBy(tweet.getCreatedBy());
        this.reTweet.getOriginalTweet().setText(tweet.getText());
        this.reTweet.getOriginalTweet().setMentions(tweet.getMentions());

        return this;
    }

    public ReTweetBuilder withCreatedBy(User user) {
        reTweet.setCreatedBy(user);
        return this;
    }

    @Override
    public ReTweet build() {
        reTweet.setId(UUID.randomUUID());
        reTweet.setTimestamp(System.currentTimeMillis());
        return reTweet;
    }
}
