package com.tr.controller;

import java.net.URL;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AbstractControllerTest {

    @LocalServerPort
    protected int port;

    protected URL base;
    protected String baseURL;

    @Before
    public void setUpBase() throws Exception {
        this.base = new URL("http://localhost:" + port + "/rest");
        this.baseURL = "http://localhost:" + port;
    }

    @Autowired
    protected TestRestTemplate template;
}
