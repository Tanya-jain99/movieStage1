package com.example.android.tanya.moviestage1;

import com.example.android.tanya.moviestage1.Model.Movies;
import com.example.android.tanya.moviestage1.Model.Reviews;
import com.example.android.tanya.moviestage1.Model.Trailer;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {
    @GET("popular/")
    Call<Movies> getPopularMovies(@Query ("api_key") String apiKey, @Query("page") int page);

    @GET("top_rated/")
    Call<Movies> getTopRatingMovies(@Query ("api_key") String apiKey, @Query("page") int page);

    //https://api.themoviedb.org/3/movie/{movie_id}/videos?api_key=<<api_key>>&language=en-US
    @GET("{movie_id}/videos")
    Call<Trailer> getTrailer(@Path ("movie_id") String path,@Query ("api_key") String apiKey);

    @GET("{movie_id}/reviews")
    Call<Reviews> getReviews(@Path ("movie_id") String path, @Query ("api_key") String apiKey);

}