package com.example.moviewatchlist.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.moviewatchlist.dto.TmdbMovieDto;
import com.example.moviewatchlist.service.TmdbService;

@RestController
@RequestMapping("/api/tmdb")
public class TmdbController {

    private final TmdbService tmdbService;

    public TmdbController(TmdbService tmdbService) {
        this.tmdbService = tmdbService;
    }

    @GetMapping("/search")
    public List<TmdbMovieDto> search(@RequestParam String query) {
        return tmdbService.searchMovies(query);
    }
}
