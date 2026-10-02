package com.example.moviewatchlist.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TmdbSearchResponse(List<TmdbMovieDto> results) {}