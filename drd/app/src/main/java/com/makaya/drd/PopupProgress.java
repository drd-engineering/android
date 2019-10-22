package com.makaya.drd;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

/**
 * Created by xbudi on 16/12/2016.
 */

public class PopupProgress extends Dialog {


    Activity activity;
    Dialog dialog;
    TextView textProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_progress);
        dialog=this;
        textProgress=findViewById(R.id.textProgress);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    public PopupProgress(Activity activity)
    {
        super(activity);
        this.activity = activity;
    }

    public void setTextProgress(String text){

        textProgress.setText(text);
    }
}
