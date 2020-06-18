package com.example.android.tanya.moviestage1;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.tanya.moviestage1.AddMovieViewModel;
import com.example.android.tanya.moviestage1.Data.AppDatabase;
import com.example.android.tanya.moviestage1.Model.Results;

import java.util.List;

public class MainViewModel extends ViewModelProvider.NewInstanceFactory {

private AppDatabase database;

    public MainViewModel(AppDatabase db) {
        this.database=db;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T)new AddMovieViewModel(database);
    }
}
