package com.makaya.drd.service;

import android.app.Activity;
import android.os.Handler;

import com.makaya.drd.MainApplication;
import com.makaya.drd.R;
import com.makaya.drd.domain.StampLiteRoot;
import com.makaya.drd.library.DisplayToast;

/**
 * Created by xbudi on 28/09/2017.
 */

public class StampService {

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
    Handler handler;

    public StampService(Activity activity)
    {
        this.activity=activity;
        global=(MainApplication)activity.getApplication();
        handler=new Handler();
    }

    public void getList(long memberId, String topCriteria, int current_page)
    {
        String url= String.format(activity.getString(R.string.api_stamp_list),
                global.getUrlApplApi(), memberId, topCriteria, current_page,
                MainApplication.getPageSize());

        PostDataModelUrl posData = new PostDataModelUrl();
        posData.execute(url, StampLiteRoot.class);
        posData.setOnDataPostedListener(new PostDataModelUrl.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data) {
                if (data==null){
                    if (onSetDataPostedListener != null) {
                        onSetDataPostedListener.onDataError();
                    }
                    handler.post(new DisplayToast(activity, activity.getResources().getString(R.string.fetch_data_problem)));
                    return;
                }
                if (onSetDataPostedListener != null) {
                    StampLiteRoot root=((StampLiteRoot) data);
                    onSetDataPostedListener.onDataPosted(root, (root.Root).size() == 0);
                }
            }

            @Override
            public <T> void onPostedError(Exception data) {
                if (onSetDataPostedListener != null) {
                    onSetDataPostedListener.onDataError();
                }
                handler.post(new DisplayToast(activity, activity.getResources().getString(R.string.fetch_data_problem)));
            }
        });
    }

}
