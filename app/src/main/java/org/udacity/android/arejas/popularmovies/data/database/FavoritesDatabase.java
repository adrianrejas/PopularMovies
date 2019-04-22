package org.udacity.android.arejas.popularmovies.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import org.udacity.android.arejas.popularmovies.data.database.dao.MovieCreditsDao;
import org.udacity.android.arejas.popularmovies.data.database.dao.MovieDetailsDao;
import org.udacity.android.arejas.popularmovies.data.entities.MovieCreditsItem;
import org.udacity.android.arejas.popularmovies.data.entities.MovieDetails;

/*
 * Class representing the database of favorites movies
 */
@Database(entities = {MovieDetails.class, MovieCreditsItem.class}, version = 1)
public abstract class FavoritesDatabase extends RoomDatabase {

    public static final String FAVORITE_DB_NAME = "favorite_movies.db";

    public abstract MovieDetailsDao movieDetailsDao();

    public abstract MovieCreditsDao movieCreditsDao();

}