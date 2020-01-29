package com.sagar.chincholkar.retrofit2.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sagar.chincholkar.retrofit2.MovieDetailScreen;
import com.sagar.chincholkar.retrofit2.R;
import com.sagar.chincholkar.retrofit2.Retrofit.GetUpcomingMovies.Result;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UpcomingMoviesApdapter extends RecyclerView.Adapter<UpcomingMoviesApdapter.ViewHolder> {
    private ArrayList<Result> listItems;
    private Context c;
    private String imgurl;
    private String imgid;
    private boolean Sadult;
    private String overview;
    public UpcomingMoviesApdapter(Context c, ArrayList<Result> listItems) {
        this.c = c;
        this.listItems = listItems;
    }

    @Override
    public UpcomingMoviesApdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view,parent,false);

        return new UpcomingMoviesApdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final UpcomingMoviesApdapter.ViewHolder holder, int position) {
        final Result Mlist=listItems.get(position);

        Sadult=Mlist.getAdult();
        imgid= String.valueOf(Mlist.getId());
        // overview=Mlist.getOverview();
        imgurl=Mlist.getPosterPath();
        holder.title.setText(Mlist.getTitle());// DateUtils.formatDateTime(c, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_12HOUR)
        holder.releasedate.setText(Mlist.getReleaseDate());
        if(!Sadult) {
            holder.adult.setText("U/A");
        }else{
            holder.adult.setText("A");
        }
        // picasso
        Picasso.with(c)
                .load("https://image.tmdb.org/t/p/w500"+imgurl)
                .into(holder.posterimg);

        holder.posterimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(c, MovieDetailScreen.class);

                i.putExtra("id",Mlist.getId());
                i.putExtra("title",Mlist.getTitle());
                i.putExtra("Overview",Mlist.getOverview());
                i.putExtra("vote",Mlist.getVoteAverage());
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                c.startActivity(i);

            }
        });




    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        private ImageView posterimg,play;
        private TextView releasedate;
        private TextView title,adult;





        public ViewHolder(View itemView) {
            super(itemView);
            //  itemView.setOnClickListener(c.getApplicationContext());
            posterimg=(ImageView)itemView.findViewById(R.id.imageView);
            play=(ImageView)itemView.findViewById(R.id.play);
            title=(TextView) itemView.findViewById(R.id.title);
            releasedate=(TextView)itemView.findViewById(R.id.releasedate);
            adult=(TextView) itemView.findViewById(R.id.adulttextview);


        }


    }
}