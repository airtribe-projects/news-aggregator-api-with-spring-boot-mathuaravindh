package org.airtribe.newsaggregator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
public class NewsaggregatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewsaggregatorApplication.class, args);
	}

}
