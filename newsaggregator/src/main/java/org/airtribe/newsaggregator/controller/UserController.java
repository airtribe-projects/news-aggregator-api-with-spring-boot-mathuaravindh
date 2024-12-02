package org.airtribe.newsaggregator.controller;

import jakarta.validation.Valid;
import org.airtribe.newsaggregator.dto.PreferencesRequestDTO;
import org.airtribe.newsaggregator.dto.UserRegistrationDTO;
import org.airtribe.newsaggregator.entity.Preference;
import org.airtribe.newsaggregator.entity.User;
import org.airtribe.newsaggregator.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@RestController
class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/api/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody UserRegistrationDTO registrationDTO) {
        User createdUser = userService.registerUser(registrationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping("/api/preferences")
    public ResponseEntity<Set<Preference>> getUserPreferences() {
        // Get the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Find the user by username
        User user = userService.getUserByUserName(username).orElseThrow(
                () -> new RuntimeException("User not found")
        );

        // Get the preferences of the user
        Set<Preference> preferences = user.getPreferences();

        // Return the preferences
        return ResponseEntity.ok(preferences);
    }

    @PutMapping("/api/preferences")
    public ResponseEntity<Set<Preference>> assignUserPreferences(@RequestBody PreferencesRequestDTO preferencesRequestDTO) {
        // Get the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Find the user by username
        User user = userService.getUserByUserName(username).orElseThrow(
                () -> new RuntimeException("User not found")
        );

        Set<Preference> newPreferences = userService.assignPreferencesToUser(user, preferencesRequestDTO.getPreferences());

        // Return the preferences
        return ResponseEntity.ok(newPreferences);
    }
}
