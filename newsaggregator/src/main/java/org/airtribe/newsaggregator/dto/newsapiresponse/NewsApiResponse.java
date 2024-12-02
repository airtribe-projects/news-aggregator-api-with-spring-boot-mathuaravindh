package org.airtribe.newsaggregator.dto.newsapiresponse;

import lombok.Data;

import java.util.List;

@Data
public class NewsApiResponse {
    private String status;
    private int totalResults;
    private List<Article> articles;
}

