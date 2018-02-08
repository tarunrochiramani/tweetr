package com.tr.controller;

import com.tr.utils.Constants;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @RequestMapping(Constants.BASE_PATH)
    public String index() {
        return "Greetings from Spring Boot!";
    }
}