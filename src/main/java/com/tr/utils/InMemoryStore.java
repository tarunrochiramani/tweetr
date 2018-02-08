package com.tr.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.tr.model.User;
import org.springframework.stereotype.Component;

@Component
public class InMemoryStore {
    private Map<UUID, User> userMap = new HashMap<>();
    private Map<UUID, List<UUID>> userFollowing = new HashMap<>();
    private Map<UUID, List<UUID>> userFollowedBy = new HashMap<>();

    public Map<UUID, User> getUserMap() {
        return userMap;
    }

    public Map<UUID, List<UUID>> getUserFollowing() {
        return userFollowing;
    }

    public Map<UUID, List<UUID>> getUserFollowedBy() {
        return userFollowedBy;
    }
}
