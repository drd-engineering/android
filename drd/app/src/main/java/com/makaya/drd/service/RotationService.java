package com.makaya.drd.service;

import android.app.Activity;
import android.os.Handler;

import com.makaya.drd.MainApplication;
import com.makaya.drd.R;
import com.makaya.drd.domain.ProcessActivity;
import com.makaya.drd.domain.Rotation;
import com.makaya.drd.domain.RotationLiteRoot;
import com.makaya.drd.domain.RotationRoot;
import com.makaya.drd.library.DisplayToast;

/**
 * Created by xbudi on 28/09/2017.
 */

public class RotationService {

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

    public RotationService(Activity activity)
    {
        this.activity=activity;
        global=(MainApplication)activity.getApplication();
        handler=new Handler();
    }

    public void getById(long rotationId, long memberId)
    {
        String url= String.format(activity.getString(R.string.api_rotation_byid),
                global.getUrlApplApi(), rotationId, memberId);

        PostDataModelUrl posData = new PostDataModelUrl();
        posData.execute(url, Rotation.class);
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
                    onSetDataPostedListener.onDataPosted(data, true);
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

    public void getByMemberId(long memberId, String status, String topCriteria, int page)
    {
        String url= String.format(activity.getString(R.string.api_rotation_progress_member),
                global.getUrlApplApi(), memberId, status, topCriteria, page, MainApplication.getPageSize());

        PostDataModelUrl posData = new PostDataModelUrl();
        posData.execute(url, RotationLiteRoot.class);
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
                    RotationLiteRoot root=((RotationLiteRoot) data);
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

    public void getNodeByMemberId(long memberId, String status, String topCriteria, int page)
    {
        String url= String.format(activity.getString(R.string.api_rotation_progressnode_member),
                global.getUrlApplApi(), memberId, status,topCriteria,page,MainApplication.getPageSize());

        PostDataModelUrl posData = new PostDataModelUrl();
        posData.execute(url, RotationLiteRoot.class);
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
                    RotationLiteRoot root=((RotationLiteRoot) data);
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

    public void getInboxByMemberId(long memberId)
    {
        String url= String.format(activity.getString(R.string.api_rotation_inbox_member),
                global.getUrlApplApi(), memberId);

        PostDataModelUrl posData = new PostDataModelUrl();
        posData.execute(url, RotationRoot.class);
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
                    RotationRoot root=((RotationRoot) data);
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

    public void getNodeById(long id)
    {
        String url= String.format(activity.getString(R.string.api_rotation_inbox_node),
                global.getUrlApplApi(), id);

        PostDataModelUrl posData = new PostDataModelUrl();
        posData.execute(url, Rotation.class);
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
                    onSetDataPostedListener.onDataPosted(data, true);
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

    public void processActivity(ProcessActivity param, int bit)
    {
        String url= String.format(activity.getString(R.string.api_rotation_process),
                global.getUrlApplApi(), bit);

        PostDataModelUrl posData = new PostDataModelUrl();
        posData.execute(url, param, Integer.class);
        posData.setOnDataPostedListener(new PostDataModelUrl.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data) {
                if (data==null){
                    if (onSetDataPostedListener != null) {
                        onSetDataPostedListener.onDataError();
                    }
                    handler.post(new DisplayToast(activity, activity.getResources().getString(R.string.save_data_problem)));
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
                handler.post(new DisplayToast(activity, activity.getResources().getString(R.string.save_data_problem)));
            }
        });
    }

    public void checkingSignature(long memberId)
    {
        String url= String.format(activity.getString(R.string.api_document_checksignature),
                global.getUrlApplWeb(), memberId);

        PostDataModelUrl posData = new PostDataModelUrl();
        posData.execute(url, memberId, Integer.class);
        posData.setOnDataPostedListener(new PostDataModelUrl.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data) {
                if (data==null){
                    if (onSetDataPostedListener != null) {
                        onSetDataPostedListener.onDataError();
                    }
                    handler.post(new DisplayToast(activity, activity.getResources().getString(R.string.save_data_problem)));
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
                handler.post(new DisplayToast(activity, activity.getResources().getString(R.string.save_data_problem)));
            }
        });
    }
    public void checkingPrivateStamp(long memberId)
    {
        String url= String.format(activity.getString(R.string.api_document_checkprivatestamp),
                global.getUrlApplWeb(), memberId);

        PostDataModelUrl posData = new PostDataModelUrl();
        posData.execute(url, memberId, Integer.class);
        posData.setOnDataPostedListener(new PostDataModelUrl.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data) {
                if (data==null){
                    if (onSetDataPostedListener != null) {
                        onSetDataPostedListener.onDataError();
                    }
                    handler.post(new DisplayToast(activity, activity.getResources().getString(R.string.save_data_problem)));
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
                handler.post(new DisplayToast(activity, activity.getResources().getString(R.string.save_data_problem)));
            }
        });
    }

}
