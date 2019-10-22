package com.makaya.drdamin;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_progress);
        dialog=this;

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
        TextView textProgress=(TextView)findViewById(R.id.textProgress);
        textProgress.setText(text);
    }
}
