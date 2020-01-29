package com.sagar.chincholkar.retrofit2;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.sagar.chincholkar.retrofit2.Retrofit.APIInterface;
import com.sagar.chincholkar.retrofit2.Retrofit.ApiClient;
import com.sagar.chincholkar.retrofit2.Retrofit.GetUpcomingMovies.GetUpcomingMovies;
import com.sagar.chincholkar.retrofit2.Retrofit.GetUpcomingMovies.Result;
import com.sagar.chincholkar.retrofit2.adapter.UpcomingMoviesApdapter;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private final static String API_KEY = "b7cd3340a794e5a2f35e3abb820b497f";
    private static final String TAG = "Main Activity";
    private APIInterface apiInterface;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        apiInterface = ApiClient.getClient().create(APIInterface.class);
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        swipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue, R.color.red);
        GetMoviesData();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                GetMoviesData();
//                UpcomingMovie.clear();
//                new MainActivity.UpcomingMovielist().execute();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void GetMoviesData() {
        /**
         * GET List Resources
         **/
        Call<GetUpcomingMovies> call = apiInterface.getUpcomingMovies(API_KEY);
        call.enqueue(new Callback<GetUpcomingMovies>() {
            @Override
            public void onResponse
                    (Call<GetUpcomingMovies> call, Response<GetUpcomingMovies> response) {
                ArrayList<Result> movies = new ArrayList<>();
                movies.clear();
                movies = response.body().getResults();
                Collections.reverse(movies);
                UpcomingMoviesApdapter upcomingMoviesApdapter = new UpcomingMoviesApdapter(getApplicationContext(), movies);
                recyclerView.setAdapter(upcomingMoviesApdapter);
                swipeRefreshLayout.setRefreshing(false);

                Log.d(TAG, "Number of movies received: " + movies.size());
            }

            @Override
            public void onFailure(Call<GetUpcomingMovies> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
    }
}