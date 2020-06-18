package com.example.android.tanya.moviestage1;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.tanya.moviestage1.Model.Results;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private List<Results> mMovieData=new ArrayList<>();
    private final MovieAdapterOnClickHandler mClickHandler;

    public MovieAdapter(ArrayList<Results> movie, MovieAdapterOnClickHandler clickHandler) {
        mMovieData = movie;
        mClickHandler = clickHandler;
    }

    public interface MovieAdapterOnClickHandler {
        void onClick(int adapterPosition);
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView mMovieListImageView;
        public  TextView header;

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            mMovieListImageView =  itemView.findViewById(R.id.iv_movie_posters);
            header=itemView.findViewById(R.id.movieName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(adapterPosition);
        }
    }

    @NonNull
    @Override
    public MovieAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_listitem;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        //inflate list item xml into a view
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapterViewHolder holder, int position) {
        //set the movie for list item's position

        final String TMDB_BASE_URL = "https://image.tmdb.org/t/p/";
        final String TMDB_POSTER_SIZE = "w500/";

        String movieToBind = TMDB_BASE_URL+TMDB_POSTER_SIZE  +mMovieData.get(position).getPoster_path();
        mMovieData.get(position).setPoster(movieToBind);
        holder.header.setText(mMovieData.get(position).getTitle());
        Picasso.get()
                .load(movieToBind)
                .placeholder(R.drawable.image_loading)
                .error(R.drawable.image_not_found)
                .into(holder.mMovieListImageView);
    }

    @Override
    public int getItemCount() {
        if (null == mMovieData) {
            return 0;
        }
       Log.d("movie",mMovieData.size()+"");
        return mMovieData.size();
    }
public void clear(){
        this.mMovieData.clear();
        notifyDataSetChanged();
}
    public void setmMovieData(List<Results> movies) {
       this. mMovieData.addAll (movies);
        notifyDataSetChanged();
    }

    public List<Results> getmMovieData() {
        return this.mMovieData;
    }
}