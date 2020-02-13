package com.sagar.chincholkar.retrofit2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.sagar.chincholkar.retrofit2.Retrofit.APIInterface;
import com.sagar.chincholkar.retrofit2.Retrofit.ApiClient;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Main Activity";
    private APIInterface apiInterface;



    private APIInterface.SortBy defaultApiSortBy = APIInterface.SortBy.RELEASE_DATE_DESCENDING;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        apiInterface = ApiClient.getClient().create(APIInterface.class);
        Button btn_upcoming= findViewById(R.id.b_upcoming);
        Button btn_Popular=findViewById(R.id.b_popular);
        Button ban_Nonpaying=findViewById(R.id.b_nowplaying);
        btn_upcoming.setOnClickListener(this);
        btn_Popular.setOnClickListener(this);
        ban_Nonpaying.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
     /*   int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
*/
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.b_popular:
                startActivity(new Intent(getApplicationContext(), PopularMoviesActivity.class));
                break;
            case R.id.b_upcoming:
                startActivity(new Intent(getApplicationContext(),UpcomingMoviesActivity.class));
                break;
            case R.id.b_nowplaying:
                startActivity(new Intent(getApplicationContext(),NowPlayingMoviesActivity.class));
                break;
        }
    }
}
