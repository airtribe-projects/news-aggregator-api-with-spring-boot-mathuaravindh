package org.airtribe.newsaggregator.repository.DataLoader;

import org.airtribe.newsaggregator.entity.Preference;
import org.airtribe.newsaggregator.entity.enums.NewsCategory;
import org.airtribe.newsaggregator.repository.PreferenceRepository;
import org.airtribe.newsaggregator.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class PreferenceDataLoader implements ApplicationRunner {

    @Autowired
    private PreferenceRepository preferenceRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Insert predefined preferences only if they don't already exist
        for (NewsCategory category : NewsCategory.values()) {
            if (!preferenceRepository.findByCategory(category).isPresent()) {
                preferenceRepository.save(new Preference(category));
                System.out.println("Saved category: " + category);
            }
        }
    }
}
