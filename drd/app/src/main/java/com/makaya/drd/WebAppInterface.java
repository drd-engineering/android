package com.makaya.drd;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.makaya.drd.service.DepositTrxService;

public class WebAppInterface {
    // MY EVENT HANDLER
    private OnSelectedPostedListener onSetSelectedListener;
    public interface OnSelectedPostedListener {
        public <T> void onSelected(String tbtype, String data);
    }
    public void setOnSelectedListener(OnSelectedPostedListener listener) {
        onSetSelectedListener = listener;
    }

    private OnReleasedPostedListener onSetReleasedListener;
    public interface OnReleasedPostedListener {
        public <T> void onRelease();
    }
    public void setOnReleasedListener(OnReleasedPostedListener listener) {
        onSetReleasedListener = listener;
    }

    private OnPdfRenderPostedListener onSetPdfRenderListener;
    public interface OnPdfRenderPostedListener {
        public <T> void onPdfRender(int page);
    }
    public void setOnPdfRenderListener(OnPdfRenderPostedListener listener) {
        onSetPdfRenderListener = listener;
    }

    private OnSavePostedListener onSetSaveListener;
    public interface OnSavePostedListener {
        public <T> void onSaved();
    }
    public void setOnSaveListener(OnSavePostedListener listener) {
        onSetSaveListener = listener;
    }
    // /MY EVENT HANDLER

    Context mContext;
    String msg;

    /** Instantiate the interface and set the context */
    WebAppInterface(Context c) {
        mContext = c;
    }

    /** Show a toast from the web page */
    @JavascriptInterface
    public void showToast(String toast) {
        msg=toast;
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }

    @JavascriptInterface
    public void doAndroidRelease() {
        if (onSetReleasedListener!=null)
            onSetReleasedListener.onRelease();
    }

    @JavascriptInterface
    public void doAndroidSelect(String tbtype, String data) {
        if (onSetSelectedListener!=null)
            onSetSelectedListener.onSelected(tbtype, data);
    }

    @JavascriptInterface
    public void doAndroidPdfRender(int page) {
        if (onSetPdfRenderListener!=null)
            onSetPdfRenderListener.onPdfRender(page);
    }

    @JavascriptInterface
    public void doAndroidSaved() {
        if (onSetSaveListener!=null)
            onSetSaveListener.onSaved();
    }
    public String getMessage(){
        return msg;
    }
}