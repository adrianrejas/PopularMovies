package org.udacity.android.arejas.popularmovies.data.network.service;

import org.udacity.android.arejas.popularmovies.data.network.model.MovieCreditsRestApi;
import org.udacity.android.arejas.popularmovies.data.network.model.MovieDetailsRestApi;
import org.udacity.android.arejas.popularmovies.data.network.model.MovieListRestApi;
import org.udacity.android.arejas.popularmovies.data.network.model.MovieReviewListRestApi;
import org.udacity.android.arejas.popularmovies.data.network.model.MovieVideoListRestApi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Interface for implementing the Retrofit calls to movie DB API. Functions are not documented because
 * they are self-explanatory
 */
public interface MovieDbApService {

    String MOVIEDBAPI_POPULAR_REQUEST_BASE = "movie/popular";
    String MOVIEDBAPI_TOP_RATED_REQUEST_BASE = "movie/top_rated";
    String MOVIEDBAPI_SEARCH_REQUEST_BASE = "search/movie";
    String MOVIEDBAPI_MOVIE_DETAIL_REQUEST_BASE = "movie/{id}";
    String MOVIEDBAPI_MOVIE_REVIEWS_REQUEST_BASE = "movie/{id}/reviews";
    String MOVIEDBAPI_MOVIE_CREDITS_REQUEST_BASE = "movie/{id}/credits";
    String MOVIEDBAPI_MOVIE_VIDEOS_REQUEST_BASE = "movie/{id}/videos";

    @GET(MOVIEDBAPI_POPULAR_REQUEST_BASE)
    Call<MovieListRestApi> getMostPopularMovies(@Query("page") int page,
                                                @Query("api_key") String apiKey,
                                                @Query("language") String language);

    @GET(MOVIEDBAPI_TOP_RATED_REQUEST_BASE)
    Call<MovieListRestApi> getTopRatedMovies(@Query("page") int page,
                                             @Query("api_key") String apiKey,
                                             @Query("language") String language);

    @GET(MOVIEDBAPI_SEARCH_REQUEST_BASE)
    Call<MovieListRestApi> searchMovies(@Query("page") int page,
                                        @Query("api_key") String apiKey,
                                        @Query("language") String language,
                                        @Query("query") String query);

    @GET(MOVIEDBAPI_MOVIE_DETAIL_REQUEST_BASE)
    Call<MovieDetailsRestApi> getMovieDetails(@Path("id") int movie_id,
                                              @Query("api_key") String apiKey,
                                              @Query("language") String language);

    @GET(MOVIEDBAPI_MOVIE_REVIEWS_REQUEST_BASE)
    Call<MovieReviewListRestApi> getMovieReviewList(@Path("id") int movie_id,
                                                    @Query("api_key") String apiKey,
                                                    @Query("language") String language);

    @GET(MOVIEDBAPI_MOVIE_CREDITS_REQUEST_BASE)
    Call<MovieCreditsRestApi> getMovieCredits(@Path("id") int movie_id,
                                              @Query("api_key") String apiKey);

    @GET(MOVIEDBAPI_MOVIE_VIDEOS_REQUEST_BASE)
    Call<MovieVideoListRestApi> getMovieVideoList(@Path("id") int movie_id,
                                                  @Query("api_key") String apiKey,
                                                  @Query("language") String language);

}
