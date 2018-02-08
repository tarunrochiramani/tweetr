package com.tr.model;

import java.sql.Timestamp;
import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class Tweet {
    private UUID id;
    private User createdBy;
    private Timestamp createdDate;
    private String text;
    private String[] mentions;

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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String[] getMentions() {
        return mentions;
    }

    public void setMentions(String[] mentions) {
        this.mentions = mentions;
    }
}
