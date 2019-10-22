package com.makaya.drd.service;

import android.app.Activity;
import android.os.Handler;

import com.makaya.drd.MainApplication;
import com.makaya.drd.R;
import com.makaya.drd.domain.Document;
import com.makaya.drd.domain.DocumentLiteRoot;
import com.makaya.drd.domain.DocumentRoot;
import com.makaya.drd.domain.Rotation;
import com.makaya.drd.domain.RotationRoot;
import com.makaya.drd.library.DisplayToast;

/**
 * Created by xbudi on 28/09/2017.
 */

public class DocumentService {

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

    public DocumentService(Activity activity)
    {
        this.activity=activity;
        global=(MainApplication)activity.getApplication();
        handler=new Handler();
    }

    public void getLite(long memberId, String topCriteria, int current_page, String sortdata,String filterdata)
    {
        String url= String.format(activity.getString(R.string.api_document_company),
                global.getUrlApplApi(), memberId, topCriteria, current_page,
                MainApplication.getPageSize(), sortdata, filterdata);

        PostDataModelUrl posData = new PostDataModelUrl();
        posData.execute(url, DocumentLiteRoot.class);
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
                    DocumentLiteRoot root=((DocumentLiteRoot) data);
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

    public void getDocument(long memberId, long rotationNodeId, long documentId)
    {
        String url= String.format(activity.getString(R.string.api_document),
                global.getUrlApplApi(), memberId, rotationNodeId, documentId);

        PostDataModelUrl posData = new PostDataModelUrl();
        posData.execute(url, Document.class);
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

    public void getDocumentPermission(long memberId, long rotationNodeId, long documentId)
    {
        String url= String.format(activity.getString(R.string.api_document_permission),
                global.getUrlApplApi(), memberId, rotationNodeId, documentId);

        PostDataModelUrl posData = new PostDataModelUrl();
        posData.execute(url, Integer.class);
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

}
