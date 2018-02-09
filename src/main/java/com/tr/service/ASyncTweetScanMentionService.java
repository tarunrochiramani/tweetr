package com.tr.service;

import java.util.List;
import java.util.UUID;

import com.tr.model.Tweet;
import com.tr.utils.Helper;
import com.tr.utils.InMemoryStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Async
public class ASyncTweetScanMentionService {

    @Autowired private InMemoryStore inMemoryStore;
    @Autowired private Validator validator;
    @Autowired private Helper helper;

    public void scanAndAddMentions(UUID tweetId) {
        if (!validator.validateTweetId(tweetId)) {
            return;
        }

        Tweet tweet = (Tweet) inMemoryStore.getBasicTweetMap().get(tweetId);

        List<String> strings = helper.scanMentions(tweet.getText());
        if (!strings.isEmpty()) {
            tweet.getMentions().addAll(strings);
        }
    }
}
