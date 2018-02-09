package com.tr.model;

import java.util.ArrayList;
import java.util.List;

public final class DetailedTweet extends Tweet {
    private List<Comment> comments = new ArrayList<>();

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
