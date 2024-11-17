package org.airtribe.newsaggregator.service;

import org.airtribe.newsaggregator.entity.Preference;
import org.airtribe.newsaggregator.entity.User;
import org.airtribe.newsaggregator.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class NewsService {

    @Autowired
    private UserRepository userRepository;

    @Value("${newsapi.api.key}")
    private String newsApiKey;

    private static final String NEWS_API_URL = "https://newsapi.org/v2/top-headlines?category=%s&apiKey=%s";

    public ResponseEntity<String> fetchNewsByUserPreferences(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(404).body("User not found");
        }

        User user = userOptional.get();
        Set<Preference> preferences = user.getPreferences();

        if (preferences.isEmpty()) {
            return ResponseEntity.badRequest().body("User has no preferences set");
        }

        // Build the categories query parameter by joining preference categories
        String categories = preferences.stream()
                .map(pref -> pref.getCategory().name().toLowerCase())
                .collect(Collectors.joining(","));

        String apiUrl = String.format(NEWS_API_URL, categories, newsApiKey);

        // Call the News API
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> newsApiResponse = restTemplate.getForEntity(apiUrl, String.class);

        if (newsApiResponse.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok(newsApiResponse.getBody());
        } else {
            return ResponseEntity.status(newsApiResponse.getStatusCode()).body("Failed to fetch news");
        }
    }
}
