package org.airtribe.newsaggregator.repository;

import org.airtribe.newsaggregator.entity.Preference;
import org.airtribe.newsaggregator.entity.enums.NewsCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PreferenceRepository extends JpaRepository<Preference, Long> {
    Optional<Preference> findByCategory(NewsCategory category);
}
