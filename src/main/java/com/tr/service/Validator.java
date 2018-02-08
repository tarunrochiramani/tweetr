package com.tr.service;

import java.util.UUID;

import com.tr.utils.InMemoryStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Validator {
    @Autowired private InMemoryStore inMemoryStore;

    public boolean validateUserId(UUID userId) {
        if (!inMemoryStore.getUserMap().containsKey(userId)) {
            return false;
        }

        return true;
    }

    public boolean validateTweetId(UUID tweeId) {
        if (!inMemoryStore.getTweetMap().containsKey(tweeId)) {
            return false;
        }

        return true;
    }
}
