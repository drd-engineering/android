package com.makaya.xchat;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;

/**
 * Created by xbudi on 20/10/2016.
 */

public class MainApplication {
    Activity activity;
    
    public MainApplication(Activity activity){
        this.activity=activity;
    }
    
    public String getUrlApplBaseApi()
    {
        return activity.getString(R.string.urlApplBaseApi);
    }

    public String getUrlApplApi()
    {
        return activity.getString(R.string.urlApplApi);
    }

    public String getUrlApplWeb()
    {
        return activity.getString(R.string.urlApplWeb);
    }

    public int getInboxTimer()
    {
        return activity.getResources().getInteger(R.integer.inboxTimer);
    }
    public int getInboxChatTimer()
    {
        return activity.getResources().getInteger(R.integer.inboxChatTimer);
    }

    public int getPageSize()
    {
        return activity.getResources().getInteger(R.integer.pageSize);
    }
    public int getPaginateLoadingTriggerThreshold()
    {
        return activity.getResources().getInteger(R.integer.paginateLoadingTriggerThreshold);
    }

}
