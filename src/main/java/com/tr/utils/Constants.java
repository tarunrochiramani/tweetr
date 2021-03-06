package com.tr.utils;

public class Constants {

    public static final String BASE_PATH = "/rest";
    public static final String USERS_PATH = BASE_PATH + "/users";
    public static final String USER_PATH = BASE_PATH + "/user/{id}";
    public static final String USER_TEMPLATE_PATH = BASE_PATH + "/user";
    public static final String USER_FOLLOW = BASE_PATH + "/user/{userId}/follow/user/{followId}";

    public static final String CREATE_TWEET = BASE_PATH + "/tweet";
    public static final String TWEET_PATH = BASE_PATH + "/tweet/{id}";
    public static final String RETWEET_PATH = BASE_PATH + "/reTweet";
    public static final String TWEET_COMMENT_PATH = BASE_PATH + "/tweet/{id}/comment";

    public static final String USER_TWEETS = BASE_PATH + "/user/{userId}/tweets";
    public static final String USER_FEEDS = BASE_PATH + "/user/{id}/feed";

    public static final String HEADER_USER_ID_PARAM = "user_id";

    public static final String KAFKA_NOTIFICATION_TOPIC = "notify.t";
    public static final String KAFKA_NOTIFICATION_GROUP = "notify";
}
