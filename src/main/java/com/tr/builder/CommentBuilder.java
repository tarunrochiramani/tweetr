package com.tr.builder;

import com.tr.model.Comment;
import com.tr.model.InputTweet;
import com.tr.model.User;
import javafx.util.Builder;

public class CommentBuilder implements Builder<Comment> {
    private Comment comment = new Comment();

    public static CommentBuilder aCommentBuilder() {
        return new CommentBuilder();
    }

    public CommentBuilder withInputTweet(InputTweet inputTweet) {
        this.comment.setComment(inputTweet.getText());
        return this;
    }

    public CommentBuilder withCommentingUser(User user) {
        this.comment.setCommentingUser(user);
        return this;
    }

    @Override
    public Comment build() {
        comment.setTimestamp(System.currentTimeMillis());
        return comment;
    }
}
