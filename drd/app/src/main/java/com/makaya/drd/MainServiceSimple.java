package com.makaya.drd;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import com.makaya.drd.domain.MemberLogin;

/**
 * Created by xbudi on 25/11/2016.
 */

public class MainServiceSimple extends Service {
    Handler mHandler;
    Context context;
    boolean inProcess=false;
    SessionManager session;
    MemberLogin user;

    @Override
    public IBinder onBind(Intent intent) {
        return  null;
    }
    @Override
    public void onCreate() {
        Toast.makeText(this, "Service was Created", Toast.LENGTH_LONG).show();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stopping the player when service is destroyed
        Toast.makeText(this, "Service Stopped", Toast.LENGTH_LONG).show();
    }

}
