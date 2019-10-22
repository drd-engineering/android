package com.makaya.drd.service;

import android.app.Activity;
import android.os.Handler;

import com.makaya.drd.MainApplication;
import com.makaya.drd.R;
import com.makaya.drd.domain.MemberTopupDeposit;
import com.makaya.drd.domain.MemberTopupDepositRoot;
import com.makaya.drd.library.DisplayToast;
import com.makaya.drd.library.PublicFunction;


/**
 * Created by xbudi on 28/02/2017.
 */

public class MemberTopupDepositService {

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

    Handler handler;
    Activity activity;
    MainApplication global;

    public MemberTopupDepositService(Activity activity)
    {
        this.activity=activity;
        global=(MainApplication)activity.getApplication();
        handler=new Handler();
    }

    public void fetchDataLite(long dataId, int current_page,String sortdata,String filterdata)
    {
        String url= String.format(activity.getString(R.string.api_topup_get),
                global.getUrlApplApi(), dataId, current_page, MainApplication.getPageSize(),sortdata,filterdata);

        PostDataModelUrl posData = new PostDataModelUrl();
        posData.execute(url, MemberTopupDepositRoot.class);
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
                    MemberTopupDepositRoot root=((MemberTopupDepositRoot) data);
                    onSetDataPostedListener.onDataPosted(root.Root, (root.Root).size() == 0);
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

    public void fetchData(long dataId)
    {
        String url= String.format(activity.getString(R.string.api_topup_get_id),
                MainApplication.getUrlApplApi(), dataId);
        PostDataModelUrl posData = new PostDataModelUrl();
        posData.execute(url, MemberTopupDeposit.class);
        posData.setOnDataPostedListener(new PostDataModelUrl.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data) {
                if (data==null) {
                    if (onSetDataPostedListener != null) {
                        onSetDataPostedListener.onDataError();
                    }
                    handler.post(new DisplayToast(activity, activity.getResources().getString(R.string.fetch_data_problem)));
                    return;
                }

                if (onSetDataPostedListener != null) {
                    onSetDataPostedListener.onDataPosted((MemberTopupDeposit)data, true);
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

    public void saveData(MemberTopupDeposit topup)
    {
        String url= String.format(activity.getString(R.string.api_topup_save),
                global.getUrlApplApi());

        String ssss= PublicFunction.classToJson(topup);

        PostDataModelUrl posData = new PostDataModelUrl();
        posData.execute(url, topup, MemberTopupDeposit.class);
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
                    onSetDataPostedListener.onDataPosted((MemberTopupDeposit) data, true);
                }
                handler.post(new DisplayToast(activity, activity.getResources().getString(R.string.save_data_success)));
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

    public void updateStatus(long id, String status)
    {
        String url= String.format(activity.getString(R.string.api_topup_updatestatus),
                global.getUrlApplApi(), id, status);

        //String ssss=PublicFunction2.classToJson(assist);

        PostDataModelUrl posData = new PostDataModelUrl();
        posData.execute(url, Integer.class);
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
                    onSetDataPostedListener.onDataPosted((Integer) data, true);
                }
                handler.post(new DisplayToast(activity, activity.getResources().getString(R.string.save_data_success)));
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
