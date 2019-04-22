package org.udacity.android.arejas.popularmovies.gateways.ui;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;

import org.udacity.android.arejas.popularmovies.data.entities.MovieListItem;
import org.udacity.android.arejas.popularmovies.gateways.data.MoviesDataRepository;
import org.udacity.android.arejas.popularmovies.gateways.data.entities.Resource;

/*
* View model representing the data used at the main activity. It stores all list of movies created
* in order to cache them.
 */
public class MainViewModel extends AndroidViewModel {

    private LiveData<Resource<PagedList<MovieListItem>>> popularMovies;
    private LiveData<Resource<PagedList<MovieListItem>>> topRatedMovies;
    private LiveData<Resource<PagedList<MovieListItem>>> favoritesListMovies;

    private final MoviesDataRepository dataRepository;
    private final String language;

    public MainViewModel(@NonNull Application application, @NonNull MoviesDataRepository dataRepository,
                         @NonNull String language) {
        super(application);
        this.dataRepository = dataRepository;
        this.language = language;
    }

    public LiveData<Resource<PagedList<MovieListItem>>> getPopularMovies(boolean forceReload) {
        if ((popularMovies == null) || forceReload)
            popularMovies = this.dataRepository.getPopularMovies(forceReload, this.language);
        return popularMovies;
    }

    public LiveData<Resource<PagedList<MovieListItem>>> getTopRatedMovies(boolean forceReload) {
        if ((topRatedMovies == null) || forceReload)
            topRatedMovies = this.dataRepository.getTopRatedMovies(forceReload, this.language);
        return topRatedMovies;
    }

    public LiveData<Resource<PagedList<MovieListItem>>> getFavoritesListMovies() {
        if (favoritesListMovies == null)
            favoritesListMovies = this.dataRepository.getFavoritesListMovies();
        return favoritesListMovies;
    }

    public void updateFavoriteListLanguage() {
        this.dataRepository.refreshDbInfoForALanguage(this.language);
    }
}
