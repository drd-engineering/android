package com.makaya.drd;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.makaya.drd.service.PodCastService;
import com.squareup.picasso.Picasso;
import com.makaya.drd.domain.MemberLogin;
import com.makaya.drd.domain.PodCast;
import com.makaya.drd.library.PublicFunction;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class PodCastActivity extends AppCompatActivity {

    Activity activity;
    SessionManager session;
    MemberLogin user;

    PodCast podcast;

    ImageView btnRepeat;
    TextView title;
    ImageView image;
    TextView descr;
    TextView duration;
    ProgressBar progressBar;
    LinearLayout layoutPodCast;
    ImageButton btnPlayStop;
    SeekBar seekBar;
    TextView trackTime;
    long dataId;
    MediaPlayer mediaPlayer;
    boolean isPlay;
    boolean isRepeat=false;
    boolean isCompleted=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.podcast);
        activity=this;
        session=new SessionManager(getApplication());
        user=session.getUserLogin();
        PublicFunction.setHeaderStatus(activity,"PodCast");
        dataId=getIntent().getLongExtra("DataId",0);
        initObject();
        fetchData();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mediaPlayer.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
    }

    private void initObject()
    {
        layoutPodCast=findViewById(R.id.layoutPodCast);
        progressBar =findViewById(R.id.progressBar);

        layoutPodCast.setVisibility(View.GONE);

        btnRepeat=findViewById(R.id.btnRepeat);
        btnPlayStop=findViewById(R.id.btnPlayStop);
        seekBar=findViewById(R.id.seekBar);
        trackTime=findViewById(R.id.trackTime);

        title=findViewById(R.id.title);
        image=findViewById(R.id.image);
        duration=findViewById(R.id.duration);
        descr =findViewById(R.id.descr);

        btnPlayStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPlay)
                    pause();
                else
                    play();
            }
        });
        btnRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRepeat)
                    btnRepeat.setBackgroundResource(android.R.color.transparent);
                else
                    btnRepeat.setBackgroundResource(android.R.color.holo_green_light);

                isRepeat=!isRepeat;
                mediaPlayer.setLooping(isRepeat);
            }
        });
    }

    private void bindObject()
    {
        String path = MainApplication.getUrlApplWeb() + "/Images/podcast/" + podcast.Image;
        Picasso.with(activity)
                .load(path)
                .placeholder(R.drawable.nopicture)
                .error(R.drawable.nopicture)
                .into(image);

        progressBar.setVisibility(View.GONE);
        layoutPodCast.setVisibility(View.VISIBLE);

        title.setText(podcast.Title);
        duration.setText(""+ PublicFunction.formatDuration(podcast.Duration));
        descr.setText(podcast.Descr);
        preparePlay();

        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
                int realPos=mediaPlayer.getDuration()/100;
                seekBar.setSecondaryProgress(i*realPos);
            }
        });
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                seekBar.setMax(mediaPlayer.getDuration());
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                isCompleted=true;
                stop();
                mediaPlayer.reset();
            }
        });
        seekBar.setProgress(0);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {
                    mediaPlayer.seekTo(i);
                    seekBar.setProgress(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                int track=mediaPlayer.getCurrentPosition()/1000;
                setTextTrackTime(trackTime,""+ PublicFunction.formatDuration(track));
            }
        },0,1000);
    }

    private void preparePlay()
    {
        isPlay=true;
        isCompleted=false;
        String url = MainApplication.getUrlApplWeb()+"/images/podcast/"+podcast.AudioFileName;
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare(); // might take long! (for buffering, etc)
            mediaPlayer.setLooping(isRepeat);
            btnPlayStop.setImageResource(android.R.drawable.ic_media_pause);

        }catch (IOException x){

        }
        mediaPlayer.start();

    }

    private void setTextTrackTime(final TextView text,final String value){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                trackTime.setText(value);
            }
        });
    }

    private void play()
    {
        if (isCompleted) {
            preparePlay();
            return;
        }
        btnPlayStop.setImageResource(android.R.drawable.ic_media_pause);
        isPlay=true;
        mediaPlayer.start();
    }

    private void pause()
    {
        btnPlayStop.setImageResource(android.R.drawable.ic_media_play);
        isPlay=false;
        mediaPlayer.pause();
    }

    private void stop()
    {
        btnPlayStop.setImageResource(android.R.drawable.ic_media_play);
        isPlay=false;
        mediaPlayer.stop();
    }

    private void fetchData()
    {
        PodCastService dsvr=new PodCastService(activity);
        dsvr.setOnDataPostedListener(new PodCastService.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data, boolean isLoadedAllItems) {
                podcast=(PodCast)data;
                bindObject();
            }

            @Override
            public <T> void onDataError() {

            }
        });
        dsvr.getPodCastById(dataId,user.Id);
    }

}
