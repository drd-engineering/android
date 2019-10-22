package com.makaya.drdamin.service;

import android.app.Activity;
import android.widget.Toast;

import com.makaya.drdamin.MainApplication;
import com.makaya.drdamin.R;
import com.makaya.drdamin.domain.ModuleRoot;


/**
 * Created by xbudi on 01/03/2017.
 */

public class ModuleService {
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


    public ModuleService(Activity activity)
    {
        this.activity=activity;
        global=(MainApplication)activity.getApplication();
    }

    public void fetchAllActive()
    {
        String url= String.format(activity.getString(R.string.api_get_moduleallactive),
                global.getUrlLinkApi());

        PostDataModelUrl posData = new PostDataModelUrl();
        posData.execute(url, ModuleRoot.class);
        posData.setOnDataPostedListener(new PostDataModelUrl.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data) {
                if (onSetDataPostedListener != null) {
                    ModuleRoot root=((ModuleRoot) data);
                    onSetDataPostedListener.onDataPosted(root.Root, true);
                }
            }

            @Override
            public <T> void onPostedError(Exception data) {
                if (onSetDataPostedListener != null) {
                    onSetDataPostedListener.onDataError();
                }
                Toast.makeText(activity,activity.getResources().getString(R.string.fetch_data_problem),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
