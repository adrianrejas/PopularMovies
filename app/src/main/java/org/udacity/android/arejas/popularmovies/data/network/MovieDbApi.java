package org.udacity.android.arejas.popularmovies.data.network;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.support.annotation.NonNull;

import org.udacity.android.arejas.popularmovies.data.network.model.MovieCreditsRestApi;
import org.udacity.android.arejas.popularmovies.data.network.model.MovieDetailsRestApi;
import org.udacity.android.arejas.popularmovies.data.network.model.MovieElementRestApi;
import org.udacity.android.arejas.popularmovies.data.network.model.MovieListRestApi;
import org.udacity.android.arejas.popularmovies.data.network.model.MovieReviewListRestApi;
import org.udacity.android.arejas.popularmovies.data.network.model.MovieVideoListRestApi;
import org.udacity.android.arejas.popularmovies.data.network.service.MovieDbApService;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Class with methods to be invoked for invoking background requests to movieDB API.
 */
@Singleton
public class MovieDbApi {

    /**
     * Enumerator of possible sizes for image
     */
    public enum ImageSize {
        W92,
        W154,
        W185,
        W342,
        W500,
        W780,
        ORIGINAL;

        /**
         * Function used for creating the URI for an image
         *
         * @param size
         * @return
         */
        static String getImageSizeString(ImageSize size) {
            switch (size) {
                case W92:
                    return "w92";
                case W154:
                    return "w154";
                case W185:
                    return "w185";
                case W342:
                    return "w342";
                case W500:
                    return "w500";
                case W780:
                    return "w780";
                case ORIGINAL:
                    return "original";
            }
            return "original";
        }

    }

    public static final int RESULTS_PER_PAGE = 20;

    private static final String TAG = "MovieAPI";

    private static final String MOVIEDBAPI_IMAGE_BASE = "http://image.tmdb.org/t/p/";

    private static final String MOVIEDBAPI_BASE = "http://api.themoviedb.org/3/";

    private static final String API_KEY = "94adaaddf36766bcd826206447b409ec";

    private static Retrofit retrofit;
    private static MovieDbApService service;

    /**
     * Constructor.
     */
    @Inject
    public MovieDbApi () {
        initMovieDbApi();
    }

