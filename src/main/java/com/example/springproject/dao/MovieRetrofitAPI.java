package com.example.springproject.dao;

import com.example.springproject.dtos.MoviesListDTO;
import com.example.springproject.models.Movie;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;

public interface MovieRetrofitAPI {
    @GET("/3/movie/{id}}")
    Call<Movie> getMovies(@Path("id") Integer id, @Query("api_key")String key);

}
