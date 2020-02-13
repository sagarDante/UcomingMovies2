/*
package com.sagar.chincholkar.retrofit2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class YoutubeVideoActivity extends AppCompatActivity {


    private YouTubePlayerView youTubePlayerView;
    // private Button playNextVideoButton;
    private String videoid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_youtube_video);
        youTubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);

        videoid=getIntent().getStringExtra("videoid");
        if(videoid!=null){
        initYouTubePlayerView();
        }else{
            Log.d("YoutubeVideoActivity", "onCreate: null video id");
        }
    }
    private void initYouTubePlayerView() {
        // The player will automatically release itself when the fragment is destroyed.
        // The player will automatically pause when the fragment is stopped
        // If you don't add YouTubePlayerView as a lifecycle observer, you will have to release it manually.


        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                //String videoId = "S0Q4gqBUs7c";
                youTubePlayer.cueVideo(videoid, 0);
            }
        });
    }
}
*/