    /**
     * It initiates the static service to be used for calling movieDB API
     * through retrofit library. The idea is to call this function at the beginning of the
     * application or before every call if for any reason the service is not instantiated.
     */
    private void initMovieDbApi () {
        retrofit = new Retrofit.Builder()
                .baseUrl(MOVIEDBAPI_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(MovieDbApService.class);
    }

    /**
     * This method does a network callback and return a live data object associated with.
     *
     * @param callToExecute Call object to execute
     * @param resultCallback Callback in case of success or failure
     *
     *
     */
    private <T extends MovieElementRestApi> void makeCall(
            Call<T> callToExecute, final MovieDbApiCallback<T> resultCallback) {
        callToExecute.enqueue(new Callback<T>() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
                switch (response.code()) {
                    case 200:
                        resultCallback.onSuccess(response.body());
                        break;
                    default:
                        resultCallback.onFailure(new IOException(
                                String.format("Error at executing request with code %d", response.code())));
                        break;
                }
            }

            @Override
            public void onFailure(@NonNull Call<T> call, @NonNull Throwable exception) {
                resultCallback.onFailure(exception);
            }
        });
    }

    /**
     * This method does a request about the list of most popular movies
     *
     * @param page Page number to request.
     * @param language Language to use for the request.
     * @param resultCallback Callback in case of success or failure.
     *
     */
    public void requestMostPopularMovies(
            int page, String language, final MovieDbApiCallback<MovieListRestApi> resultCallback) {
        checkRetrofitService();
        makeCall(service.getMostPopularMovies(page, API_KEY, language), resultCallback);
    }

    /**
     * Get the call request about the list of most popular movies.
     * @param page Page number to request.
     * @param language Language to use for the request.
     * @return Network call to execute.
     *
     */
    public Call<MovieListRestApi> getMostPopularMoviesCall(
            int page, String language) {
        checkRetrofitService();
        return service.getMostPopularMovies(page, API_KEY, language);
    }

    /**
     * This method does a request about the list of top rated movies
     *
     * @param page Page number to request.
     * @param language Language to use for the request.
     * @param resultCallback Callback in case of success or failure.
     *
     */
    public void requestTopRatedMovies(
            int page, String language, final MovieDbApiCallback<MovieListRestApi> resultCallback) {
        checkRetrofitService();
        makeCall(service.getTopRatedMovies(page, API_KEY, language), resultCallback);
    }

    /**
     * Get the call request about the list of top rated movies.
     * @param page Page number to request.
     * @param language Language to use for the request.
     * @return Network call to execute.
     *
     */
    public Call<MovieListRestApi> getTopRatedMoviesCall(
            int page, String language) {
        checkRetrofitService();
        return service.getTopRatedMovies(page, API_KEY, language);
    }

    /**
     * This method does a movie search request based on a string
     *
     * @param page Page number to request.
     * @param language Language to use for the request.
     * @param query Strint to use for searching.
     * @param resultCallback Callback in case of success or failure.
     *
     */
    public void requestMovieSearch(
            int page, String language, String query,
            final MovieDbApiCallback<MovieListRestApi> resultCallback) {
        checkRetrofitService();
        makeCall(service.searchMovies(page, API_KEY, language, query), resultCallback);
    }

    /**
     * Get the call request for movie search request based on a string.
     * @param page Page number to request.
     * @param language Language to use for the request.
     * @param query Strint to use for searching.
     * @return Network call to execute.
     *
     */
    public Call<MovieListRestApi> getMovieSearchCall(
            int page, String language, String query) {
        checkRetrofitService();
        return service.searchMovies(page, API_KEY, language, query);
    }

    /**
     * This method does a request about the details of a movie in particular.
     *
     * @param id ID of the movie to be requested.
     * @param language Language to use for the request.
     * @param resultCallback Callback in case of success or failure.
     *
     */
    public void requestMovieDetails(
            int id, String language, final MovieDbApiCallback<MovieDetailsRestApi> resultCallback) {
        checkRetrofitService();
        makeCall(service.getMovieDetails(id, API_KEY, language), resultCallback);
    }

    /**
     * This method does a request about the details of a movie in particular.
     *  @param id ID of the movie to be requested.
     * @param language Language to use for the request.
     * @return Network call to execute.
     *
     */
    public Call<MovieDetailsRestApi> getMovieDetailsCall(
            int id, String language) {
        checkRetrofitService();
        return service.getMovieDetails(id, API_KEY, language);
    }

    /**
     * This method does a request about the credits of a movie in particular.
     *
     * @param id ID of the movie which credit to be requested.
     * @param resultCallback Callback in case of success or failure.
     *
     */
    public void requestMovieCredits(
            int id, final MovieDbApiCallback<MovieCreditsRestApi> resultCallback) {
        checkRetrofitService();
        makeCall(service.getMovieCredits(id, API_KEY), resultCallback);
    }

    /**
     * This method does a request about the credits of a movie in particular.
     *  @param id ID of the movie which credit to be requested.
     * @return Network call to execute.
     *
     */
    public Call<MovieCreditsRestApi> getMovieCreditsCall(
            int id) {
        checkRetrofitService();
        return service.getMovieCredits(id, API_KEY);
    }

    /**
     * This method does a request about the available reviews of a movie in particular.
     *
     * @param id ID of the movie which reviews to be requested.
     * @param language Language to use for the request.
     * @param resultCallback Callback in case of success or failure.
     *
     */
    public void requestMovieReviewList(
            int id, String language, final MovieDbApiCallback<MovieReviewListRestApi> resultCallback) {
        checkRetrofitService();
        makeCall(service.getMovieReviewList(id, API_KEY, language), resultCallback);
    }

    /**
     * This method does a request about the available reviews of a movie in particular.
     *  @param id ID of the movie which reviews to be requested.
     * @param language Language to use for the request.
     * @return Network call to execute.
     *
     */
    public Call<MovieReviewListRestApi> getMovieReviewsCall(
            int id, String language) {
        checkRetrofitService();
        return service.getMovieReviewList(id, API_KEY, language);
    }

    /**
     * This method does a request about the available videos of a movie in particular.
     *
     * @param id ID of the movie which videos to be requested.
     * @param language Language to use for the request.
     * @param resultCallback Callback in case of success or failure.
     *
     */
    public void requestMovieVideoList(
            int id, String language, final MovieDbApiCallback<MovieVideoListRestApi> resultCallback) {
        checkRetrofitService();
        makeCall(service.getMovieVideoList(id, API_KEY, language), resultCallback);
    }

    /**
     * This method does a request about the available videos of a movie in particular.
     *  @param id ID of the movie which videos to be requested.
     * @param language Language to use for the request.
     * @return Network call to execute.
     *
     */
    public Call<MovieVideoListRestApi> getMovieVideosCall(
            int id, String language) {
        checkRetrofitService();
        return service.getMovieVideoList(id, API_KEY, language);
    }

    /**
     * This method checks if retrofit service is initiated, and initiates it if not.
     */
    private void checkRetrofitService() {
        if ((retrofit == null) || (service == null))
            initMovieDbApi();
    }

    /**
     * This method creates the URI for a poster image.
     *
     * @param imagePath Relative path of the image requested.
     * @param size Size of the image requested.
     *
     * @return Full URI of the image.
     */
    public static Uri getImageUri(String imagePath, ImageSize size) {
        return Uri.parse(MOVIEDBAPI_IMAGE_BASE).buildUpon().
                appendEncodedPath(ImageSize.getImageSizeString(size)).
                appendEncodedPath(imagePath).
                build();
    }

    /**
     * Callback to invoke when an asynchronous call to movie DB API is finished.
     * @param <T> Type of the param expected as result.
     */
    interface MovieDbApiCallback<T> {

        void onSuccess(T response);

        void onFailure(Throwable exception);

    }
}
