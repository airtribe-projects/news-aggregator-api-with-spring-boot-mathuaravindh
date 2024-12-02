package org.airtribe.newsaggregator.entity;

import jakarta.persistence.*;
import org.airtribe.newsaggregator.entity.enums.NewsCategory;

import java.util.Set;
import java.util.HashSet;

@Entity
public class Preference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private NewsCategory category;

    // Default constructor for JPA
    public Preference() {
    }

    // Constructor to create a preference with a category
    public Preference(NewsCategory category) {
        this.category = category;
    }

    // Getter for id
    public Long getId() {
        return id;
    }

    // Setter for id
    public void setId(Long id) {
        this.id = id;
    }

    // Getter for category
    public NewsCategory getCategory() {
        return category;
    }

    // Setter for category
    public void setCategory(NewsCategory category) {
        this.category = category;
    }
}
