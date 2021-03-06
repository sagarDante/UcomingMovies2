package com.sagar.chincholkar.retrofit2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

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

public class PopularMoviesActivity extends AppCompatActivity {
    public final static String API_KEY = "b7cd3340a794e5a2f35e3abb820b497f";
    private static final String TAG = "Main Activity";
    private APIInterface apiInterface;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private static final int PAGE_START = 1;
    private boolean isScrolling = false;
    private boolean isLastPage = false;
    private LinearLayoutManager manager;
    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    GridLayoutManager mgl_manager;

    ArrayList<Result> movies = new ArrayList<>();
    private FragmentManager fragmentManager;

    private UpcomingMoviesApdapter upcomingMoviesApdapter;
    private APIInterface.SortBy defaultApiSortBy = APIInterface.SortBy.RELEASE_DATE_DESCENDING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_movies);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        movies.clear();
        mgl_manager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);

        //manager = new LinearLayoutManager(this);
        apiInterface = ApiClient.getClient().create(APIInterface.class);
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mgl_manager);
        fragmentManager=getSupportFragmentManager();
        swipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue, R.color.red);
        swipeRefreshLayout.setRefreshing(true);


        GetMoviesData();


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                GetMoviesData();

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
        swipeRefreshLayout.setRefreshing(true);
        Call<GetUpcomingMovies> call = apiInterface.getPopularMovies(API_KEY, defaultApiSortBy, PAGE_START);
        call.enqueue(new Callback<GetUpcomingMovies>() {
            @Override
            public void onResponse
                    (Call<GetUpcomingMovies> call, Response<GetUpcomingMovies> response) {

                assert response.body() != null;
                movies.clear();
                movies.addAll(response.body().getResults());
                Collections.reverse(movies);
                upcomingMoviesApdapter = new UpcomingMoviesApdapter(PopularMoviesActivity.this, movies,fragmentManager);
                recyclerView.setAdapter(upcomingMoviesApdapter);
//                upcomingMoviesApdapter.notifyDataSetChanged();


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
