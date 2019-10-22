package com.makaya.drd;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.makaya.drd.domain.Company;
import com.makaya.drd.domain.MemberLogin;
import com.makaya.drd.library.PublicFunction;
import com.makaya.drd.service.CompanyService;

import java.util.Timer;
import java.util.TimerTask;

//import android.support.v7.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {

    SessionManager session;
    MemberLogin user;
    TextView progressText;
    Activity activity;
    LinearLayout layout;
    Company zone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window w = getWindow();
        w.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        w.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        setContentView(R.layout.splash_screen);
        activity=this;

        session=new SessionManager(getApplication());
        user=session.getUserLogin();
        progressText=(TextView)findViewById(R.id.progressText);
        layout=findViewById(R.id.layout);
        //fetchSessionManager();
       /* fetchData();
        doSplashScreen();*/
        getZone(getString(R.string.main_zone));

    }

    void startService()
    {
        Intent serviceIntent = new Intent(this, MainService.class);
        startService(serviceIntent);
        /*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getApplicationContext().startForegroundService(serviceIntent);
        }
        else {
            getApplicationContext().startService(serviceIntent);
        }*/
    }

    private void setText(final TextView text, final String value){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                text.setText(value);
            }
        });
    }

    private void runCheckingUser(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                checkingUser();
            }
        });
    }

    private void checkingService()
    {
        runCheckingUser();
        /*progressText.setText("Checking services...");
        if (PublicFunction.isMyServiceRunning(getApplication(),MainService.class))
            checkingUser();
        else {
            progressText.setText("Starting services...");
            startService();
            startTimerUser();
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    if (PublicFunction.isMyServiceRunning(getApplication(),MainService.class)) {
                        setText(progressText, "Service is running...");
                        runCheckingUser();
                    }
                }
            }, 1000 * 5);
        }*/
    }

    private Timer timer;
    private TimerTask timerTask;
    public void startTimerUser() {
        timer = new Timer();
        timerTask = new TimerTask() {
            public void run() {
                if (PublicFunction.isMyServiceRunning(getApplication(),MainService.class)) {
                    timer.cancel();
                    timer = null;
                    setText(progressText, "Service is running...");
                    runCheckingUser();
                }
            }
        };
        timer.schedule(timerTask, 1000, 1000); //
    }

    private void checkingUser()
    {

        progressText.setText("Checking user login...");
        session.setOnSessionListener(new SessionManager.OnSetSessionListener() {
            @Override
            public <T> void onDataPosted(T data) {
                finishSplashScreen((MemberLogin)data);
            }
        });
        session.initLoginStatus();
    }

    private void getZone(String code)
    {
        progressText.setText("Get zone...");
        CompanyService svr=new CompanyService(activity);
        svr.setOnDataPostedListener(new CompanyService.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data, boolean isLoadedAllItems) {
                zone=(Company)data;
                //PublicFunction2.setBackgroundColorGradient(layout,zone.BackColorBar,"#e5e3e3");
                checkingService();
            }

            @Override
            public <T> void onDataError() {

            }
        });
        svr.getByCode(code);
    }

    private void finishSplashScreen(MemberLogin user)
    {
        if (user!=null && user.Id!=0) {
            Intent intent = new Intent(this, DashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }else{
            Intent intent = new Intent(this, UserLoginActivity.class);
            intent.putExtra("AppZone",zone);
            startActivity(intent);
        }

        finish();
    }

}
