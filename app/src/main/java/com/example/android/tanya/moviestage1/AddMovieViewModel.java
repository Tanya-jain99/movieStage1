package com.example.android.tanya.moviestage1;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.tanya.moviestage1.Data.AppDatabase;
import com.example.android.tanya.moviestage1.Model.Results;

import java.util.List;

public class AddMovieViewModel extends ViewModel {
    public LiveData<List<Results>> getMovies() {
        return movies;
    }

    private LiveData<List<Results>> movies;

    public AddMovieViewModel(AppDatabase db) {
       movies=db.MovieDao().loadAllFavoriteMovies();
    }
}
