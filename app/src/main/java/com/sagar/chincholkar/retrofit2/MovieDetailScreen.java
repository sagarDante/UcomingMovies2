package com.sagar.chincholkar.retrofit2;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.sagar.chincholkar.retrofit2.Retrofit.GetUpcomingMovies.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MovieDetailScreen extends AppCompatActivity implements com.daimajia.slider.library.SliderTypes.BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{

//test
    private String title,overview;
    private String REQUEST_URL;
    private TextView titletv,infotv;
    private double rating;
    private int id;

    private ArrayList<Result> Imageurls;
    private String MOVIE_IMAGES ;
    private ProgressDialog pDialog;
    private String responce;
    private SliderLayout sliderLayout;
    private HashMap<String, String> Hash_file_maps ;
    private JSONObject catObj;
    private RatingBar ratingBar;
    private static String TAG = com.sagar.chincholkar.retrofit2.MainActivity.class.getSimpleName();
    double rating1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail_screen);
        //https://www.youtube.com/results?search_query=LSSGHvzMY60

        pDialog = new ProgressDialog(MovieDetailScreen.this);
        id = getIntent().getIntExtra("id",0);
        title = getIntent().getStringExtra("title");
        overview = getIntent().getStringExtra("Overview");
        rating=getIntent().getDoubleExtra("vote",0);
        rating1=(float)rating;
//        Toast.makeText(getApplicationContext(), "id: " + id+" :"+rating, Toast.LENGTH_LONG).show();
        REQUEST_URL = "https://api.themoviedb.org/3/movie/"+id+"/images?api_key=b7cd3340a794e5a2f35e3abb820b497f";
        Imageurls= new ArrayList<>();
        ratingBar=(RatingBar)findViewById(R.id.ratingBar);
       // final RatingBar rate_bar = new RatingBar(this);
        ratingBar.setRating((float) rating1);
        ratingBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),""+rating, Toast.LENGTH_SHORT).show();
            }
        });
        titletv = (TextView) findViewById(R.id.title);
        infotv = (TextView) findViewById(R.id.overview);

        titletv.setText(title);
        infotv.setText(overview);
        Hash_file_maps = new HashMap<>();
        sliderLayout = (SliderLayout)findViewById(R.id.slider);

        new MovieDetailScreen.UpcomingMovielist().execute();
    }

    /**
     * Async task to get all Selected movie Images
     */
    private class UpcomingMovielist extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MovieDetailScreen.this);
            pDialog.setMessage("Fetching Data...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            com.sagar.chincholkar.retrofit2.ServiceHandler jsonParser = new com.sagar.chincholkar.retrofit2.ServiceHandler();
            String json = jsonParser.makeServiceCall(REQUEST_URL, com.sagar.chincholkar.retrofit2.ServiceHandler.GET);

            Log.e("Response: ", "> " + json);

            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    if (jsonObj != null) {
                        JSONArray categories = jsonObj
                                .getJSONArray("backdrops");
                        int no=0;
                        for (int i = 0; i < categories.length(); i++) {
                            catObj = (JSONObject) categories.get(i);
                           // MovieDetails cat = new MovieDetails(catObj.getString("file_path"));

                           // Imageurls.add(cat);
                            //imgurl=catObj.getString("file_path");
                            Hash_file_maps.put(title+no, "https://image.tmdb.org/t/p/w500"+catObj.getString("file_path"));
                            if(no<=4){
                                no++;
                            }//else{no=0;}
                        }

                    } else {
                        responce=jsonObj.getString("result");
//                        Toast.makeText(getApplicationContext(), ""+responce, Toast.LENGTH_SHORT).show();
                        Log.e("Create Category Error: ", "> " + jsonObj.getString("message"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("JSON Data", "Didn't receive any data from server!");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
            for(String name : Hash_file_maps.keySet()){

                TextSliderView textSliderView = new TextSliderView(MovieDetailScreen.this);
                textSliderView
                        .description(name)
                        .image(Hash_file_maps.get(name))
                        .setScaleType(BaseSliderView.ScaleType.Fit)
                        .setOnSliderClickListener(MovieDetailScreen.this);
                textSliderView.bundle(new Bundle());
                textSliderView.getBundle()
                        .putString("extra",name);
                sliderLayout.addSlider(textSliderView);
            }
            sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
            sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            sliderLayout.setCustomAnimation(new DescriptionAnimation());
            sliderLayout.setDuration(4000);
            sliderLayout.addOnPageChangeListener(MovieDetailScreen.this);
//            Toast.makeText(MovieDetailScreen.this,"success", Toast.LENGTH_SHORT).show();


        }

    }


    /**
     * Async task to create a new food category
     * */


    @Override
    protected void onStop() {

        sliderLayout.stopAutoCycle();

        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

        Toast.makeText(this,slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {

        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {}
}
