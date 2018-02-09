package com.tr.model;

import java.util.ArrayList;
import java.util.List;

public class Tweet extends BasicTweet {
    private String text;
    private List<String> mentions = new ArrayList<>();

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getMentions() {
        return mentions;
    }

    public void setMentions(List<String> mentions) {
        this.mentions = mentions;
    }
}
