package com.makaya.xchat.service;

import android.app.Activity;
import android.os.Handler;

import com.makaya.xchat.MainApplication;
import com.makaya.xchat.R;
import com.makaya.xchat.domain.MemberLiteRoot;
import com.makaya.xchat.domain.MessageSumClass;
import com.makaya.xchat.domain.MessageSumContent;
import com.makaya.xchat.domain.MessageSumDetailContent;
import com.makaya.xchat.library.DisplayToast;
import com.makaya.xchat.library.PostDataModelUrl;

import java.util.ArrayList;

/**
 * Created by xbudi on 28/09/2017.
 */

public class MessageService {

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
    MainApplication main;
    Handler handler;

    public MessageService(Activity activity)
    {
        this.activity=activity;
        main=new MainApplication(activity);
        handler=new Handler();
    }


    public void fetchDataSummary(long userId, long maxId, String topCriteria, int page)
    {
        String url=String.format(activity.getString(R.string.api_get_msg_sum),
                main.getUrlApplApi(), userId, maxId, topCriteria, page, main.getPageSize());
        final PostDataModelUrl posData = new PostDataModelUrl();
        posData.execute(url, MessageSumContent.class);
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
                    MessageSumContent root=((MessageSumContent) data);
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

    public void fetchChat(long meId, long yourId, int page)
    {
        String url = String.format(activity.getString(R.string.api_get_msg_sum_detail2),
                main.getUrlApplApi(), meId, yourId, page, main.getPageSize());
        final PostDataModelUrl posData = new PostDataModelUrl();
        posData.execute(url, MessageSumDetailContent.class);
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
                    MessageSumDetailContent root=((MessageSumDetailContent) data);
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

    public void fetchNewChat(long meId, long yourId)
    {
        String url = String.format(activity.getString(R.string.api_get_msg_new),
                main.getUrlApplApi(), meId, yourId);
        final PostDataModelUrl posData = new PostDataModelUrl();
        posData.execute(url, MessageSumDetailContent.class);
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
                    MessageSumDetailContent root=((MessageSumDetailContent) data);
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

    public void fetchContact(long userId, String topCriteria, int page)
    {
        String url=String.format(activity.getString(R.string.api_member_contact),
                main.getUrlApplApi(), userId, topCriteria, page, main.getPageSize());
        final PostDataModelUrl posData = new PostDataModelUrl();
        posData.execute(url, MemberLiteRoot.class);
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
                    MemberLiteRoot root=((MemberLiteRoot) data);
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

    public void fetchAdvisor()
    {
        String url=String.format(activity.getString(R.string.api_member_advisor),
                main.getUrlApplApi());
        final PostDataModelUrl posData = new PostDataModelUrl();
        posData.execute(url, MemberLiteRoot.class);
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
                    MemberLiteRoot root=((MemberLiteRoot) data);
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
