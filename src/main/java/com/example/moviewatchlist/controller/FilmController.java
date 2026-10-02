package com.example.moviewatchlist.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.moviewatchlist.entity.Film;
import com.example.moviewatchlist.repository.FilmRepository;
import com.example.moviewatchlist.service.TmdbService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/api/films")
public class FilmController {

    private final FilmRepository filmRepository;
    private final TmdbService tmdbService;

    public FilmController(FilmRepository filmRepository, TmdbService tmdbService) {
        this.filmRepository = filmRepository;
        this.tmdbService = tmdbService;
    }

    @GetMapping
    public List<Film> getAll(@RequestParam(required = false) String search) {
        if (search != null && !search.isBlank()) {
            return filmRepository.findByTitleContainingIgnoreCase(search);
        }
        return filmRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Film> getById(@PathVariable Long id) {
        return filmRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film create(@Valid @RequestBody Film film) {
        film.setId(null);
        return filmRepository.save(film);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Film> update(@PathVariable Long id, @Valid @RequestBody Film film) {
        if (!filmRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        film.setId(id);
        return ResponseEntity.ok(filmRepository.save(film));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!filmRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        filmRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/from-tmdb/{tmdbId}")
    public ResponseEntity<Film> addFromTmdb(@PathVariable Long tmdbId) {
        if (filmRepository.existsByTmdbId(tmdbId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        return tmdbService.getMovie(tmdbId)
                .map(movie -> {
                    Film film = new Film();
                    film.setTmdbId(movie.id());
                    film.setTitle(movie.title());
                    film.setOverview(movie.overview());
                    film.setPosterPath(movie.posterPath());
                    if (movie.releaseDate() != null && movie.releaseDate().length() >= 4) {
                        film.setReleaseYear(Integer.parseInt(movie.releaseDate().substring(0, 4)));
                    }
                    Film saved = filmRepository.save(film);
                    return ResponseEntity.status(HttpStatus.CREATED).body(saved);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
