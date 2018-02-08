package com.tr.model;

import java.sql.Timestamp;
import java.util.UUID;

public class ReTweet {
    private UUID id;
    private User createdBy;
    private Timestamp createdDate;
    private Tweet originalTweet;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public Tweet getOriginalTweet() {
        return originalTweet;
    }

    public void setOriginalTweet(Tweet originalTweet) {
        this.originalTweet = originalTweet;
    }
}
