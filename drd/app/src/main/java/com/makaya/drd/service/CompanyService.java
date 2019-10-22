package com.makaya.drd.service;

import android.app.Activity;
import android.os.Handler;
import android.widget.Toast;

import com.makaya.drd.MainApplication;
import com.makaya.drd.R;
import com.makaya.drd.domain.Company;
import com.makaya.drd.domain.SubscriptionRegistry;
import com.makaya.drd.domain.SubscriptionRegistryResult;
import com.makaya.drd.library.DisplayToast;

/**
 * Created by xbudi on 17/10/2017.
 */

public class CompanyService {

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

    public CompanyService(Activity activity)
    {
        this.activity=activity;
        global=(MainApplication)activity.getApplication();
        handler=new Handler();
    }


    public void getByCode(String code)
    {
        String url= String.format(activity.getString(R.string.api_get_companycode),
                MainApplication.getUrlApplApi(), code);
        PostDataModelUrl posData = new PostDataModelUrl();
        posData.execute(url, Company.class);
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
                    onSetDataPostedListener.onDataPosted(data, true);
                }

            }

            @Override
            public <T> void onPostedError(Exception data) {
                if (onSetDataPostedListener != null) {
                    onSetDataPostedListener.onDataError();
                }
                handler.post(new DisplayToast(activity, activity.getResources().getString(R.string.fetch_data_problem)));            }
        });
    }

    public void saveRegistration(SubscriptionRegistry registry)
    {
        String url= String.format(activity.getString(R.string.api_save_registry),
                MainApplication.getUrlApplWeb());
        PostDataModelUrl posData = new PostDataModelUrl();
        posData.execute(url, registry, SubscriptionRegistryResult.class);
        posData.setOnDataPostedListener(new PostDataModelUrl.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data) {
                if (data==null) {
                    if (onSetDataPostedListener != null) {
                        onSetDataPostedListener.onDataError();
                    }
                    handler.post(new DisplayToast(activity, activity.getResources().getString(R.string.save_data_problem)));                    return;
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
                handler.post(new DisplayToast(activity, activity.getResources().getString(R.string.save_data_problem)));                    return;
            }
        });
    }
}
