package com.jfstack.fse.projtracker.be.controller;

import com.jfstack.fse.projtracker.be.dto.UserForm;
import com.jfstack.fse.projtracker.be.entity.User;
import com.jfstack.fse.projtracker.be.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

//@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService userService;

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> addUser(@RequestBody UserForm userForm) {
        logger.info("addUser(ENTER)");

        //check if user already exists using employee id
        Optional<User> found = this.userService.getUserByEmployeeId(userForm.getEmployeeId());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        if(found.isPresent()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        User user = new User();
        user.setEmployeeId(userForm.getEmployeeId());
        user.setFirstName(userForm.getFirstName());
        user.setLastName(userForm.getLastName());

        user = this.userService.addUser(user);

        logger.info("addUser(EXIT)");
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> getUsers() {
        logger.info("getUsers(ENTER)");

        Optional<List<User>> allUsers = this.userService.getAllUsers();

        if (!allUsers.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        logger.info("getUsers(EXIT)");
        return new ResponseEntity<>(allUsers.get(), HttpStatus.OK);
    }

    @PutMapping(value = "/{userId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> updateUser(
            @PathVariable("userId") Integer userId,
            @RequestBody UserForm userForm ) {
        logger.info("updateUser(ENTER)");

        //check if user already exists
        Optional<User> found = this.userService.getUserByEmployeeId(userForm.getEmployeeId());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        if(!found.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        User user = found.get();
        user.setFirstName(userForm.getFirstName());
        user.setLastName(userForm.getLastName());

        this.userService.updateUser(user);

        logger.info("updateUser(EXIT)");
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable("userId") Integer userId) {
        logger.info("deleteUser(ENTER)");

        //check if user already exists
        Optional<User> found = this.userService.getUserByEmployeeId(userId);

        if(!found.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        this.userService.deleteUser(found.get().getUserId());

        logger.info("deleteUser(EXIT)");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
