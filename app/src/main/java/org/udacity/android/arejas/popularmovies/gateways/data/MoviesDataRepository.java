package org.udacity.android.arejas.popularmovies.gateways.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.udacity.android.arejas.popularmovies.data.database.FavoritesDatabase;
import org.udacity.android.arejas.popularmovies.data.entities.MovieCreditsItem;
import org.udacity.android.arejas.popularmovies.data.entities.MovieDetails;
import org.udacity.android.arejas.popularmovies.data.entities.MovieListItem;
import org.udacity.android.arejas.popularmovies.data.entities.MovieReviewItem;
import org.udacity.android.arejas.popularmovies.data.entities.MovieVideoItem;
import org.udacity.android.arejas.popularmovies.gateways.data.entities.db.DbPagedListResource;
import org.udacity.android.arejas.popularmovies.gateways.data.entities.mixed.MixedBoundResource;
import org.udacity.android.arejas.popularmovies.gateways.data.entities.network.NetworkPagedListResource;
import org.udacity.android.arejas.popularmovies.gateways.data.entities.Resource;
import org.udacity.android.arejas.popularmovies.data.network.MovieDbApi;
import org.udacity.android.arejas.popularmovies.data.network.model.MovieCreditsRestApi;
import org.udacity.android.arejas.popularmovies.data.network.model.MovieDetailsRestApi;
import org.udacity.android.arejas.popularmovies.data.network.model.MovieListRestApi;
import org.udacity.android.arejas.popularmovies.gateways.data.translator.EntityTranslation;
import org.udacity.android.arejas.popularmovies.data.network.model.MovieReviewListRestApi;
import org.udacity.android.arejas.popularmovies.data.network.model.MovieVideoListRestApi;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import retrofit2.Call;

/*
 * Class representing the gateway for getting information, no matter it comes from network or database.
 * Also used for saving information. It stores all list of movies created in order to cache them.
 */
@SuppressWarnings("UnusedReturnValue")
@Singleton
public class MoviesDataRepository {

    private static final String TAG = "MoviesDataRepository";

    private final MovieDbApi movieDbApi;
    private final FavoritesDatabase favoritesDatabase;
    private final Executor dbExecutor;
    private final Executor networkExecutor;

    private LiveData<Resource<PagedList<MovieListItem>>> popularMovies;
    private LiveData<Resource<PagedList<MovieListItem>>> topRatedMovies;
    private LiveData<Resource<PagedList<MovieListItem>>> searchListMovies;
    private LiveData<Resource<PagedList<MovieListItem>>> favoritesListMovies;

    private final Object popularMoviesLock = new Object();
    private final Object topRatedMoviesLock = new Object();
    private final Object searchListMoviesLock = new Object();
    private final Object favoritesListMoviesLock = new Object();

    private String currentSearchQuery = null;

    @Inject
    public MoviesDataRepository(
            MovieDbApi movieDbApi, FavoritesDatabase favoritesDatabase,
            @Named("dbExecutor") Executor dbExecutor,  @Named("networkExecutor") Executor networkExecutor) {
        this.movieDbApi = movieDbApi;
        this.favoritesDatabase = favoritesDatabase;
        this.dbExecutor = dbExecutor;
        this.networkExecutor = networkExecutor;
    }

    /**
     * Getting a list of most popular movies exclusively from network
     *
     * @param forceReload
     * @param language
     * @return
     */
    public LiveData<Resource<PagedList<MovieListItem>>> getPopularMovies(boolean forceReload, String language) {
        synchronized (popularMoviesLock) {
            if (forceReload) popularMovies = null;
            if (popularMovies == null) {
                NetworkPagedListResource<MovieListRestApi, MovieListItem> data = new NetworkPagedListResource<MovieListRestApi, MovieListItem>(MovieDbApi.RESULTS_PER_PAGE, networkExecutor) {
                    @Override
                    public Call<MovieListRestApi> createPagedRestCall(Integer page) {
                        return movieDbApi.getMostPopularMoviesCall(page, language);
                    }

                    @Override
                    public List<MovieListItem> transformRestToEntityList(MovieListRestApi result) throws Exception {
                        return EntityTranslation.transformMovieListFromRestApiToEntity(result, language);
                    }

                    @Override
                    public Integer getTotalPagesFromRestApi(MovieListRestApi result) throws Exception {
                        return result.getTotalPages();
                    }
                };
                popularMovies = data.getLiveData();
            }
            return popularMovies;
        }
    }

