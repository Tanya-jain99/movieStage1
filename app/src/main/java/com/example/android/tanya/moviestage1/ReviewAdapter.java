package com.example.android.tanya.moviestage1;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.tanya.moviestage1.Model.ReviewResults;
import com.example.android.tanya.moviestage1.ReviewAdapter.ReviewsHolder;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewsHolder> {
Context context;
ArrayList<ReviewResults> reviews=new ArrayList<>();

    public ReviewAdapter(Context context,ArrayList<ReviewResults> reviews) {
       this.context=context;
       this.reviews=reviews;
    }

    @NonNull
    @Override
    public ReviewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.review_listitem,parent,false);
        return new ReviewsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsHolder holder, int position) {
     holder.review.setText(reviews.get(position).getContent());

      holder.name.setText(reviews.get(position).getAuthor());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class ReviewsHolder extends RecyclerView.ViewHolder{
public TextView review,name;
        public ReviewsHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.author_tv);
           review=itemView.findViewById(R.id.reviewcontent);
        }
    }
}
