package com.sagar.chincholkar.retrofit2.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.sagar.chincholkar.retrofit2.MovieDetailScreen;
import com.sagar.chincholkar.retrofit2.MyCustomDialogFragment;
import com.sagar.chincholkar.retrofit2.R;
import com.sagar.chincholkar.retrofit2.Retrofit.APIInterface;
import com.sagar.chincholkar.retrofit2.Retrofit.ApiClient;
import com.sagar.chincholkar.retrofit2.Retrofit.GetMovieTrailorFromId.GetMovieTrailer;
import com.sagar.chincholkar.retrofit2.Retrofit.GetUpcomingMovies.Result;
import com.sagar.chincholkar.retrofit2.UpcomingMoviesActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpcomingMoviesApdapter extends RecyclerView.Adapter<UpcomingMoviesApdapter.ViewHolder> {
    private ArrayList<Result> listItems;
    private Context c;
    private String imgurl;
    private String imgid;
    private boolean Sadult;
    private String overview;
    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;
    private APIInterface apiInterface;
    private static final String TAG = "Adapter";
    public UpcomingMoviesApdapter(Context c) {
        this.c = c;
    }
    private Lifecycle lifecycle;
    private YouTubePlayerView youTubePlayerView;
    private FragmentManager mFragmentManager;

    public UpcomingMoviesApdapter(Context c, ArrayList<Result> listItems,FragmentManager fragmentManager) {
        this.c = c;
        this.listItems = listItems;
        this.mFragmentManager=fragmentManager;
    }

    public UpcomingMoviesApdapter(Context c, ArrayList<Result> listItems) {
        this.c = c;
        this.listItems = listItems;

    }

    @Override
    public UpcomingMoviesApdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view,parent,false);


        apiInterface = ApiClient.getClient().create(APIInterface.class);
        return new UpcomingMoviesApdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final UpcomingMoviesApdapter.ViewHolder holder, final int position) {
        final Result Mlist = listItems.get(position);

        Sadult = Mlist.getAdult();
        imgid = String.valueOf(Mlist.getId());
        // overview=Mlist.getOverview();
        imgurl = Mlist.getPosterPath();
        holder.title.setText(Mlist.getTitle());// DateUtils.formatDateTime(c, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_12HOUR)
        holder.releasedate.setText(Mlist.getReleaseDate());
        if (!Sadult) {
            holder.adult.setText("U/A");
        } else {
            holder.adult.setText("A");
        }
        // picasso
        Picasso.with(c)
                .load("https://image.tmdb.org/t/p/w500" + imgurl)
                .into(holder.posterimg);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(c, MovieDetailScreen.class);

                i.putExtra("id", Mlist.getId());
                i.putExtra("title", Mlist.getTitle());
                i.putExtra("Overview", Mlist.getOverview());
                i.putExtra("vote", Mlist.getVoteAverage());
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                c.startActivity(i);

            }
        });

        holder.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               GetMoviesTrailors(Mlist.getId(), position);
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
    private void GetMoviesTrailors(int id, final int pos) {
        /* *
         * GET List Resources
         **/
        final String[] key = new String[2];
        Call<GetMovieTrailer> call = apiInterface.getUpcomingMoviesVideos(id, UpcomingMoviesActivity.API_KEY);
        call.enqueue(new Callback<GetMovieTrailer>() {

            @Override
            public void onResponse
                    (Call<GetMovieTrailer> call, Response<GetMovieTrailer> response) {
                List<com.sagar.chincholkar.retrofit2.Retrofit.GetMovieTrailorFromId.Result> moviesTrailors = new ArrayList<>();
                assert response.body() != null;
                moviesTrailors = response.body().getResults();

                key[0] =moviesTrailors.get(0).getKey();

                Log.d(TAG, "onClick: " + key[0]);


                FragmentTransaction ft = mFragmentManager.beginTransaction();
                Fragment prev = mFragmentManager.findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);
                DialogFragment dialogFragment = new MyCustomDialogFragment();
                Bundle args = new Bundle();
                args.putString("videoid",key[0] );
                dialogFragment.setArguments(args);
                dialogFragment.show(ft, "dialog");
               /* Intent i =new Intent(c, YoutubeVideoActivity.class);
                i.putExtra("videoid",key[0]);
                c.startActivity(i);*/
                Log.d(TAG, "Number of movies received: " + moviesTrailors.size()+ key[0]);
            }

            @Override
            public void onFailure(Call<GetMovieTrailer> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });

    }
}