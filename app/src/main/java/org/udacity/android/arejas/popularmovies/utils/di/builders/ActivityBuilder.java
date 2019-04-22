package org.udacity.android.arejas.popularmovies.utils.di.builders;

import org.udacity.android.arejas.popularmovies.ui.MainActivity;
import org.udacity.android.arejas.popularmovies.ui.MovieActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Builder used by dagger for binding objects to the existing activies requiring them
 */
@Module
public abstract class ActivityBuilder {

    @ContributesAndroidInjector
    abstract MainActivity bindMainActivity();

    @ContributesAndroidInjector
    abstract MovieActivity bindMovieActivity();

}
