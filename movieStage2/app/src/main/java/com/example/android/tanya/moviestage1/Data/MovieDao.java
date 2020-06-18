package com.example.android.tanya.moviestage1.Data;



import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.android.tanya.moviestage1.Model.Movies;
import com.example.android.tanya.moviestage1.Model.Results;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movies ORDER BY vote_average DESC")
    List<Results> loadAllFavoriteMovies();

    @Query("SELECT isFavourite FROM movies WHERE id = :movieId")
    boolean isFavourite(int movieId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavoriteMovie(Results movie);

    @Query("UPDATE movies SET isFavourite = :isFavorite WHERE id = :movieId")
    void updateFavoriteMovie(int movieId, boolean isFavorite);

    @Delete
    void deleteFavoriteMovie(Results movie);

    @Query("SELECT * FROM movies WHERE id = :movieId")
    Results getMovie(int movieId);
}
