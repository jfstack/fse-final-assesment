package com.jfstack.fse.projtracker.be.controller;

import com.jfstack.fse.projtracker.be.dto.UserForm;
import com.jfstack.fse.projtracker.be.entity.User;
import com.jfstack.fse.projtracker.be.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> addUser(@RequestBody UserForm userForm) {
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

        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{userId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> updateUser(
            @PathVariable("userId") Integer userId,
            @RequestBody UserForm userForm ) {

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

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable("userId") Integer userId) {
        //check if user already exists

        Optional<User> found = this.userService.getUserByEmployeeId(userId);

        if(!found.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        this.userService.deleteUser(found.get().getUserId());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
