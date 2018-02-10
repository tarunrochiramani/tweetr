package com.tr.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

@Component
public class Helper {
    public List<String> scanMentions(String text) {
        return getSubStrings(text, "@[a-zA-Z0-9\\._-]+");
    }

    private List<String> getSubStrings(String text, String patternText) {
        List<String> tokens = new ArrayList<>();
        Pattern pattern = Pattern.compile(patternText);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            tokens.add(matcher.group().substring(1));
        }
        return tokens;

    }
}
