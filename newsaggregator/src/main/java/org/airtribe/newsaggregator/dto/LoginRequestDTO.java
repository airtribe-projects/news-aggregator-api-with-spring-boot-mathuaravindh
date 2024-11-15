package org.airtribe.newsaggregator.dto;

public class LoginRequestDTO {

    private String email;
    private String password;

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setUsername(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
