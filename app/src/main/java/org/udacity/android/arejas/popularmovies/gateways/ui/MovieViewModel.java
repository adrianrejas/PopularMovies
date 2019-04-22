package org.udacity.android.arejas.popularmovies.gateways.ui;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import org.udacity.android.arejas.popularmovies.data.entities.MovieCreditsItem;
import org.udacity.android.arejas.popularmovies.data.entities.MovieDetails;
import org.udacity.android.arejas.popularmovies.data.entities.MovieReviewItem;
import org.udacity.android.arejas.popularmovies.data.entities.MovieVideoItem;
import org.udacity.android.arejas.popularmovies.gateways.data.MoviesDataRepository;
import org.udacity.android.arejas.popularmovies.gateways.data.entities.Resource;

import java.util.List;

/*
 * View model representing the data used at the movie activity. It stores the data of the movie
 * in order to cache it. It provides also functions for setting and unsetting movies as favorites.
 */
public class MovieViewModel extends AndroidViewModel {

    private final MoviesDataRepository dataRepository;
    private final String language;
    private final Integer movieId;

    private LiveData<Resource<MovieDetails>> details;
    private LiveData<Resource<List<MovieCreditsItem>>> credits;
    private LiveData<Resource<List<MovieReviewItem>>> reviews;
    private LiveData<Resource<List<MovieVideoItem>>> videos;

    public MovieViewModel(@NonNull Application application, @NonNull MoviesDataRepository dataRepository,
                          @NonNull String language, @NonNull Integer movieId) {
        super(application);
        this.dataRepository = dataRepository;
        this.language = language;
        this.movieId = movieId;
    }

    public LiveData<Resource<MovieDetails>> getMovieDetails() {
        if (details == null)
            details = this.dataRepository.getMovieDetails(movieId, language);
        return details;
    }

    public LiveData<Resource<List<MovieCreditsItem>>> getMovieCredits() {
        if (credits == null)
            credits = this.dataRepository.getMovieCredits(movieId);
        return credits;
    }

    public LiveData<Resource<List<MovieReviewItem>>> getMovieReviews() {
        if (reviews == null)
            reviews = this.dataRepository.getMovieReviews(movieId, language);
        return reviews;
    }

    public LiveData<Resource<List<MovieVideoItem>>> getMovieVideos() {
        if (videos == null)
            videos = this.dataRepository.getMovieVideos(movieId, language);
        return videos;
    }


    /**
     * Set a movie as favorite, if we have the details and the credits loaded, this data will be
     * saved at favorites database. If not, it will be requested and later saved. Movie credits will
     * be stored 200 ms later in order to be sure movie details are stored (if movie credits storage
     * failed don't do anything, movie credits storage is optional for the working of the app). A LiveData
     * Resource object will be returned in order to trace the operation result.
     *
     * @return
     */
    public LiveData<Resource> setMovieAsFavorite() {
        MovieDetails movieDetails = null;
        if (details != null && details.getValue() != null && details.getValue().getStatus().equals(Resource.Status.SUCCESS)) {
            movieDetails = details.getValue().getData();
        }
        if (movieDetails == null) {
            return this.dataRepository.getAndSaveMovieDetailsOnFavoriteDatabase(movieId, language);
        } else {
            (new android.os.Handler()).postDelayed(() -> {
                try {
                    List<MovieCreditsItem> movieCredits = null;
                    if (credits != null && credits.getValue() != null && credits.getValue().getStatus().equals(Resource.Status.SUCCESS)) {
                        movieCredits = credits.getValue().getData();
                    }
                    if (movieCredits == null) {
                        dataRepository.getAndSaveMovieCreditsOnFavoriteDatabase(movieId);
                    } else {
                        MovieCreditsItem[] credits = new MovieCreditsItem[movieCredits.size()];
                        dataRepository.saveMovieCreditsOnFavoriteDatabase(movieCredits.toArray(credits));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, 200);
            return this.dataRepository.saveMovieDetailsOnFavoriteDatabase(movieDetails);
        }
    }

    /**
     * Unset a movie as favorite, if we have the details loaded, this data will be
     * removed from favorites database. Due to cascade relationship established with Room library,
     * movie credits of the movie will be removed once the details are removed. A LiveData
     * Resource object will be returned in order to trace the operation result.
     *
     * @return
     */
    public LiveData<Resource> unsetMovieAsFavorite() {
        MovieDetails movieDetails = null;
        if (details != null && details.getValue() != null && details.getValue().getStatus().equals(Resource.Status.SUCCESS)) {
            movieDetails = details.getValue().getData();
        }
        if (movieDetails != null) {
            return this.dataRepository.removeMovieDetailsFromFavoriteDatabase(movieDetails);
        } else {
            MutableLiveData<Resource> nullResult = new MutableLiveData<>();
            nullResult.postValue(Resource.error(new NullPointerException(), null));
            return nullResult;
        }
    }
}
