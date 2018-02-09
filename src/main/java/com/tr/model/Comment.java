package com.tr.model;

public class Comment {
    private User commentingUser;
    private String comment;

    public User getCommentingUser() {
        return commentingUser;
    }

    public void setCommentingUser(User commentingUser) {
        this.commentingUser = commentingUser;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
