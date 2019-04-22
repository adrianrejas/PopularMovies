package org.udacity.android.arejas.popularmovies.data.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import org.udacity.android.arejas.popularmovies.data.entities.MovieCreditsItem;

import java.util.List;

/*
* Class representing the interactions with movie_credits database
 */
@Dao
public interface MovieCreditsDao {

    @Query("SELECT * FROM movie_credits")
    List<MovieCreditsItem> getAll();

    @Query("SELECT * FROM movie_credits WHERE movieId IN (:movieIds)")
    List<MovieCreditsItem> loadAllByMovieIds(int[] movieIds);

    @Query("SELECT * FROM movie_credits WHERE movieId=:movieId")
    List<MovieCreditsItem> findByMovieId(int movieId);

    @Query("SELECT * FROM movie_credits")
    LiveData<List<MovieCreditsItem>> getAllLiveData();

    @Query("SELECT * FROM movie_credits WHERE movieId IN (:movieIds)")
    LiveData<List<MovieCreditsItem>> loadAllByMovieIdsLiveData(int[] movieIds);

    @Query("SELECT * FROM movie_credits WHERE movieId=:movieId")
    LiveData<List<MovieCreditsItem>> findByMovieIdLiveData(int movieId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MovieCreditsItem person);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(MovieCreditsItem... people);

    @Delete
    void delete(MovieCreditsItem person);

    @Delete
    void deteleAll(MovieCreditsItem... people);

    @Update
    void update(MovieCreditsItem person);

}