    /**
     * Getting a list of top rated movies exclusively from network
     *
     * @param forceReload
     * @param language
     * @return
     */
    public LiveData<Resource<PagedList<MovieListItem>>> getTopRatedMovies(boolean forceReload, String language) {
        synchronized (topRatedMoviesLock) {
            if (forceReload) topRatedMovies = null;
            if (topRatedMovies == null) {
                NetworkPagedListResource<MovieListRestApi, MovieListItem> data = new NetworkPagedListResource<MovieListRestApi, MovieListItem>(MovieDbApi.RESULTS_PER_PAGE, networkExecutor) {
                    @Override
                    public Call<MovieListRestApi> createPagedRestCall(Integer page) {
                        return movieDbApi.getTopRatedMoviesCall(page, language);
                    }

                    @Override
                    public List<MovieListItem> transformRestToEntityList(MovieListRestApi result) throws Exception {
                        return EntityTranslation.transformMovieListFromRestApiToEntity(result, language);
                    }

                    @Override
                    public Integer getTotalPagesFromRestApi(MovieListRestApi result) throws Exception {
                        return result.getTotalPages();
                    }
                };
                topRatedMovies = data.getLiveData();
            }
            return topRatedMovies;
        }
    }

    /**
     * NOT USED FOR NOW: Getting a list of movies from network containing a search string.
     *
     * @param query
     * @param forceReload
     * @param language
     * @return
     */
    public LiveData<Resource<PagedList<MovieListItem>>> getSearchListMovies(String query, boolean forceReload, String language) {
        synchronized (searchListMoviesLock) {
            if (forceReload || !currentSearchQuery.equals(query)) {
                searchListMovies = null;
                currentSearchQuery = query;
            }
            if (searchListMovies == null) {
                NetworkPagedListResource<MovieListRestApi, MovieListItem> data = new NetworkPagedListResource<MovieListRestApi, MovieListItem>(MovieDbApi.RESULTS_PER_PAGE, networkExecutor) {
                    @Override
                    public Call<MovieListRestApi> createPagedRestCall(Integer page) {
                        return movieDbApi.getMovieSearchCall(page, language, currentSearchQuery);
                    }

                    @Override
                    public List<MovieListItem> transformRestToEntityList(MovieListRestApi result) throws Exception {
                        return EntityTranslation.transformMovieListFromRestApiToEntity(result, language);
                    }

                    @Override
                    public Integer getTotalPagesFromRestApi(MovieListRestApi result) throws Exception {
                        return result.getTotalPages();
                    }
                };
                searchListMovies = data.getLiveData();
            }
            return searchListMovies;
        }
    }

    /**
     * Get a list of movies exclusively from favorites database
     *
     * @return
     */
    public LiveData<Resource<PagedList<MovieListItem>>> getFavoritesListMovies() {
        synchronized (favoritesListMoviesLock) {
            if (favoritesListMovies == null) {
                DbPagedListResource<Integer, MovieListItem> data = new DbPagedListResource<Integer, MovieListItem>(
                        MovieDbApi.RESULTS_PER_PAGE, dbExecutor) {
                    @Override
                    public DataSource.Factory<Integer, MovieListItem> getDbDataSource() {
                        return favoritesDatabase.movieDetailsDao().getAllDataSourceShortVersion();
                    }

                    @Override
                    public void transformResult(PagedList<MovieListItem> result) throws Exception {
                    }

                    @Override
                    public boolean checkIfSuccess(PagedList<MovieListItem> result) throws Exception {
                        return result != null;
                    }
                };
                favoritesListMovies = data.getLiveData();
            }
            return favoritesListMovies;
        }
    }

    /**
     * Get the details of a movie. The idea is to get them from database if present, and if not from
     * network.
     *
     * @param movieId
     * @param language
     * @return
     */
    public LiveData<Resource<MovieDetails>> getMovieDetails(int movieId, String language) {
        return new MixedBoundResource<MovieDetails, MovieDetailsRestApi>() {

            @Override
            public void transformDbResult(@Nullable MovieDetails data) throws Exception {
                Objects.requireNonNull(data).setSavedAsFavorite(true);
            }

            @Override
            protected MovieDetails transformRestToEntity(@NonNull MovieDetailsRestApi item) throws Exception {
                return EntityTranslation.transformMovieDetailsFromRestApiToEntity(item, language);
            }

            @Override
            protected boolean shouldRequestToDb() {
                return true;
            }

            @Override
            protected boolean shouldRequestToNetwork(@Nullable MovieDetails data) {
                return (data == null);
            }

            @Override
            protected boolean shouldSaveToDb(@Nullable MovieDetails data) {
                return false;
            }

            @Override
            protected void saveRestCallResult(@Nullable MovieDetails data) {}

            @Override
            protected LiveData<MovieDetails> loadFromDb() {
                return favoritesDatabase.movieDetailsDao().findByIdLiveData(movieId);
            }

            @Override
            protected Call<MovieDetailsRestApi> createNetworkCall() {
                return movieDbApi.getMovieDetailsCall(movieId, language);
            }
        }.getAsLiveData();
    }

