package com.makaya.drdamin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

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

