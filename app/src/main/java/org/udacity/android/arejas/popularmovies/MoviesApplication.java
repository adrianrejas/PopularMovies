package org.udacity.android.arejas.popularmovies;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import org.udacity.android.arejas.popularmovies.utils.di.components.DaggerAppComponent;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

/**
 * Class representing the application (it's main task is to initiate injection system powered by dagger.
 */
public class MoviesApplication extends Application implements HasActivityInjector {

    private static Application application;

    public static Context getContext() {
        return application.getApplicationContext();
    }

    public static Application getApplication() {
        return application;
    }

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        this.initDagger();
    }

    private void initDagger(){
        DaggerAppComponent.builder().application(this).build().inject(this);
    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }
}