    /**
     * Get the credits of a movie. The idea is to get them from database if present, and if not from
     * network.
     *
     * @param movieId
     * @return
     */
    public LiveData<Resource<List<MovieCreditsItem>>> getMovieCredits(int movieId) {
        return new MixedBoundResource<List<MovieCreditsItem>, MovieCreditsRestApi>() {

            @Override
            public void transformDbResult(@Nullable List<MovieCreditsItem> data) throws Exception {}

            @Override
            protected List<MovieCreditsItem> transformRestToEntity(@NonNull MovieCreditsRestApi item) throws Exception {
                return EntityTranslation.transformMovieCreditsListFromRestApiToEntity(item);
            }

            @Override
            protected boolean shouldRequestToDb() {
                return true;
            }

            @Override
            protected boolean shouldRequestToNetwork(@Nullable List<MovieCreditsItem> data) {
                return ((data == null) || (data.isEmpty()));
            }

            @Override
            protected boolean shouldSaveToDb(@Nullable List<MovieCreditsItem> data) {
                return false;
            }

            @Override
            protected void saveRestCallResult(@Nullable List<MovieCreditsItem> data) {}

            @Override
            protected LiveData<List<MovieCreditsItem>> loadFromDb() {
                return favoritesDatabase.movieCreditsDao().findByMovieIdLiveData(movieId);
            }

            @Override
            protected Call<MovieCreditsRestApi> createNetworkCall() {
                return movieDbApi.getMovieCreditsCall(movieId);
            }
        }.getAsLiveData();
    }

    /**
     * Get the reviews of a movie. The idea is to get them  just from  network.
     *
     * @param movieId
     * @param language
     * @return
     */
    public LiveData<Resource<List<MovieReviewItem>>> getMovieReviews(int movieId, String language) {
        return new MixedBoundResource<List<MovieReviewItem>, MovieReviewListRestApi>() {

            @Override
            public void transformDbResult(@Nullable List<MovieReviewItem> data) throws Exception {}

            @Override
            protected List<MovieReviewItem> transformRestToEntity(@NonNull MovieReviewListRestApi item) throws Exception {
                return EntityTranslation.transformMovieReviewListFromRestApiToEntity(item, language);
            }

            @Override
            protected boolean shouldRequestToDb() {
                return false;
            }

            @Override
            protected boolean shouldRequestToNetwork(@Nullable List<MovieReviewItem> data) {
                return true;
            }

            @Override
            protected boolean shouldSaveToDb(@Nullable List<MovieReviewItem> data) {
                return false;
            }

            @Override
            protected void saveRestCallResult(@Nullable List<MovieReviewItem> data) {}

            @Override
            protected LiveData<List<MovieReviewItem>> loadFromDb() {
                return null;
            }

            @Override
            protected Call<MovieReviewListRestApi> createNetworkCall() {
                return movieDbApi.getMovieReviewsCall(movieId, language);
            }
        }.getAsLiveData();
    }

    /**
     * Get the videos of a movie. The idea is to get them  just from  network.
     *
     * @param movieId
     * @param language
     * @return
     */
    public LiveData<Resource<List<MovieVideoItem>>> getMovieVideos(int movieId, String language) {
        return new MixedBoundResource<List<MovieVideoItem>, MovieVideoListRestApi>() {

            @Override
            public void transformDbResult(@Nullable List<MovieVideoItem> data) throws Exception {}

            @Override
            protected List<MovieVideoItem> transformRestToEntity(@NonNull MovieVideoListRestApi item) throws Exception {
                return EntityTranslation.transformMovieVideoListFromRestApiToEntity(item, language);
            }

            @Override
            protected boolean shouldRequestToDb() {
                return false;
            }

            @Override
            protected boolean shouldRequestToNetwork(@Nullable List<MovieVideoItem> data) {
                return true;
            }

            @Override
            protected boolean shouldSaveToDb(@Nullable List<MovieVideoItem> data) {
                return false;
            }

            @Override
            protected void saveRestCallResult(@Nullable List<MovieVideoItem> data) {}

            @Override
            protected LiveData<List<MovieVideoItem>> loadFromDb() {
                return null;
            }

            @Override
            protected Call<MovieVideoListRestApi> createNetworkCall() {
                return movieDbApi.getMovieVideosCall(movieId, language);
            }
        }.getAsLiveData();
    }

