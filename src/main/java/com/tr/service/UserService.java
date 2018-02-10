package com.tr.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.UUID;

import com.tr.exception.InvalidInputException;
import com.tr.model.User;
import com.tr.model.UserProfile;
import com.tr.utils.InMemoryStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.tr.builder.UserProfileBuilder.anUserProfileBuilder;

@Component
public class UserService {

    @Autowired private InMemoryStore inMemoryStore;
    @Autowired private Validator validator;

    private static Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserProfile addUser(User user) {
        user.setId(UUID.randomUUID());
        inMemoryStore.getUserMap().put(user.getId(), user);
        inMemoryStore.getUserMapByUserName().put(user.getUserName(), user);
        inMemoryStore.getUserActivity().put(user.getId(), new Stack<>());
        return anUserProfileBuilder().withUser(user).build();
    }

    public UserProfile getUser(UUID userId) throws InvalidInputException {
        if (!validator.validateUserId(userId)) {
            logger.error("Unable to get user. Invalid User id - " + userId);
            throw new InvalidInputException("Invalid user id");
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
        if (!validator.validateUserId(userId) || !validator.validateUserId(followId)) {
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
        if (!validator.validateUserId(userId) || !validator.validateUserId(followId)) {
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
        if (!validator.validateUserId(userId)) {
            return false;
        }

        inMemoryStore.getUserMap().remove(userId);

        List<UUID> followingUUIDS = inMemoryStore.getUserFollowing().get(userId);
        if (followingUUIDS != null) {
            followingUUIDS.forEach(uuid -> {
                inMemoryStore.getUserFollowedBy().get(uuid).remove(userId);
            });
        }
        inMemoryStore.getUserFollowing().remove(userId);


        List<UUID> followedByUUIDS = inMemoryStore.getUserFollowedBy().get(userId);
        if (followedByUUIDS != null) {
            followedByUUIDS.forEach(uuid -> {
                inMemoryStore.getUserFollowing().get(uuid).remove(userId);
            });
        }
        inMemoryStore.getUserFollowedBy().remove(userId);


        return true;
    }

    public List<UserProfile> getAllUsers() {
        Set<UUID> userIds = inMemoryStore.getUserMap().keySet();
        List<UserProfile> users = new ArrayList<>();
        userIds.forEach(uuid -> {
            try {
                users.add(getUser(uuid));
            } catch (InvalidInputException e) {
                logger.error("Error loading users", e);
            }
        });

        return users;
    }
}
