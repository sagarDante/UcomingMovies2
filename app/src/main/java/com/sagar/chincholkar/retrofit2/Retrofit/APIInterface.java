package com.sagar.chincholkar.retrofit2.Retrofit;

import com.sagar.chincholkar.retrofit2.Retrofit.GetUpcomingMovies.GetUpcomingMovies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIInterface {
    @GET("movie/upcoming")
    Call<GetUpcomingMovies> getUpcomingMovies(@Query("api_key") String apiKey);
    @GET("movie/{movie_id}/videos")
    Call<GetUpcomingMovies> getUpcomingMoviesVideos(@Path("movie_id") int movieId, @Query("api_key") String apiKey);
}
