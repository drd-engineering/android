package com.makaya.drdamin;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.makaya.drdamin.domain.UserAdmin;
import com.makaya.drdamin.service.PublicFunction;

import java.util.Timer;
import java.util.TimerTask;

//import android.support.v7.app.AppCompatActivity;

public class SplashScreenActivity extends Activity {

    SessionManager session;
    UserAdmin user;
    TextView progressText;

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

        session=new SessionManager(getApplication());
        user=session.getUserLogin();
        progressText=(TextView)findViewById(R.id.progressText);
        //fetchSessionManager();
       /* fetchData();
        doSplashScreen();*/
        checkingService();

    }

    void startService()
    {
        Intent serviceIntent = new Intent(getApplicationContext(), MainService.class);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getApplicationContext().startForegroundService(serviceIntent);
        }
        else {
            getApplicationContext().startService(serviceIntent);
        }
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
        progressText.setText("Checking services...");
        if (PublicFunction.isMyServiceRunning(getApplication(),MainService.class))
            checkingUser();
        else {
            progressText.setText("Starting services...");
            startService();

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    if (PublicFunction.isMyServiceRunning(getApplication(),MainService.class)) {
                        setText(progressText, "Service is running...");
                        runCheckingUser();
                    }
                }
            }, 1000 * 5);
        }
    }

    private void checkingUser()
    {

        progressText.setText("Checking user login...");
        session.setOnSessionListener(new SessionManager.OnSetSessionListener() {
            @Override
            public <T> void onDataPosted(T data) {
                finishSplashScreen((UserAdmin)data);
            }
        });
        session.initLoginStatus();
    }


    private void finishSplashScreen(UserAdmin driver)
    {
        if (driver!=null && driver.Id!=0) {
            Intent intent = new Intent(this, DashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }else{
            Intent intent = new Intent(this, UserLoginActivity.class);
            startActivity(intent);
        }

        finish();
    }

}
