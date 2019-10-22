package com.makaya.drd;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.makaya.drd.service.UploadFile;

/**
 * Created by xbudi on 16/12/2016.
 */

public class PopupProgressUpload extends Dialog {

    // MY EVENT HANDLER

    private OnSetListener onSetListener;
    public interface OnSetListener {
        public <T> void onProcessCanceled();
    }

    public void setOnListener(OnSetListener listener) {
        onSetListener = listener;
    }

    // /MY EVENT HANDLER

    Activity activity;
    Dialog dialog;
    TextView title;
    TextView filename;
    TextView process;
    Button btnCancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_progress_upload);
        dialog=this;
        title=findViewById(R.id.title);
        filename=findViewById(R.id.filename);
        process=findViewById(R.id.process);
        btnCancel=findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onSetListener!=null)
                    onSetListener.onProcessCanceled();
            }
        });
        setCanceledOnTouchOutside(false);
        setCancelable(false);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    public PopupProgressUpload(Activity activity)
    {
        super(activity);
        this.activity = activity;
    }

    public void setTitle(String text){

        title.setText(text);
    }
    public void setProcess(String text){

        process.setText(text);
    }
    public void setFilename(String text){

        filename.setText(text);
    }
}
