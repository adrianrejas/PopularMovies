package org.udacity.android.arejas.popularmovies.utils.di.modules;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;

import org.udacity.android.arejas.popularmovies.MoviesApplication;
import org.udacity.android.arejas.popularmovies.R;
import org.udacity.android.arejas.popularmovies.data.database.FavoritesDatabase;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Module used by dagger for the provision of objects to be injected in a special way
 */
@Module
public class AppModule {

    private static final int THREADS_FOR_GENERAL_EXECUTOR = 3;

    @Provides
    Application provideApplication() {
        return MoviesApplication.getApplication();
    }

    @Provides
    Context provideApplicationContext() {
        return MoviesApplication.getContext();
    }

    @Provides
    @Named("dbExecutor")
    Executor provideDbExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    @Provides
    @Named("networkExecutor")
    Executor provideNetworkExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    @Provides
    @Named("generalExecutor")
    Executor provideGeneralExecutor() {
        return Executors.newFixedThreadPool(THREADS_FOR_GENERAL_EXECUTOR);
    }

    @Provides
    @Singleton
    FavoritesDatabase provideDatabase(MoviesApplication application) {
        return Room.databaseBuilder(application,
                FavoritesDatabase.class, FavoritesDatabase.FAVORITE_DB_NAME)
                .build();
    }

    @Provides
    @Named("defaultLanguage")
    String provideDefaultLanguage(Context appContext) {
        return appContext.getString(R.string.api_query_language_default);
    }

    @Provides
    @Named("language")
    String provideLanguage(Context appContext) {
        return appContext.getString(R.string.api_query_language);
    }

}