    /**
     * Save the details of a movie on favorites database. Return an empty resource
     * object for knowing the result of the operation.
     *
     * @param details
     * @return
     */
    public MutableLiveData<Resource> saveMovieDetailsOnFavoriteDatabase(MovieDetails details) {
        MutableLiveData<Resource> resultLD = new MutableLiveData<>();
        resultLD.postValue(Resource.loading(null));
        if (details == null) {
            resultLD.postValue(Resource.error(new NullPointerException(""), null));
        } else {
            dbExecutor.execute(() -> {
                favoritesDatabase.movieDetailsDao().insert(details);
                resultLD.postValue(Resource.success(null));
            });
        }
        return resultLD;
    }

    /**
     * Get the details of a movie from network and save them on database. Return an empty resource
     * object for knowing the result of the operation.
     *
     * @param movieId
     * @param language
     * @return
     */
    public LiveData<Resource> getAndSaveMovieDetailsOnFavoriteDatabase(int movieId, String language) {
        MediatorLiveData<Resource> resultLD = new MediatorLiveData<>();
        resultLD.postValue(Resource.loading(null));
        LiveData<Resource<MovieDetails>> saveLD = new MixedBoundResource<MovieDetails, MovieDetailsRestApi>() {

            @Override
            public void transformDbResult(@Nullable MovieDetails data) throws Exception {
            }

            @Override
            protected MovieDetails transformRestToEntity(@NonNull MovieDetailsRestApi item) throws Exception {
                return EntityTranslation.transformMovieDetailsFromRestApiToEntity(item, language);
            }

            @Override
            protected boolean shouldRequestToDb() {
                return false;
            }

            @Override
            protected boolean shouldRequestToNetwork(@Nullable MovieDetails data) {
                return true;
            }

            @Override
            protected boolean shouldSaveToDb(@Nullable MovieDetails data) {
                return true;
            }

            @Override
            protected void saveRestCallResult(@Nullable MovieDetails data) {
                favoritesDatabase.movieDetailsDao().insert(data);
            }

            @Override
            protected LiveData<MovieDetails> loadFromDb() {
                return null;
            }

            @Override
            protected Call<MovieDetailsRestApi> createNetworkCall() {
                return movieDbApi.getMovieDetailsCall(movieId, language);
            }
        }.getAsLiveData();
        resultLD.addSource(saveLD, movieDetailsResource -> {
            Resource oldResource = resultLD.getValue();
            Objects.requireNonNull(oldResource).setStatus(Objects.requireNonNull(movieDetailsResource).getStatus());
            resultLD.postValue(oldResource);
        });
        return resultLD;
    }

    /**
     * Save the credits of a movie on favorites database. Return an empty resource
     * object for knowing the result of the operation.
     *
     * @param credits
     * @return
     */
    public LiveData<Resource> saveMovieCreditsOnFavoriteDatabase(MovieCreditsItem[] credits) {
        MutableLiveData<Resource> resultLD = new MutableLiveData<>();
        resultLD.postValue(Resource.loading(null));
        if (credits == null) {
            resultLD.postValue(Resource.error(new NullPointerException(""), null));
        } else {
            dbExecutor.execute(() -> {
                favoritesDatabase.movieCreditsDao().insertAll(credits);
                resultLD.postValue(Resource.success(null));
            });
        }
        return resultLD;
    }

