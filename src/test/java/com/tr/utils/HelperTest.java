package com.tr.utils;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class HelperTest {
    private Helper helper = new Helper();

    @Test
    public void canScanMentionsWithASimpleUserName() {
        List<String> strings = helper.scanMentions("Hello @tarun and @something");

        assertNotNull(strings);
        assertFalse(strings.isEmpty());
        assertEquals(2, strings.size());
        assertEquals("tarun", strings.get(0));
        assertEquals("something", strings.get(1));
    }


    @Test
    public void canScanMentionsWithASimpleUserNameWithNumber() {
        List<String> strings = helper.scanMentions("Hello @tarun_5 and @something.5 and @someone-thing_1");

        assertNotNull(strings);
        assertFalse(strings.isEmpty());
        assertEquals(3, strings.size());
        assertEquals("tarun_5", strings.get(0));
        assertEquals("something.5", strings.get(1));
        assertEquals("someone-thing_1", strings.get(2));

    }

    @Test
    public void canScanMentionsWithNoMentions() {
        List<String> strings = helper.scanMentions("Hello there is not mentions in this text but alpha1number and user_name-hyphen.");

        assertNotNull(strings);
        assertTrue(strings.isEmpty());
    }
}

