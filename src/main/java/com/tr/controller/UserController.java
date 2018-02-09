package com.tr.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.tr.exception.InvalidInputException;
import com.tr.model.User;
import com.tr.model.UserProfile;
import com.tr.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static com.tr.utils.Constants.USERS_PATH;
import static com.tr.utils.Constants.USER_FOLLOW;
import static com.tr.utils.Constants.USER_PATH;
import static com.tr.utils.Constants.USER_TEMPLATE_PATH;

@RestController
public class UserController {
    @Autowired private UserService userService;
    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(path = USER_PATH, method = RequestMethod.GET)
    public ResponseEntity<UserProfile> getUser(@PathVariable String id) {
        UserProfile user = null;
        try {
            user = userService.getUser(UUID.fromString(id));
        } catch (InvalidInputException e) {
            logger.error("Invalid input." , e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @RequestMapping(path = USER_TEMPLATE_PATH, method = RequestMethod.POST)
    public ResponseEntity<UserProfile> createUser(@RequestBody User user) {
        UserProfile createProfile = userService.addUser(user);
        return new ResponseEntity<>(createProfile, HttpStatus.OK);
    }

    @RequestMapping(path = USER_TEMPLATE_PATH, method = RequestMethod.GET)
    public ResponseEntity<User> userTemplate() {
        return new ResponseEntity<>(new User(), HttpStatus.OK);
    }

    @RequestMapping(path = USER_PATH, method = RequestMethod.DELETE)
    public ResponseEntity removeUser(@PathVariable String id) {
        boolean success = userService.removeUser(UUID.fromString(id));
        if (!success) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(path = USER_FOLLOW, method = RequestMethod.POST)
    public ResponseEntity<String> followUser(@PathVariable String userId, @PathVariable String followId) {
        boolean success = userService.addFollows(UUID.fromString(userId), UUID.fromString(followId));
        if (!success) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(HttpStatus.OK.toString(), HttpStatus.OK);
    }

    @RequestMapping(path = USER_FOLLOW, method = RequestMethod.DELETE)
    public ResponseEntity removeFollows(@PathVariable String userId, @PathVariable String followId) {
        boolean success = userService.removeFollows(UUID.fromString(userId), UUID.fromString(followId));
        if (!success) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(path = USERS_PATH, method = RequestMethod.GET)
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = new ArrayList<>();
        users.addAll(userService.getAllUsers());
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

}
