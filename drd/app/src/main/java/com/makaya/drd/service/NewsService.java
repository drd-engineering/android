package com.makaya.drd.service;

import android.app.Activity;
import android.widget.Toast;

import com.makaya.drd.MainApplication;
import com.makaya.drd.R;
import com.makaya.drd.domain.News;
import com.makaya.drd.domain.NewsLiteRoot;
import com.makaya.drd.domain.NewsVideo;
import com.makaya.drd.domain.NewsVideoRoot;


/**
 * Created by xbudi on 28/02/2017.
 */

public class NewsService {

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

    public NewsService(Activity activity)
    {
        this.activity=activity;
        global=(MainApplication)activity.getApplication();
    }

    public void fetchNewsLite(long memberId, int type, String topCriteria, int current_page, String sortdata, String filterdata)
    {
        String url= String.format(activity.getString(R.string.api_news_getlite),
                global.getUrlApplApi(), memberId, type, topCriteria, current_page, global.getPageSize(),sortdata,filterdata);

        PostDataModelUrl posData = new PostDataModelUrl();
        posData.execute(url, NewsLiteRoot.class);
        posData.setOnDataPostedListener(new PostDataModelUrl.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data) {
                if (data==null){
                    if (onSetDataPostedListener != null) {
                        onSetDataPostedListener.onDataError();
                    }
                    Toast.makeText(activity,activity.getResources().getString(R.string.fetch_data_problem),Toast.LENGTH_SHORT).show();
                    return;
                }
                if (onSetDataPostedListener != null) {
                    NewsLiteRoot root=((NewsLiteRoot) data);
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

    public void fetchNewsLite2(long memberId, String types, String topCriteria, int current_page, String sortdata, String filterdata)
    {
        String url= String.format(activity.getString(R.string.api_news_getlite2),
                global.getUrlApplApi(), memberId, types, topCriteria, current_page, global.getPageSize(),sortdata,filterdata);

        PostDataModelUrl posData = new PostDataModelUrl();
        posData.execute(url, NewsLiteRoot.class);
        posData.setOnDataPostedListener(new PostDataModelUrl.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data) {
                if (data==null){
                    if (onSetDataPostedListener != null) {
                        onSetDataPostedListener.onDataError();
                    }
                    Toast.makeText(activity,activity.getResources().getString(R.string.fetch_data_problem),Toast.LENGTH_SHORT).show();
                    return;
                }
                if (onSetDataPostedListener != null) {
                    NewsLiteRoot root=((NewsLiteRoot) data);
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

    public void getNewsById(long dataId, long memberId)
    {
        String url= String.format(activity.getString(R.string.api_news_getid),
                MainApplication.getUrlApplApi(), dataId,memberId);
        PostDataModelUrl posData = new PostDataModelUrl();
        posData.execute(url, News.class);
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

    public void fetchNewsVideo(long memberId, String topCriteria, int current_page, String sortdata, String filterdata)
    {
        String url= String.format(activity.getString(R.string.api_newsvideo_get),
                global.getUrlApplApi(),memberId, topCriteria, current_page, global.getPageSize(),sortdata,filterdata);

        PostDataModelUrl posData = new PostDataModelUrl();
        posData.execute(url, NewsVideoRoot.class);
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
                    NewsVideoRoot root=((NewsVideoRoot) data);
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

    public void getNewsVideoById(long dataId, long memberId)
    {
        String url= String.format(activity.getString(R.string.api_newsvideo_getid),
                MainApplication.getUrlApplApi(), dataId,memberId);
        PostDataModelUrl posData = new PostDataModelUrl();
        posData.execute(url, NewsVideo.class);
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

    public void memberHitLogSave(long memberId, long dataId, MainApplication.enumDataHit hitType)
    {
        String url= String.format(activity.getString(R.string.api_memberhitlog_save),
                MainApplication.getUrlApplApi(), memberId,dataId,hitType.ordinal());
        PostDataModelUrl posData = new PostDataModelUrl();
        posData.execute(url, Integer.class);
        posData.setOnDataPostedListener(new PostDataModelUrl.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data) {
                if (data==null) {
                    if (onSetDataPostedListener != null) {
                        onSetDataPostedListener.onDataError();
                    }
                    //Toast.makeText(activity,activity.getResources().getString(R.string.fetch_data_problem),Toast.LENGTH_SHORT).show();
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
                //Toast.makeText(activity,activity.getResources().getString(R.string.fetch_data_problem),Toast.LENGTH_SHORT).show();
            }
        });
    }


}
