package org.airtribe.newsaggregator.dto;

import java.util.List;

public class PreferencesRequestDTO {
    private List<String> preferences;

    public List<String> getPreferences() { return preferences; }
    public void setPreferences(List<String> preferences) { this.preferences = preferences; }
}
