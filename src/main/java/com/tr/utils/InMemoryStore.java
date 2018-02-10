package com.tr.utils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.UUID;

import com.tr.model.ActivityFeed;
import com.tr.model.BasicTweet;
import com.tr.model.DetailedTweet;
import com.tr.model.User;
import org.springframework.stereotype.Component;

@Component
public class InMemoryStore {
    private Map<UUID, User> userMap = new LinkedHashMap<>();
    private Map<String, User> userMapByUserName = new HashMap<>();
    private Map<UUID, List<UUID>> userFollowing = new HashMap<>();
    private Map<UUID, List<UUID>> userFollowedBy = new HashMap<>();

    private Map<UUID, List<UUID>> usersTweets = new HashMap<>();
    private Map<UUID, BasicTweet> basicTweetMap = new HashMap<>();
    private Map<UUID, DetailedTweet> detailedTweetMap = new HashMap<>();

    private Map<UUID, Stack<ActivityFeed>> userActivity = new HashMap<>();

    public Map<UUID, User> getUserMap() {
        return userMap;
    }

    public Map<UUID, List<UUID>> getUserFollowing() {
        return userFollowing;
    }

    public Map<UUID, List<UUID>> getUserFollowedBy() {
        return userFollowedBy;
    }

    public Map<UUID, List<UUID>> getUsersTweets() {
        return usersTweets;
    }

    public Map<UUID, BasicTweet> getBasicTweetMap() {
        return basicTweetMap;
    }

    public Map<String, User> getUserMapByUserName() {
        return userMapByUserName;
    }

    public Map<UUID, DetailedTweet> getDetailedTweetMap() {
        return detailedTweetMap;
    }

    public void setDetailedTweetMap(Map<UUID, DetailedTweet> detailedTweetMap) {
        this.detailedTweetMap = detailedTweetMap;
    }

    public Map<UUID, Stack<ActivityFeed>> getUserActivity() {
        return userActivity;
    }
}
