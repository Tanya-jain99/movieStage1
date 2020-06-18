package com.example.android.tanya.moviestage1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.tanya.moviestage1.Data.AppDatabase;
import com.example.android.tanya.moviestage1.Data.AppExecutors;
import com.example.android.tanya.moviestage1.Model.Movies;
import com.example.android.tanya.moviestage1.Model.Results;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {
private RecyclerView mRecyclerView;
    boolean isScrolling;
   public  static Spinner sortSpinner;
   public static ArrayList<Results> favourites=new ArrayList<>();
    int page=1;
    public static ArrayList<Results> movies=new ArrayList<>();
    private static Context mContext;
public MovieAdapter movieadapter=new MovieAdapter(movies,this);
    private RecyclerView.LayoutManager mLayoutManager;
    private final int NUM_OF_COLUMNS = 2;
    public ProgressBar  mLoadingIndicator;
    TextView error,header;
    boolean connected=false;
AppDatabase mDb;
boolean flag=false;
    public static Context getContext(){
        return mContext;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        mDb = AppDatabase.getInstance(getApplicationContext());
        mRecyclerView = findViewById(R.id.recycler_view);
        mLoadingIndicator = findViewById(R.id.indicatingBar);
        mRecyclerView.setAdapter(movieadapter);
        mRecyclerView.setVisibility(View.INVISIBLE);
        error = findViewById(R.id.error);
        header=findViewById(R.id.movieName);

        // Using a Grid Layout Manager
        mLayoutManager = new GridLayoutManager(this, NUM_OF_COLUMNS);
        mRecyclerView.setLayoutManager(mLayoutManager);
        movieadapter=new MovieAdapter(movies,this);
        mRecyclerView.setAdapter(movieadapter);
       sortSpinner = findViewById(R.id.sort_spinner);

        try {
            ConnectivityManager connectivityManager = (ConnectivityManager)
                    getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnected();


        } catch (Exception e) {
            System.out.println("CheckConnectivity Exception: " + e.getMessage());
            Log.v("connectivity", e.toString());
        }

       if(!connected){Toast.makeText(this,"No Internet Connection",Toast.LENGTH_LONG).show();}

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (connected) {
                    if (i == 0) {
                        // If most popular was selected
                        mLoadingIndicator.setVisibility(View.VISIBLE);
                        movies.clear();
                        popularMovies(1);
                        flag = false;
                    }
                    if (i == 1) {

                        mLoadingIndicator.setVisibility(View.VISIBLE);
                        movies.clear();
                        topRated(1);
                        flag = false;
                    }
                    if (i == 2) {
                        flag = true;
                        movies.clear();
                        getFavouriteMovies();
                    }
                }
            }



            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mLoadingIndicator.setVisibility(View.VISIBLE);
                popularMovies(1);
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState== AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                   isScrolling=true;//isScrolling is a boolean variable which is set to true when we scroll our page.
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //Now this helps in when we scroll the entire page there is a need to call the 2,3,4 pages and so on for this
                //we need a listener that could see if we have scrolled till the last question of page
                final int visibleThreshold = 2;

                GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                int lastItem = layoutManager.findLastCompletelyVisibleItemPosition();
                int currentTotalCount = layoutManager.getItemCount();

                if (currentTotalCount <= lastItem + visibleThreshold &&!flag) {
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    popularMovies(++page);
                }
            }

    });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!connected)
            Toast.makeText(this,"no Internet",Toast.LENGTH_LONG).show();
        mLoadingIndicator.setVisibility(View.GONE);
    }

    public void getFavouriteMovies(){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                final List<Results> tasks = mDb.MovieDao().loadAllFavoriteMovies();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        movies.clear();
                        movies.addAll(tasks);
                        mRecyclerView.setAdapter(movieadapter);

                    }
                });
            }
        });
    }

    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        error.setVisibility(View.VISIBLE);
    }

    private void popularMovies(int page) {
        Call<Movies> call = ApiInstance.getInstance().getApi().getPopularMovies("66927d5e4bedc582812eaec16096e983", page);
        call.enqueue(new Callback<Movies>() {
            @Override
            public void onResponse(Call<Movies> call, Response<Movies> response) {
                 if(response.isSuccessful()){

                     Movies movie=response.body();
                     List<Results> results=movie.getResults();
                     mRecyclerView.setVisibility(View.VISIBLE);
                     movies.addAll(results);
                     MovieAdapter adapter=new MovieAdapter(movies,MainActivity.this);
                     mRecyclerView.setAdapter(adapter);
                     movieadapter.notifyDataSetChanged();
                     mLoadingIndicator.setVisibility(View.INVISIBLE);
                 }
                 else{
                     showErrorMessage();
                 }
            }

            @Override
            public void onFailure(Call<Movies> call, Throwable t) {
                Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

private  void topRated(int page){
        Call<Movies> call2=ApiInstance.getInstance().getApi().getTopRatingMovies("66927d5e4bedc582812eaec16096e983",page);
        call2.enqueue(new Callback<Movies>() {
            @Override
            public void onResponse(Call<Movies> call, Response<Movies> response) {
                if(response.isSuccessful()){
                    movies.clear();
                    Movies movie=response.body();
                    List<Results> results=movie.getResults();
                    mRecyclerView.setVisibility(View.VISIBLE);
                    movies.addAll(results);
                    mRecyclerView.setAdapter(movieadapter);
                    mLoadingIndicator.setVisibility(View.GONE);
                    movieadapter.notifyDataSetChanged();
                }
                else{
                    showErrorMessage();
                }
            }

            @Override
            public void onFailure(Call<Movies> call, Throwable t) {
            Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
}

    @Override
    public void onClick(int adapterPosition)  {
        Context context = this;
        Class destinationClass = DetailsActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, adapterPosition);
        intentToStartDetailActivity.putExtra("title", movies.get(adapterPosition).getTitle());
        intentToStartDetailActivity.putExtra("poster", movies.get(adapterPosition).getPoster_path());
        intentToStartDetailActivity.putExtra("rate", movies.get(adapterPosition).getRate());
        intentToStartDetailActivity.putExtra("release", movies.get(adapterPosition).getReleaseDate());
        intentToStartDetailActivity.putExtra("overview", movies.get(adapterPosition).getOverview());
        intentToStartDetailActivity.putExtra("id",movies.get(adapterPosition).getId());
        intentToStartDetailActivity.putExtra("Fav",movies.get(adapterPosition).getIsFavourite());
        intentToStartDetailActivity.putExtra("pos",adapterPosition);
        startActivity(intentToStartDetailActivity);
    }
}

