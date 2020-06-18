package com.example.android.tanya.moviestage1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Movie;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.tanya.moviestage1.Data.AppDatabase;
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
    int page=1;

    public static ArrayList<Results> movies=new ArrayList<>();
    private static Context mContext;
    public MovieAdapter movieadapter=new MovieAdapter(movies,this);
    private RecyclerView.LayoutManager mLayoutManager;
    private  int NUM_OF_COLUMNS;
    public ProgressBar  mLoadingIndicator;
    TextView error,header;
    boolean connected=false;
    int currentSelection;
    private String BUNDLE_RECYCLER_LAYOUT="com.android.MainActivity.Layout";
    Parcelable savedRecyclerLayoutState;
    AppDatabase mDb;
    boolean flag=false;
        MovieAdapter popular;
    public static Context getContext(){
        return mContext;
    }
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
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
          NUM_OF_COLUMNS =calculateNoOfColumns(this);
        mLayoutManager = new GridLayoutManager(this, NUM_OF_COLUMNS);
        mRecyclerView.setLayoutManager(mLayoutManager);
        movieadapter=new MovieAdapter(movies,this);
        mRecyclerView.setAdapter(movieadapter);


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
        if(savedInstanceState!=null) {

            movies=savedInstanceState.getParcelableArrayList("movies");
            savedRecyclerLayoutState=savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
//            movieadapter.clear();
//            movieadapter.setmMovieData(movies);
        }
      if(savedInstanceState==null)
         topRated(1);
       if(!connected){Toast.makeText(this,"No Internet Connection",Toast.LENGTH_LONG).show();}
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
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        movies = savedInstanceState.getParcelableArrayList("array");
        mRecyclerView.setVisibility(View.VISIBLE);
        savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, mRecyclerView.getLayoutManager().onSaveInstanceState());
        outState.putInt("sort",currentSelection);
        outState.putParcelableArrayList("array",movies);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!connected)
            Toast.makeText(this,"no Internet",Toast.LENGTH_LONG).show();
        mLoadingIndicator.setVisibility(View.GONE);

    }


    private  void  viewFav(){
        movies.clear();
    MainViewModel movieViewModel=new MainViewModel(mDb);
    final AddMovieViewModel model=  new ViewModelProvider(this,movieViewModel).get(AddMovieViewModel.class);
    model.getMovies().observe(this, new Observer<List<Results>>() {
        @Override
        public void onChanged(List<Results> results) {
            Log.d("Tag","in here");
            movies.addAll(results);
            MovieAdapter adapter=new MovieAdapter(movies,MainActivity.this);
            mRecyclerView.setAdapter(adapter);
        }
    });
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
                     Log.d("popular",movies.size()+"");
                     movieadapter.notifyDataSetChanged();
                     mLoadingIndicator.setVisibility(View.INVISIBLE);
                     //To mantain the scroll position on rotation for a better user experience
                     mRecyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
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
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu, menu);
            return super.onCreateOptionsMenu(menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            switch (id) {
                case R.id.topRated:
                    movies.clear();
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    topRated(1);
                    break;
                case R.id.popular:
                    movies.clear();
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                   mRecyclerView.setAdapter(null);
                   mRecyclerView.setAdapter(movieadapter);
                    popularMovies(1);
                    break;

                case R.id.favorite:
                    movies.clear();
                    mRecyclerView.setAdapter(null);
                    viewFav();
                    break;
            }
            return super.onOptionsItemSelected(item);
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
                    mLoadingIndicator.setVisibility(View.GONE);
                    movieadapter.notifyDataSetChanged();
                    //To mantain the scroll position on rotation for a better user experience
                    mRecyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
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
        intentToStartDetailActivity.putExtra("Movies",movies.get(adapterPosition));
        intentToStartDetailActivity.putExtra("pos",adapterPosition);
        startActivity(intentToStartDetailActivity);
    }

    public  int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics =context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 200;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        if(noOfColumns < 2)
            noOfColumns = 2;
        return noOfColumns;
    }
    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        error.setVisibility(View.VISIBLE);
    }
}

