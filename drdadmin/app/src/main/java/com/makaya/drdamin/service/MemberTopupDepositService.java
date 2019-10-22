package com.makaya.drdamin.service;

import android.app.Activity;
import android.widget.Toast;

import com.makaya.drdamin.MainApplication;
import com.makaya.drdamin.R;
import com.makaya.drdamin.domain.MemberTopupDeposit;
import com.makaya.drdamin.domain.MemberTopupDepositRoot;

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

    Activity activity;
    MainApplication global;

    public MemberTopupDepositService(Activity activity)
    {
        this.activity=activity;
        global=(MainApplication)activity.getApplication();
    }

    public void fetchDataLite(long dataId, int current_page,String sortdata,String filterdata)
    {
        String url= String.format(activity.getString(R.string.api_memtopup_get),
                global.getUrlLinkApi(), dataId, current_page, MainApplication.getPageSize(),sortdata,filterdata);

        PostDataModelUrl posData = new PostDataModelUrl();
        posData.execute(url, MemberTopupDepositRoot.class);
        posData.setOnDataPostedListener(new PostDataModelUrl.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data) {
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
                Toast.makeText(activity,activity.getResources().getString(R.string.fetch_data_problem),Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void fetchDataCommand(String command, int current_page,String sortdata,String filterdata)
    {
        String url= String.format(activity.getString(R.string.api_memtopup_get_command),
                global.getUrlLinkApi(), command, current_page, MainApplication.getPageSize(),sortdata,filterdata);

        PostDataModelUrl posData = new PostDataModelUrl();
        posData.execute(url, MemberTopupDepositRoot.class);
        posData.setOnDataPostedListener(new PostDataModelUrl.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data) {
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
                Toast.makeText(activity,activity.getResources().getString(R.string.fetch_data_problem),Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void fetchData(long dataId)
    {
        String url= String.format(activity.getString(R.string.api_memtopup_get_id),
                MainApplication.getUrlLinkApi(), dataId);
        PostDataModelUrl posData = new PostDataModelUrl();
        posData.execute(url, MemberTopupDeposit.class);
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
                    onSetDataPostedListener.onDataPosted((MemberTopupDeposit)data, true);
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

    public void saveData(MemberTopupDeposit topup)
    {
        String url= String.format(activity.getString(R.string.api_memtopup_save),
                global.getUrlLinkApi());

        //String ssss=PublicFunction.classToJson(topup);

        PostDataModelUrl posData = new PostDataModelUrl();
        posData.execute(url, topup, MemberTopupDeposit.class);
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
                    onSetDataPostedListener.onDataPosted((MemberTopupDeposit) data, true);
                }
                Toast.makeText(activity,activity.getResources().getString(R.string.save_data_success),Toast.LENGTH_SHORT).show();
            }

            @Override
            public <T> void onPostedError(Exception data) {
                if (onSetDataPostedListener != null) {
                    onSetDataPostedListener.onDataError();
                }
                Toast.makeText(activity,activity.getResources().getString(R.string.save_data_problem),Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateStatus(long id, String status)
    {
        String url= String.format(activity.getString(R.string.api_memtopup_updatestatus),
                global.getUrlLinkApi(), id, status);

        //String ssss=PublicFunction.classToJson(assist);

        PostDataModelUrl posData = new PostDataModelUrl();
        posData.execute(url, Integer.class);
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
                    onSetDataPostedListener.onDataPosted((Integer) data, true);
                }
                Toast.makeText(activity,activity.getResources().getString(R.string.save_data_success),Toast.LENGTH_SHORT).show();
            }

            @Override
            public <T> void onPostedError(Exception data) {
                if (onSetDataPostedListener != null) {
                    onSetDataPostedListener.onDataError();
                }
                Toast.makeText(activity,activity.getResources().getString(R.string.save_data_problem),Toast.LENGTH_SHORT).show();
            }
        });
    }

}
