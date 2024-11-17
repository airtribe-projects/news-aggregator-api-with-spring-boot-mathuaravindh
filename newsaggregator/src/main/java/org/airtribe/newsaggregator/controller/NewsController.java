package org.airtribe.newsaggregator.controller;

import org.airtribe.newsaggregator.entity.Preference;
import org.airtribe.newsaggregator.entity.User;
import org.airtribe.newsaggregator.service.NewsService;
import org.airtribe.newsaggregator.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class NewsController {

    @Autowired
    private UserService userService;

    @Autowired
    private NewsService newsService;

    @Value("${newsapi.api.key}")
    private String newsApiKey;

    private static final String NEWS_API_URL = "https://newsapi.org/v2/top-headlines?category=%s&apiKey=%s";

    @GetMapping("api/news")
    public ResponseEntity<?> getNewsBasedOnPreferences() {
        // Get the current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            return ResponseEntity.status(401).body("User not authenticated");
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<User> userOptional = userService.getUserByUserName(userDetails.getUsername());

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(404).body("User not found");
        }

        User user = userOptional.get();

        return ResponseEntity.ok(newsService.fetchNewsByUserPreferences(user.getEmail()));

    }
}