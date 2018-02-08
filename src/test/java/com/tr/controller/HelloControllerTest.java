package com.tr.controller;

import org.junit.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;

public class HelloControllerTest extends AbstractControllerTest {

    @Test
    public void getHello() {
        ResponseEntity<String> response = template.getForEntity(base.toString(),
                String.class);
        assertEquals("Greetings from Spring Boot!", response.getBody());
    }
}
