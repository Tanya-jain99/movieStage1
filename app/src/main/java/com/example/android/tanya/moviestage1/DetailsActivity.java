package com.example.android.tanya.moviestage1;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.tanya.moviestage1.Data.AppDatabase;
import com.example.android.tanya.moviestage1.Data.AppExecutors;
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
   ImageView fav;
   byte isFavourite;
   AppDatabase mDb;
   static boolean update=false;
String TAG=DetailsActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        final Results movie = getIntent().getParcelableExtra("Movies");
        final String title = movie.getTitle();
        final double rate = movie.getRate();
        final String release = movie.getReleaseDate();
        final  String overview = movie.getOverview();
        final  String poster=movie.getPoster_path();
        movie_id=movie.getId();
        position=getIntent().getIntExtra("pos",-1);
        isFavourite=movie.getIsFavourite();
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
                .load(movie.getPoster_path())
                .placeholder(R.drawable.image_loading)
                .error(R.drawable.image_not_found)
                .into(mMoviePosterDisplay);
        getTrailers();
        getReviews();
       mDb=AppDatabase.getInstance(this);
      final byte isFav=mDb.MovieDao().isFavourite(movie_id);

        if(isFav==1){
            Log.d("Tag",isFav+"DB");
            fav.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_click));
        }

        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(isFav==1)|!(MainActivity.movies.get(position).getIsFavourite()==1)) {
                    MainActivity.movies.get(position).setIsFavourite((byte)1);
                    fav.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_click));
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            mDb.MovieDao().insertFavoriteMovie(movie);
                            mDb.MovieDao().updateFavoriteMovie(movie_id,(byte)1);

                            finish();
                        }
                    });
               Toast.makeText(DetailsActivity.this,"Added to Favorites",Toast.LENGTH_SHORT).show();

                }
                else{
                    isFavourite=(byte)0;
                    fav.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_border));
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            mDb.MovieDao().deleteFavoriteMovie(movie);

                        }
                    });
                    Toast.makeText(DetailsActivity.this,"Removed from Favorites",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putByte("isFav",MainActivity.movies.get(position).getIsFavourite());
        outState.putInt("position",position);
        super.onSaveInstanceState(outState);
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
