package com.example.springproject.controllers;

import com.example.springproject.dtos.MoviesListDTO;
import com.example.springproject.models.Movie;
import com.example.springproject.services.MovieService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class RestApiMovieController {

    private final String key = System.getenv("API_KEY");
    private final MovieService movieService;

    public RestApiMovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/api/movies/{id}")
    public Movie getMovies(@PathVariable Integer id) throws IOException {
        return movieService.getMovies(key, id);
    }

//    @GetMapping("/api/movies/{item}")
//    public Movie getMovies(@PathVariable Integer item) throws IOException {
//        return movieService.getSpecificMovie(item);
//    }

    @PostMapping("/api/{item}/save")
    public void saveMovies(@PathVariable Integer item) throws IOException {
        movieService.saveMovies(movieService.getMovies(key,item));
    }

}
