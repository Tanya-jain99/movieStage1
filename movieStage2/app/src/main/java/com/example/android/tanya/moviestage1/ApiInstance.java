package com.example.android.tanya.moviestage1;

import java.net.URL;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiInstance {
    private static ApiInstance instance;
    private static String BASE_URL = "http://api.themoviedb.org/3/movie/";

    private Retrofit retrofit;

    public static ApiInstance getInstance() {
        if (instance == null)
            instance = new ApiInstance();

        return instance;
    }

    private ApiInstance() {
       retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    public Api getApi(){
        return retrofit.create(Api.class);
    }

}
