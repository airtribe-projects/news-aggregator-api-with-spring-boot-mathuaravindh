package org.airtribe.newsaggregator.service;

import org.airtribe.newsaggregator.dto.newsapiresponse.Article;
import org.airtribe.newsaggregator.dto.newsapiresponse.NewsApiResponse;
import org.airtribe.newsaggregator.entity.Preference;
import org.airtribe.newsaggregator.entity.User;
import org.airtribe.newsaggregator.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class NewsService {

    @Autowired
    private UserRepository userRepository;

    @Value("${newsapi.api.key}")
    private String newsApiKey;

    private static final String NEWS_API_URL = "https://newsapi.org/v2/top-headlines?apiKey=%s%s";

    public ResponseEntity<?> fetchNewsByUserPreferences(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);

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
                .map(pref -> "&category=" + pref.getCategory().name().toLowerCase())
                .collect(Collectors.joining());

        String apiUrl = String.format(NEWS_API_URL, newsApiKey, categories);

        // Call the News API
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<NewsApiResponse> newsApiResponse = restTemplate.getForEntity(apiUrl, NewsApiResponse.class);

        if (newsApiResponse.getBody() != null && "ok".equals(newsApiResponse.getBody().getStatus())) {
            List<Article> articles = newsApiResponse.getBody().getArticles();
            return ResponseEntity.ok(articles);
        } else {
            return ResponseEntity.status(newsApiResponse.getStatusCode()).body("Failed to fetch news");
        }
    }
}
