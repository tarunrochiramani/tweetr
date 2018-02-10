package com.tr.utils;

import java.util.Comparator;

import com.tr.model.BasicTweet;

public class TweetTimeStampDescendingOrderComparator implements Comparator<BasicTweet> {
    @Override
    public int compare(BasicTweet o1, BasicTweet o2) {
        return o2.getTimestamp().compareTo(o1.getTimestamp());
    }
}
