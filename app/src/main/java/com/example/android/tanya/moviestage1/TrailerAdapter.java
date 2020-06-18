package com.example.android.tanya.moviestage1;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.tanya.moviestage1.Model.Trailer;
import com.example.android.tanya.moviestage1.Model.TrailerResults;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.VideoHolder> {

   public Context context;
   public List<TrailerResults> trailers;
public TrailerAdapter(Context context,List<TrailerResults> trailers){
    this.context=context;
    this.trailers=trailers;
}

    @NonNull
    @Override
    public VideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.video_item,parent,false);
        return new VideoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoHolder holder, int position) {
        TrailerResults trailer = trailers.get(position);

        if (trailer.getSite().equalsIgnoreCase("youtube")) {
            Uri uri = Uri.parse("http://img.youtube.com/vi/" + trailer.getKey() + "/3.jpg");
            //holder.name.setText(trailer.getTitle());
            Picasso.Builder builder = new Picasso.Builder(context);
            builder.build()
                    .load(uri)
                    .placeholder((R.drawable.image_loading))
                    .error(R.drawable.image_loading)
                    .into(holder.image);
        }
    }
        @Override
        public int getItemCount () {
            return trailers.size();
        }

        public class VideoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView trailer;
            ImageView image;
            ImageButton trailerPlayBtn;

            VideoHolder(View itemView) {
                super(itemView);
                trailer = itemView.findViewById(R.id.tv_trailer_name);
                trailerPlayBtn=itemView.findViewById(R.id.play_button);
                trailerPlayBtn.setOnClickListener(this);
                image=itemView.findViewById(R.id.thumbnail);
                itemView.setOnClickListener(this);

            }

            @Override
            public void onClick(View view) {
                TrailerResults video = trailers.get(getAdapterPosition());
                if (video != null) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://youtube.com/watch?v=" + video.getKey()));
                    if (intent.resolveActivity(context.getPackageManager()) != null) {
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, "Error playing the video", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        }
    }
