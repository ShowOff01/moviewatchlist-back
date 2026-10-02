package com.example.moviewatchlist.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import com.example.moviewatchlist.config.TmdbProperties;
import com.example.moviewatchlist.dto.TmdbMovieDto;
import com.example.moviewatchlist.dto.TmdbSearchResponse;

@Service 
public class TmdbService {
    
    private final RestClient restClient;

    public TmdbService(TmdbProperties properties) {
        this.restClient = RestClient.builder()
                .baseUrl("https://api.themoviedb.org/3")
                .defaultHeader("Authorization", "Bearer " + properties.token())
                .build();
    }

    public List<TmdbMovieDto> searchMovies(String query) {
        TmdbSearchResponse response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/search/movie")
                    .queryParam("query", query)
                    .queryParam("language", "fr-FR")
                    .build())
                .retrieve()
                .body(TmdbSearchResponse.class);
        
        if (response == null || response.results() == null) {
            return List.of();
        }

        return response.results().stream()
                .filter(movie -> movie.posterPath() != null)
                .toList(); 
    }

    public Optional<TmdbMovieDto> getMovie(Long tmdbId) {
        try {
            TmdbMovieDto movie = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/movie/{id}")
                            .queryParam("language", "fr-FR")
                            .build(tmdbId))
                    .retrieve()
                    .body(TmdbMovieDto.class);
            return Optional.ofNullable(movie);
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        }
    }
}