package com.tr.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.tr.exception.InvalidInputException;
import com.tr.model.User;
import com.tr.model.UserProfile;
import com.tr.utils.InMemoryStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.tr.builder.UserProfileBuilder.anUserProfileBuilder;

@Component
public class UserService {

    @Autowired
    private InMemoryStore inMemoryStore;

    public UserProfile addUser(User user) {
        user.setId(UUID.randomUUID());
        inMemoryStore.getUserMap().put(user.getId(), user);
        return anUserProfileBuilder().withUser(user).build();
    }

    public UserProfile getUser(UUID userId) throws InvalidInputException {
        if (!validateUserId(userId)) {
            throw new InvalidInputException("Invalid user i`d");
        }
        User user = inMemoryStore.getUserMap().get(userId);
        UserProfile userProfile = anUserProfileBuilder().withUser(user).build();

        if (inMemoryStore.getUserFollowedBy().containsKey(userId)) {
            userProfile.setFollowers(inMemoryStore.getUserFollowedBy().get(userId).size());
        }

        if (inMemoryStore.getUserFollowing().containsKey(userId)) {
            userProfile.setFollowing(inMemoryStore.getUserFollowing().get(userId).size());
        }

        return userProfile;
    }

    public boolean addFollows(UUID userId, UUID followId) {
        if (!validateUserId(userId) || !validateUserId(followId)) {
            return false;
        }

        List<UUID> followingUUIDS = inMemoryStore.getUserFollowing().get(userId);
        if (followingUUIDS == null) {
            followingUUIDS = new ArrayList<>();
            inMemoryStore.getUserFollowing().put(userId, followingUUIDS);
        }
        followingUUIDS.add(followId);

        followingUUIDS = inMemoryStore.getUserFollowedBy().get(followId);
        if (followingUUIDS == null) {
            followingUUIDS = new ArrayList<>();
            inMemoryStore.getUserFollowedBy().put(followId, followingUUIDS);
        }
        followingUUIDS.add(userId);

        return true;
    }

    public boolean removeFollows(UUID userId, UUID followId) {
        if (!validateUserId(userId) || !validateUserId(followId)) {
            return false;
        }

        boolean removedFollows = false;
        List<UUID> followingUUIDS = inMemoryStore.getUserFollowing().get(userId);
        if (followingUUIDS != null) {
            removedFollows = followingUUIDS.remove(followId);
        }


        boolean removedFollower = false;
        followingUUIDS = inMemoryStore.getUserFollowedBy().get(followId);
        if (followingUUIDS != null) {
            removedFollower = followingUUIDS.remove(userId);
        }

        return removedFollows && removedFollower;
    }

    public boolean removeUser(UUID userId) {
        if (!validateUserId(userId)) {
            return false;
        }

        inMemoryStore.getUserMap().remove(userId);

        List<UUID> followingUUIDS = inMemoryStore.getUserFollowing().get(userId);
        followingUUIDS.forEach(uuid -> {
            inMemoryStore.getUserFollowedBy().get(uuid).remove(userId);
        });
        inMemoryStore.getUserFollowing().remove(userId);

        List<UUID> followedByUUIDS = inMemoryStore.getUserFollowedBy().get(userId);
        followedByUUIDS.forEach(uuid -> {
            inMemoryStore.getUserFollowing().get(uuid).remove(userId);
        });
        inMemoryStore.getUserFollowedBy().remove(userId);

        return true;
    }

    private boolean validateUserId(UUID userId) {
        if (!inMemoryStore.getUserMap().containsKey(userId)) {
            return false;
        }

        return true;
    }
}
