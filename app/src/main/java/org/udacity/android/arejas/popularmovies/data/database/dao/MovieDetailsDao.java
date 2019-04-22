package org.udacity.android.arejas.popularmovies.data.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import org.udacity.android.arejas.popularmovies.data.entities.MovieDetails;
import org.udacity.android.arejas.popularmovies.data.entities.MovieListItem;

import java.util.List;

/*
 * Class representing the interactions with movie_details database
 */
@Dao
public interface MovieDetailsDao {

    @Query("SELECT * FROM movie_details")
    List<MovieDetails> getAll();

    @Query("SELECT id, voteAverage, title, posterPath, dataLanguage FROM movie_details")
    List<MovieListItem> getAllShortVersion();

    @Query("SELECT *  FROM movie_details WHERE id IN (:userIds)")
    List<MovieDetails> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM movie_details WHERE id=:id LIMIT 1")
    MovieDetails findById(int id);

    @Query("SELECT * FROM movie_details WHERE id=:id LIMIT 1")
    List<MovieDetails> checkIfExistAndFindById(int id);

    @Query("SELECT * FROM movie_details WHERE title LIKE (:name)")
    List<MovieDetails> findByName(String name);

    @Query("SELECT * FROM movie_details")
    LiveData<List<MovieDetails>> getAllLiveData();

    @Query("SELECT * FROM movie_details")
    DataSource.Factory<Integer, MovieDetails> getAllDataSource();

    @Query("SELECT id, voteAverage, title, posterPath, dataLanguage FROM movie_details")
    LiveData<List<MovieListItem>> getAllLiveDataShortVersion();

    @Query("SELECT id, voteAverage, title, posterPath, dataLanguage FROM movie_details")
    DataSource.Factory<Integer, MovieListItem> getAllDataSourceShortVersion();

    @Query("SELECT * FROM movie_details WHERE id IN (:userIds)")
    LiveData<List<MovieDetails>> loadAllByIdsLiveData(int[] userIds);

    @Query("SELECT * FROM movie_details WHERE id=:id LIMIT 1")
    LiveData<MovieDetails> findByIdLiveData(int id);

    @Query("SELECT * FROM movie_details WHERE title LIKE (:name)")
    LiveData<List<MovieDetails>> findByNameLiveData(String name);

    @Insert
    void insert(MovieDetails movie);

    @Insert
    void insertAll(MovieDetails... movies);

    @Delete
    void delete(MovieDetails movie);

    @Delete
    void deteleAll(MovieDetails... movies);

    @Update
    void update(MovieDetails movie);

}
