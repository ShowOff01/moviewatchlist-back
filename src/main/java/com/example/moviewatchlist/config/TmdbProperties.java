package com.example.moviewatchlist.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "tmdb.api")
public record TmdbProperties(String token) {}
