package com.example.springproject.services;

import com.example.springproject.dao.MovieRetrofitAPI;
import com.example.springproject.dao.RetrofitClientInstance;
import com.example.springproject.dtos.MoviesListDTO;
import com.example.springproject.models.Movie;
import com.example.springproject.repositories.MovieRepository;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.List;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private Retrofit retrofit;
    private MovieRetrofitAPI movieRetrofitAPI;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
        retrofit = RetrofitClientInstance.getRetrofitInstance();
        movieRetrofitAPI = retrofit.create(MovieRetrofitAPI.class);
    }

    public Movie getMovies(String key, Integer id) throws IOException {
        Movie movieResponse = new Movie();
        Call<Movie> response = movieRetrofitAPI.getMovies(id, key);

        Response<Movie> execute = response.execute();
        if (execute.isSuccessful() && execute.body() != null) {
            movieResponse = execute.body();
        }
        return movieResponse;
    }

//    public Movie getSpecificMovie(Integer id, Integer item) throws IOException {
//        MoviesListDTO movieResponse = new MoviesListDTO();
//        Call<MoviesListDTO> response = movieRetrofitAPI.getMovies(id);
//
//        Response<MoviesListDTO> execute = response.execute();
//        if (execute.isSuccessful() && execute.body() != null) {
//            movieResponse = execute.body();
//        }
//        return movieResponse.getResults().get(item);
//    }

    public void saveMovies(Movie movie){
        movieRepository.save(movie);
    }

}
