package com.sagar.chincholkar.retrofit2.Retrofit;

import com.sagar.chincholkar.retrofit2.Retrofit.GetMovieTrailorFromId.GetMovieTrailer;
import com.sagar.chincholkar.retrofit2.Retrofit.GetUpcomingMovies.GetUpcomingMovies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIInterface {
    enum SortBy {
        RELEASE_DATE_ASCENDING("release_date.asc"),
        RELEASE_DATE_DESCENDING("release_date.desc");

        String value;

        SortBy(String value) {
            this.value = value;
        }


        @Override
        public String toString() {
            return this.value;
        }
    }
    //*** Upcoming Movies Api ***//
    @GET("movie/upcoming")
    Call<GetUpcomingMovies> getUpcomingMovies(@Query("api_key") String apiKey,@Query("sort_by") SortBy sortBy ,@Query("page") int page);

    //*** Upcoming Movies Video Api ***//
    @GET("movie/{movie_id}/videos")
    Call<GetMovieTrailer> getUpcomingMoviesVideos(@Path("movie_id") int movieId, @Query("api_key") String apiKey);

    @GET("movie/popular")
    Call<GetUpcomingMovies> getPopularMovies(@Query("api_key") String apiKey,@Query("sort_by") SortBy sortBy ,@Query("page") int page);
    @GET("movie/now_playing")
    Call<GetUpcomingMovies> getNowPlayingMovies(@Query("api_key") String apiKey,@Query("sort_by") SortBy sortBy ,@Query("page") int page);
}
