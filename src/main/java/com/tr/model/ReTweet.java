package com.tr.model;

public final class ReTweet extends AbstractTweet {
    private Tweet originalTweet;

    public Tweet getOriginalTweet() {
        return originalTweet;
    }

    public void setOriginalTweet(Tweet originalTweet) {
        this.originalTweet = originalTweet;
    }
}
