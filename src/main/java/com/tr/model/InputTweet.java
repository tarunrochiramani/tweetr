package com.tr.model;

import org.springframework.stereotype.Component;

@Component
public class InputTweet {
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
