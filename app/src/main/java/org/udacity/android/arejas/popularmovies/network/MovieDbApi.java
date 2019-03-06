package org.udacity.android.arejas.popularmovies.network;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;

import org.udacity.android.arejas.popularmovies.R;
import org.udacity.android.arejas.popularmovies.data.MovieDetails;
import org.udacity.android.arejas.popularmovies.data.MovieList;
import org.udacity.android.arejas.popularmovies.utils.Utils;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Class with static methods to be invoked for invoking background requests to movieDB API.
 */
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

    private static final String TAG = "MovieAPI";

    private static final String MOVIEDBAPI_IMAGE_BASE = "http://image.tmdb.org/t/p/";

    private static final String MOVIEDBAPI_BASE = "https://api.themoviedb.org/3/";

    /* Set here the API key to use */
    private static final String API_KEY = "SET_API_KEY_HERE";

    private static Retrofit retrofit;
    private static MovieDbApService service;
    private static Call currentCall;

    /* We'll initiate the retrofit service at the beginning of the appication. */
    static {
        initMovieDbApi();
    }

    /**
     * Function called for initiating the static service to be used for calling movieDB API
     * through retrofit library. The idea is to call this function at the beginning of the
     * application or before every call if for any reason the service is not instantiated.
     */
    private static void initMovieDbApi () {
        retrofit = new Retrofit.Builder()
                .baseUrl(MOVIEDBAPI_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(MovieDbApService.class);
        currentCall = null;
    }

    /**
     * This method does a request about the list of most popular movies
     *
     * @param page Page number to request.
     * @param resultCallback Callback in case of success or failure.
     * @param context Application context in order to get locale for language.
     *
     */
    private static void requestMostPopularMovies(
            int page, final MovieDbApiCallback<MovieList> resultCallback, Context context) {
        if ((retrofit == null) || (service == null))
            initMovieDbApi();
        if (currentCall != null)
            currentCall.cancel();
        String language = context.getResources().getString(R.string.api_query_language);
        Call<MovieList> serviceCall = service.getMostPopularMovies(page, API_KEY, language);
        currentCall = serviceCall;
        serviceCall.enqueue(new Callback<MovieList>() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onResponse(@NonNull Call<MovieList> call, @NonNull Response<MovieList> response) {
                try {
                    switch (response.code()) {
                        case 200:
                            resultCallback.onSuccess(response.body());
                            break;
                        default:
                            resultCallback.onFailure(new IOException(
                                    String.format("Error at requestMostPopularMovies with code %d", response.code())));
                            break;
                    }
                } finally {
                    currentCall = null;
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieList> call, @NonNull Throwable t) {
                try {
                    resultCallback.onFailure(t);
                } finally {
                    currentCall = null;
                }
            }
        });
    }

    /**
     * This method does a request about the list of top rated movies
     *
     * @param page Page number to request.
     * @param resultCallback Callback in case of success or failure.
     * @param context Application context in order to get locale for language.
     *
     */
    private static void requestTopRatedMovies(
            int page, final MovieDbApiCallback<MovieList> resultCallback, Context context) {
        if ((retrofit == null) || (service == null))
            initMovieDbApi();
        if (currentCall != null)
            currentCall.cancel();
        String language = context.getResources().getString(R.string.api_query_language);
        Call<MovieList> serviceCall = service.getTopRatedMovies(page, API_KEY, language);
        currentCall = serviceCall;
        serviceCall.enqueue(new Callback<MovieList>() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onResponse(@NonNull Call<MovieList> call, @NonNull Response<MovieList> response) {
                try {
                    switch (response.code()) {
                        case 200:
                            resultCallback.onSuccess(response.body());
                            break;
                        default:
                            resultCallback.onFailure(new IOException(
                                    String.format("Error at requestTopRatedMovies with code %d", response.code())));
                            break;
                    }
                } finally {
                    currentCall = null;
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieList> call, @NonNull Throwable t) {
                try {
                    resultCallback.onFailure(t);
                } finally {
                    currentCall = null;
                }
            }
        });
    }

    /**
     * This method does a request for a new Movie list.
     *
     * @param sortType Sort type to be used.
     * @param resultCallback Callback in case of success or failure.
     * @param context Application context in order to get locale for language.
     *
     */
    public static void requestNewMovieList(
            Utils.MovieSortType sortType, final MovieDbApiCallback<MovieList> resultCallback,
            Context context)  {
        if (sortType == Utils.MovieSortType.POPULAR)
            requestMostPopularMovies(1, resultCallback, context);
        else if (sortType == Utils.MovieSortType.TOP_RATED)
            requestTopRatedMovies(1, resultCallback, context);
        else
            resultCallback.onFailure(new IOException("Invalid sort type."));
    }

    /**
     * This method does a request for loading more pages for a new Movie list.
     *
     * @param sortType Sort type to be used.
     * @param currentList List to be updated
     * @param resultCallback Callback in case of success or failure.
     * @param context Application context in order to get locale for language.
     *
     */
    public static void requestLoadMorePages(
            Utils.MovieSortType sortType, final MovieList currentList,
            final MovieDbApiCallback<MovieList> resultCallback,
            Context context) {
        try {
            if (currentList == null)
                resultCallback.onFailure(new IOException("No existing list provided."));
            MovieDbApiCallback<MovieList> intermediateCallback = new MovieDbApiCallback<MovieList>() {
                @Override
                public void onSuccess(MovieList response) {
                    if (currentList != null) {
                        currentList.mixWith(response);
                        resultCallback.onSuccess(currentList);
                    } else {
                        resultCallback.onFailure(new IOException("Null parameters on network call"));
                    }
                }

                @Override
                public void onFailure(Throwable exception) {
                    resultCallback.onFailure(exception);
                }
            };
            if (currentList != null) {
                if (sortType == Utils.MovieSortType.POPULAR)
                    requestMostPopularMovies(currentList.getPage() + 1, intermediateCallback, context);
                else if (sortType == Utils.MovieSortType.TOP_RATED)
                    requestTopRatedMovies(currentList.getPage() + 1, intermediateCallback, context);
                else
                    resultCallback.onFailure(new IOException("Invalid sort type."));
            } else {
                resultCallback.onFailure(new IOException("Null parameters on network call"));
            }
        } catch (Exception e) { // In order to catch null pointer exceptions and another exceptions it can appear.
            resultCallback.onFailure(e);
        }
    }

    /**
     * This method does a request about the details of a movie in particular.
     *
     * @param id ID of the movie to be requested.
     * @param resultCallback Callback in case of success or failure.
     * @param context Application context in order to get locale for language.
     *
     */
    public static void requestMovieDetails(
            int id, final MovieDbApiCallback<MovieDetails> resultCallback,
            Context context) {
        if ((retrofit == null) || (service == null))
            initMovieDbApi();
        if (currentCall != null)
            currentCall.cancel();
        String language = context.getResources().getString(R.string.api_query_language);
        Call<MovieDetails> serviceCall = service.getMovieDetails(id, API_KEY, language);
        currentCall = serviceCall;
        serviceCall.enqueue(new Callback<MovieDetails>() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onResponse(@NonNull Call<MovieDetails> call, @NonNull Response<MovieDetails> response) {
                try {
                    switch (response.code()) {
                        case 200:
                            resultCallback.onSuccess(response.body());
                            break;
                        default:
                            resultCallback.onFailure(new IOException(
                                    String.format("Error at requestMostPopularMovies with code %d", response.code())));
                            break;
                    }
                } finally {
                    currentCall = null;
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieDetails> call, @NonNull Throwable t) {
                try {
                    resultCallback.onFailure(t);
                } finally {
                    currentCall = null;
                }
            }
        });
    }

    /**
     * This method will cancel call in progress. At each call, we save the current call in progress
     * in order to cancel it if necessary from UI.
     *
     */
    public static void cancelCurrentCall() {
        if (currentCall != null)
            currentCall.cancel();
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

    public interface MovieDbApiCallback<T> {

        void onSuccess(T response);

        void onFailure(Throwable exception);

    }
}
