package com.makaya.drd.service;

import android.app.Activity;
import android.widget.Toast;

import com.makaya.drd.MainApplication;
import com.makaya.drd.R;


/**
 * Created by xbudi on 28/02/2017.
 */

public class DashboardService {

    // MY EVENT HANDLER

    private OnSetDataPostedListener onSetDataPostedListener;
    public interface OnSetDataPostedListener {
        public <T> void onDataPosted(T data, boolean isLoadedAllItems);
        public <T> void onDataError();
    }

    public void setOnDataPostedListener(OnSetDataPostedListener listener) {
        onSetDataPostedListener = listener;
    }

    // /MY EVENT HANDLER

    Activity activity;
    MainApplication global;

    public DashboardService(Activity activity)
    {
        this.activity=activity;
        global=(MainApplication)activity.getApplication();
    }


}
