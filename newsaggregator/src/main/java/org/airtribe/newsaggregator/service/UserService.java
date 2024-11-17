package org.airtribe.newsaggregator.service;

import jakarta.transaction.Transactional;
import org.airtribe.newsaggregator.entity.enums.NewsCategory;
import org.airtribe.newsaggregator.repository.PreferenceRepository;
import org.airtribe.newsaggregator.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.airtribe.newsaggregator.entity.*;
import org.springframework.web.server.ResponseStatusException;
import org.airtribe.newsaggregator.dto.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PreferenceRepository preferenceRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User registerUser(UserRegistrationDTO registrationDTO) {

        if (userRepository.existsByEmail(registrationDTO.getEmail())) {
            throw new IllegalArgumentException("Email is already in use.");
        }

        Set<Preference> preferences = new HashSet<>();
        if (registrationDTO.getPreferences() != null) {
            for (String categoryString : registrationDTO.getPreferences()) {
                try {
                    NewsCategory category = NewsCategory.valueOf(categoryString.toUpperCase());
                    Preference preference = preferenceRepository.findByCategory(category)
                            .orElseGet(() -> preferenceRepository.save(new Preference(category))); //saves to db if enum value is not already present in DB
                    preferences.add(preference);
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Invalid preference category: " + categoryString);
                }
            }
        }
        // Create the User entity from DTO
        User user = new User();
        user.setUsername(registrationDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        user.setEmail(registrationDTO.getEmail());

        // Set preferences on the user
        user.setPreferences(preferences);

        // Save the user to the database
        return userRepository.save(user);
    }

    public Set<Preference> assignPreferencesToUser(User user, List<String> preferences) {
        Set<Preference> newPreferences = new HashSet<>();
        for (String categoryName : preferences) {
            Optional<Preference> preference = preferenceRepository.findByCategory(NewsCategory.valueOf(categoryName));
            preference.ifPresent(newPreferences::add);
        }
        // Set the preferences on the user
        user.setPreferences(newPreferences);

        // Save the updated user
        userRepository.save(user);

        return newPreferences;
    }

    public Optional<User> getUserByUserName(String userName)
    {
        return userRepository.findByUsername(userName);
    }
}
