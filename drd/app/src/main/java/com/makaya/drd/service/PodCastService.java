package com.makaya.drd.service;

import android.app.Activity;
import android.widget.Toast;

import com.makaya.drd.MainApplication;
import com.makaya.drd.R;
import com.makaya.drd.domain.News;
import com.makaya.drd.domain.NewsLiteRoot;
import com.makaya.drd.domain.NewsVideo;
import com.makaya.drd.domain.NewsVideoRoot;
import com.makaya.drd.domain.PodCast;
import com.makaya.drd.domain.PodCastLiteRoot;


/**
 * Created by xbudi on 28/02/2017.
 */

public class PodCastService {

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

    public PodCastService(Activity activity)
    {
        this.activity=activity;
        global=(MainApplication)activity.getApplication();
    }

    public void fetchPodCast(long memberId, String topCriteria, int current_page, String sortdata,String filterdata)
    {
        String url= String.format(activity.getString(R.string.api_podcast_get),
                global.getUrlApplApi(), memberId, topCriteria, current_page, global.getPageSize(),sortdata,filterdata);

        PostDataModelUrl posData = new PostDataModelUrl();
        posData.execute(url, PodCastLiteRoot.class);
        posData.setOnDataPostedListener(new PostDataModelUrl.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data) {
                if (data==null) {
                    if (onSetDataPostedListener != null) {
                        onSetDataPostedListener.onDataError();
                    }
                    Toast.makeText(activity,activity.getResources().getString(R.string.fetch_data_problem),Toast.LENGTH_SHORT).show();
                    return;
                }
                if (onSetDataPostedListener != null) {
                    PodCastLiteRoot root=((PodCastLiteRoot) data);
                    onSetDataPostedListener.onDataPosted(root.Root, (root.Root).size() == 0);
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

    public void getPodCastById(long dataId,long memberId)
    {
        String url= String.format(activity.getString(R.string.api_podcast_getid),
                MainApplication.getUrlApplApi(), dataId,memberId);
        PostDataModelUrl posData = new PostDataModelUrl();
        posData.execute(url, PodCast.class);
        posData.setOnDataPostedListener(new PostDataModelUrl.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data) {
                if (data==null) {
                    if (onSetDataPostedListener != null) {
                        onSetDataPostedListener.onDataError();
                    }
                    Toast.makeText(activity,activity.getResources().getString(R.string.fetch_data_problem),Toast.LENGTH_SHORT).show();
                    return;
                }

                if (onSetDataPostedListener != null) {
                    onSetDataPostedListener.onDataPosted(data, true);
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
