package com.makaya.drd;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.makaya.drd.library.PublicFunction;

public class YoutubePlayerActivity extends YouTubeBaseActivity {

    YouTubePlayerView youTubePlayerView;
    YouTubePlayer.OnInitializedListener onInitializedListener;

    String code;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.youtube_player);
        activity=this;
        code=getIntent().getStringExtra("YoutubeCode");
        PublicFunction.setStatusBarColor(activity);

        playYoutube();
    }

    private void playYoutube()
    {
        //youTubePlayerView=new YouTubePlayerView();
        youTubePlayerView.initialize("AIzaSyBg_-GkM328g2FSZ7SV6gghj3JNpcKJJ4g",onInitializedListener);
        onInitializedListener = new YouTubePlayer.OnInitializedListener(){

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

                youTubePlayer.loadVideo(code);

                youTubePlayer.play();
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };
        /*
        YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
        youTubePlayerFragment.initialize("AIzaSyBg_-GkM328g2FSZ7SV6gghj3JNpcKJJ4g", new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                if (!b) {
                    //youTubePlayer.cueVideo("fhWaJi1Hsfo"); // Plays https://www.youtube.com/watch?v=fhWaJi1Hsfo
                    YPlayer = youTubePlayer;
                    YPlayer.setFullscreen(true);
                    YPlayer.loadVideo(code);
                    YPlayer.play();
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                if (youTubeInitializationResult.isUserRecoverableError()) {
                    youTubeInitializationResult.getErrorDialog(myContext, RECOVERY_REQUEST).show();
                } else {

                    Toast.makeText(myContext, youTubeInitializationResult.toString(), Toast.LENGTH_LONG).show();
                }

            }
        });
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.youtube_fragment, youTubePlayerFragment);//.commit();
        transaction.commitNow();*/
    }
}
