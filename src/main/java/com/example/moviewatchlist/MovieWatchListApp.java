package com.example.moviewatchlist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class MovieWatchListApp {

	public static void main(String[] args) {
		SpringApplication.run(MovieWatchListApp.class, args);
	}

}
