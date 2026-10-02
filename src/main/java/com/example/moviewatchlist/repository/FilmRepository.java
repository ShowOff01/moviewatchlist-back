package com.example.moviewatchlist.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.moviewatchlist.entity.Film;

public interface FilmRepository extends JpaRepository<Film, Long> {
    List<Film> findByTitleContainingIgnoreCase(String title);
    boolean existsByTmdbId(Long tmdbId);
}