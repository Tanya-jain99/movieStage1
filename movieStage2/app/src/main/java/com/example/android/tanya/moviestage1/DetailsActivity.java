package com.example.android.tanya.moviestage1;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.tanya.moviestage1.Data.AppDatabase;
import com.example.android.tanya.moviestage1.Model.Results;
import com.example.android.tanya.moviestage1.Model.ReviewResults;
import com.example.android.tanya.moviestage1.Model.Reviews;
import com.example.android.tanya.moviestage1.Model.Trailer;
import com.example.android.tanya.moviestage1.Model.TrailerResults;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity {
ImageView mMoviePosterDisplay;
TextView titleText,rating,overviewView,releaseView;
int movie_id,position;
ArrayList<TrailerResults> trailers=new ArrayList<>();
RecyclerView trailersList;
TrailerAdapter adapter;
    ArrayList<ReviewResults> reviews=new ArrayList<>();
    RecyclerView reviewsList;
   ReviewAdapter reviewadapter;
   ImageView fav;boolean isFavourite;
   AppDatabase mDb;
String TAG=DetailsActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        final String poster = getIntent().getStringExtra("poster");
        final String title = getIntent().getStringExtra("title");
        final double rate = getIntent().getDoubleExtra("rate",0);
        final String release = getIntent().getStringExtra("release");
       final  String overview = getIntent().getStringExtra("overview");
        movie_id=getIntent().getIntExtra("id",0);
        position=getIntent().getIntExtra("pos",-1);
        isFavourite=getIntent().getBooleanExtra("Fav",false);
        ActionBar bar=getSupportActionBar();
       bar.setDisplayHomeAsUpEnabled(true);

        adapter=new TrailerAdapter(this,trailers);
        trailersList=findViewById(R.id.recyclerview);
        reviewsList=findViewById(R.id.reviewsView);
        reviewsList.setHasFixedSize(true);
        fav=findViewById(R.id.favbtn);
        reviewsList.addItemDecoration(new DividerItemDecoration(this,LinearLayout.VERTICAL));
        LinearLayoutManager manager=new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false);
        trailersList.setLayoutManager(manager);
        reviewadapter=new ReviewAdapter(this,reviews);
        trailersList=findViewById(R.id.recyclerview);
        LinearLayoutManager reviewmanager=new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        reviewsList.setLayoutManager(reviewmanager);
        mMoviePosterDisplay=findViewById(R.id.posterImageView);
        titleText=findViewById(R.id.titleTextView);
        rating=findViewById(R.id.ratingTextView);
        overviewView=findViewById(R.id.overviewTextView);
        releaseView=findViewById(R.id.releaseDateTextView);
        titleText.setText(title);
        overviewView.setText(overview);
        rating.setText(rate+"");
        releaseView.setText(release);
        setTitle(title);
        Picasso.get()
                .load(poster)
                .placeholder(R.drawable.image_loading)
                .error(R.drawable.image_not_found)
                .into(mMoviePosterDisplay);
        getTrailers();
        getReviews();
       mDb=AppDatabase.getInstance(this);
       boolean isFav=false;

        if(isFav){
            fav.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_click));
        }
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mDb.MovieDao().isFavourite(movie_id)||!MainActivity.movies.get(position).getIsFavourite()) {

                    MainActivity.movies.get(position).setIsFavourite(true);
                    fav.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_click));

                    Results movie=new Results(title,overview,release,rate,poster,true);
                    mDb.MovieDao().insertFavoriteMovie(movie);
                    mDb.MovieDao().updateFavoriteMovie(movie_id,true);
                    MainActivity.sortSpinner.setSelection(2);
                    finish();

                }
                else{
                    isFavourite=!isFavourite;
                    fav.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_border));
                    mDb.MovieDao().deleteFavoriteMovie(new Results(title,overview,release,rate,poster,true));
                    MainActivity.sortSpinner.setSelection(2);finish();
                }
            }
        });


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("isFav",MainActivity.movies.get(position).getIsFavourite());
        outState.putInt("position",position);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

  private  void getTrailers(){
        Log.d("movie_id",movie_id+"");
      Call<Trailer> trailersCall=ApiInstance.getInstance().getApi().getTrailer(String.valueOf(movie_id),"66927d5e4bedc582812eaec16096e983");
      trailersCall.enqueue(new Callback<Trailer>() {
          @Override
          public void onResponse(Call<Trailer> call, Response<Trailer> response) {
              if(response.isSuccessful()){
                 Trailer trailer=response.body();
                 trailers.addAll(trailer.getResults());
                 trailersList.setAdapter(adapter);
                 adapter.notifyDataSetChanged();

              }
          }

          @Override
          public void onFailure(Call<Trailer> call, Throwable t) {
              Toast.makeText(DetailsActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
          }
      });


    }
    public void getReviews(){

            Log.d("movie_id",movie_id+"");
            Call<Reviews> trailersCall=ApiInstance.getInstance().getApi().getReviews(String.valueOf(movie_id),"66927d5e4bedc582812eaec16096e983");
            trailersCall.enqueue(new Callback<Reviews>() {
                @Override
                public void onResponse(Call<Reviews> call, Response<Reviews> response) {
                    if(response.isSuccessful()){
                        Reviews review=response.body();
                        reviews.addAll(review.getResults());
                        reviewsList.setAdapter(reviewadapter);
                        reviewadapter.notifyDataSetChanged();
                       // Log.d("inMethod",reviews.get(0)+"");
                    }
                }

                @Override
                public void onFailure(Call<Reviews> call, Throwable t) {
                    Toast.makeText(DetailsActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });

        }
}
