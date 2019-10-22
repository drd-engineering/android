package com.makaya.drd;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by xbudi on 26/11/2016.
 */

public class Autostart extends BroadcastReceiver
{

    public void onReceive(Context context, Intent intent)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context, MainService.class));
        } else {
            context.startService(new Intent(context, MainService.class));
        }
    }

}

