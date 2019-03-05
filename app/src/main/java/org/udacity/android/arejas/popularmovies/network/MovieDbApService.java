package org.udacity.android.arejas.popularmovies.network;

import org.udacity.android.arejas.popularmovies.data.MovieDetails;
import org.udacity.android.arejas.popularmovies.data.MovieList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Interface for implementing the Retrofit calls to movie DB API. Functions are not documented because
 * they are self-explanatory
 */
interface MovieDbApService {

    String MOVIEDBAPI_POPULAR_REQUEST_BASE = "movie/popular";
    String MOVIEDBAPI_TOP_RATED_REQUEST_BASE = "movie/top_rated";
    String MOVIEDBAPI_MOVIE_DETAIL_REQUEST_BASE = "movie/{id}";

    @GET(MOVIEDBAPI_POPULAR_REQUEST_BASE)
    Call<MovieList> getMostPopularMovies(@Query("page") int page,
                                         @Query("api_key") String apiKey,
                                         @Query("language") String language);

    @GET(MOVIEDBAPI_TOP_RATED_REQUEST_BASE)
    Call<MovieList> getTopRatedMovies(@Query("page") int page,
                                      @Query("api_key") String apiKey,
                                      @Query("language") String language);

    @GET(MOVIEDBAPI_MOVIE_DETAIL_REQUEST_BASE)
    Call<MovieDetails> getMovieDetails(@Path("id") int movie_id,
                                       @Query("api_key") String apiKey,
                                       @Query("language") String language);

}
