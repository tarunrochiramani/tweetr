package com.tr.builder;

import com.tr.model.DetailedTweet;
import com.tr.model.Tweet;
import org.apache.commons.lang3.builder.Builder;

public class DetailedTweetBuilder implements Builder<DetailedTweet> {
    private DetailedTweet tweet = new DetailedTweet();

    public static DetailedTweetBuilder aDetailTweetBuilder() {
        return new DetailedTweetBuilder();
    }

    public DetailedTweetBuilder withTweet(Tweet tweet) {
        this.tweet.setId(tweet.getId());
        this.tweet.setCreatedBy(tweet.getCreatedBy());
        this.tweet.setTimestamp(tweet.getTimestamp());
        this.tweet.setMentions(tweet.getMentions());
        this.tweet.setText(tweet.getText());
        return this;
    }


    @Override
    public DetailedTweet build() {
        return tweet;
    }
}
