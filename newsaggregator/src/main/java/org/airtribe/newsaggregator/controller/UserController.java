package org.airtribe.newsaggregator.controller;

import jakarta.validation.Valid;
import org.airtribe.newsaggregator.dto.UserRegistrationDTO;
import org.airtribe.newsaggregator.entity.User;
import org.airtribe.newsaggregator.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/api/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody UserRegistrationDTO registrationDTO) {
        User createdUser = userService.registerUser(registrationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }
}
