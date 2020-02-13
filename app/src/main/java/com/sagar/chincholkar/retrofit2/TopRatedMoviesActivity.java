package com.sagar.chincholkar.retrofit2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;

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

public class TopRatedMoviesActivity extends AppCompatActivity {

    public final static String API_KEY = "b7cd3340a794e5a2f35e3abb820b497f";
    private static final String TAG = "TopRated";
    private APIInterface apiInterface;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    GridLayoutManager mgl_manager;
    private static final int PAGE_START = 1;
    private APIInterface.SortBy defaultApiSortBy = APIInterface.SortBy.RELEASE_DATE_DESCENDING;
    private FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_rated_movies);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        apiInterface = ApiClient.getClient().create(APIInterface.class);
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        mgl_manager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mgl_manager);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fragmentManager=getSupportFragmentManager();
        swipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue, R.color.red);
        GetMoviesData();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                GetMoviesData();

            }
        });
    }
    private void GetMoviesData() {
        /**
         * GET List Resources
         **/
        swipeRefreshLayout.setRefreshing(true);
        Call<GetUpcomingMovies> call = apiInterface.getTopRatedMovies(API_KEY, defaultApiSortBy, PAGE_START);
        call.enqueue(new Callback<GetUpcomingMovies>() {
            @Override
            public void onResponse
                    (Call<GetUpcomingMovies> call, Response<GetUpcomingMovies> response) {
                ArrayList<Result> movies = new ArrayList<>();
                movies.clear();
                assert response.body() != null;
                movies = response.body().getResults();
                Collections.reverse(movies);

                UpcomingMoviesApdapter upcomingMoviesApdapter = new UpcomingMoviesApdapter(TopRatedMoviesActivity.this, movies,fragmentManager);
                recyclerView.setAdapter(upcomingMoviesApdapter);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<GetUpcomingMovies> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
    }
}