    /**
     * Get the credits of a movie from network and save them on database. Return an empty resource
     * object for knowing the result of the operation.
     *
     * @param movieId
     * @return
     */
    public LiveData<Resource> getAndSaveMovieCreditsOnFavoriteDatabase(int movieId) {
        MediatorLiveData<Resource> resultLD = new MediatorLiveData<>();
        resultLD.postValue(Resource.loading(null));
        LiveData<Resource<List<MovieCreditsItem>>> saveLD = new MixedBoundResource<List<MovieCreditsItem>, MovieCreditsRestApi>() {

            @Override
            public void transformDbResult(@Nullable List<MovieCreditsItem> data) throws Exception {
            }

            @Override
            protected List<MovieCreditsItem> transformRestToEntity(@NonNull MovieCreditsRestApi item) throws Exception {
                return EntityTranslation.transformMovieCreditsListFromRestApiToEntity(item);
            }

            @Override
            protected boolean shouldRequestToDb() {
                return false;
            }

            @Override
            protected boolean shouldRequestToNetwork(@Nullable List<MovieCreditsItem> data) {
                return true;
            }

            @Override
            protected boolean shouldSaveToDb(@Nullable List<MovieCreditsItem> data) {
                return true;
            }

            @Override
            protected void saveRestCallResult(@Nullable List<MovieCreditsItem> data) {
                try {
                    MovieCreditsItem[] credits = new MovieCreditsItem[Objects.requireNonNull(data).size()];
                    favoritesDatabase.movieCreditsDao().insertAll(data.toArray(credits));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected LiveData<List<MovieCreditsItem>> loadFromDb() {
                return null;
            }

            @Override
            protected Call<MovieCreditsRestApi> createNetworkCall() {
                return movieDbApi.getMovieCreditsCall(movieId);
            }
        }.getAsLiveData();
        resultLD.addSource(saveLD, movieDetailsResource -> {
            Resource oldResource = resultLD.getValue();
            Objects.requireNonNull(oldResource).setStatus(Objects.requireNonNull(movieDetailsResource).getStatus());
            resultLD.postValue(oldResource);
        });
        return resultLD;
    }

    /**
     * Remove the details of a movie from database. Return an empty resource
     * object for knowing the result of the operation.
     *
     * @param details
     * @return
     */
    public LiveData<Resource> removeMovieDetailsFromFavoriteDatabase(MovieDetails details) {
        MutableLiveData<Resource> resultLD = new MutableLiveData<>();
        resultLD.postValue(Resource.loading(null));
        if (details == null) {
            resultLD.postValue(Resource.error(new NullPointerException(""), null));
        } else {
            dbExecutor.execute(() -> {
                favoritesDatabase.movieDetailsDao().delete(details);
                resultLD.postValue(Resource.success(null));
            });
        }
        return resultLD;
    }

    /**
     * Remove the credits of a movie from database. Return an empty resource
     * object for knowing the result of the operation.
     *
     * @param credits
     * @return
     */
    public LiveData<Resource> removeMovieCreditsFromFavoriteDatabase(MovieCreditsItem[] credits) {
        MutableLiveData<Resource> resultLD = new MutableLiveData<>();
        resultLD.postValue(Resource.loading(null));
        if (credits == null) {
            resultLD.postValue(Resource.error(new NullPointerException(""), null));
        } else {
            dbExecutor.execute(() -> {
                favoritesDatabase.movieCreditsDao().deteleAll(credits);
                resultLD.postValue(Resource.success(null));
            });
        }
        return resultLD;
    }

    /**
     * NOT USED FOR NOW: Get all movie detais elements of favorites database and request them again
     * for storing them again if language used at the stored element differs from passed one.
     *
     * @param language
     */
    public void refreshDbInfoForALanguage(@NonNull String language) {
        dbExecutor.execute(() -> {
            List<MovieDetails> movieDetailsList = favoritesDatabase.movieDetailsDao().getAll();
            for( MovieDetails movie : movieDetailsList) {
                if (!language.equals(movie.getDataLanguage())) {
                    new MixedBoundResource<MovieDetails, MovieDetailsRestApi>() {

                        @Override
                        public void transformDbResult(@Nullable MovieDetails data) throws Exception {}

                        @Override
                        protected MovieDetails transformRestToEntity(@NonNull MovieDetailsRestApi item) throws Exception {
                            return EntityTranslation.transformMovieDetailsFromRestApiToEntity(item, language);
                        }

                        @Override
                        protected boolean shouldRequestToDb() {
                            return false;
                        }

                        @Override
                        protected boolean shouldRequestToNetwork(@Nullable MovieDetails data) {
                            return true;
                        }

                        @Override
                        protected boolean shouldSaveToDb(@Nullable MovieDetails data) {
                            return true;
                        }

                        @Override
                        protected void saveRestCallResult(@Nullable MovieDetails data) {
                            favoritesDatabase.movieDetailsDao().update(data);
                        }

                        @Override
                        protected LiveData<MovieDetails> loadFromDb() {
                            return null;
                        }

                        @Override
                        protected Call<MovieDetailsRestApi> createNetworkCall() {
                            return movieDbApi.getMovieDetailsCall(movie.getId(), language);
                        }
                    };
                }
            }
        });
    }

}
