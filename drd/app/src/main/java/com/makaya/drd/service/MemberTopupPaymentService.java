package com.makaya.drd.service;

import android.app.Activity;
import android.os.Handler;
import android.widget.Toast;

import com.makaya.drd.MainApplication;
import com.makaya.drd.R;
import com.makaya.drd.domain.MemberTopupPayment;
import com.makaya.drd.library.DisplayToast;
import com.makaya.drd.library.PublicFunction;

/**
 * Created by xbudi on 28/02/2017.
 */

public class MemberTopupPaymentService {

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

    public MemberTopupPaymentService(Activity activity)
    {
        this.activity=activity;
        global=(MainApplication)activity.getApplication();
        handler=new Handler();
    }


    public void saveData(MemberTopupPayment pay)
    {
        String url= String.format(activity.getString(R.string.api_topuppay_save),
                global.getUrlApplApi());

        String ssss= PublicFunction.classToJson(pay);

        PostDataModelUrl posData = new PostDataModelUrl();
        posData.execute(url, pay, MemberTopupPayment.class);
        posData.setOnDataPostedListener(new PostDataModelUrl.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data) {
                if (data==null){
                    if (onSetDataPostedListener != null) {
                        onSetDataPostedListener.onDataError();
                    }
                    Toast.makeText(activity,activity.getResources().getString(R.string.save_data_problem),Toast.LENGTH_SHORT).show();
                    return;
                }
                if (onSetDataPostedListener != null) {
                    onSetDataPostedListener.onDataPosted((MemberTopupPayment) data, true);
                }
                //Toast.makeText(activity,activity.getResources().getString(R.string.save_data_success),Toast.LENGTH_SHORT).show();
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

    public void updateStatus(long id, String status)
    {
        String url= String.format(activity.getString(R.string.api_topuppay_updatestatus),
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
                    handler.post(new DisplayToast(activity, activity.getResources().getString(R.string.save_data_problem)));                    return;
                }
                if (onSetDataPostedListener != null) {
                    onSetDataPostedListener.onDataPosted((Integer) data, true);
                }
                handler.post(new DisplayToast(activity, activity.getResources().getString(R.string.save_data_success)));                    return;
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
