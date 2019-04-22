package org.udacity.android.arejas.popularmovies.gateways.ui.factories;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import org.udacity.android.arejas.popularmovies.gateways.data.MoviesDataRepository;
import org.udacity.android.arejas.popularmovies.gateways.ui.MainViewModel;
import org.udacity.android.arejas.popularmovies.gateways.ui.MovieViewModel;

import java.security.InvalidParameterException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
/*
* Factory used for creating view models. in the case of the movie view model, the movie ID to load
* must be set before using the factory for instantiating a view model.
 */
@SuppressWarnings("unchecked")
@Singleton
public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final Application application;
    private final MoviesDataRepository dataRepository;
    private final String language;

    private Integer movieIdToLoad;

    @Inject
    public ViewModelFactory(@NonNull Application application, MoviesDataRepository dataRepository,
                         @Named("language") String language) {
        this.application = application;
        this.dataRepository = dataRepository;
        this.language = language;
    }

    public void setMovieIdToLoad (int movieId) {
        movieIdToLoad = movieId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        if (modelClass.isAssignableFrom(MainViewModel.class))
            return (T) new MainViewModel(application, dataRepository, language);
        else if (modelClass.isAssignableFrom(MovieViewModel.class)) {
            if (movieIdToLoad != null) {
                T viewModelToReturn =
                        (T) new MovieViewModel(application, dataRepository, language, movieIdToLoad);
                movieIdToLoad = null;
                return viewModelToReturn;
            } else {
                throw new InvalidParameterException("Need to specify movie ID before");
            }
        }else {
            throw new ClassCastException("No view model class recognized");
        }
    }

}
