package org.udacity.android.arejas.popularmovies.utils.di.components;

import org.udacity.android.arejas.popularmovies.MoviesApplication;
import org.udacity.android.arejas.popularmovies.utils.di.builders.ActivityBuilder;
import org.udacity.android.arejas.popularmovies.utils.di.modules.AppModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;

/**
 * Component used by dagger to init dependency injection system
 */
@Component (modules={AndroidInjectionModule.class, AppModule.class, ActivityBuilder.class})
@Singleton
public interface AppComponent extends AndroidInjector<MoviesApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(MoviesApplication application);

        AppComponent build();

    }

    void inject(MoviesApplication app);

}
