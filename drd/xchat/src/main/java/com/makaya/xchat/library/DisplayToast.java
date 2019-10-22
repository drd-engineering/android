package com.makaya.xchat.library;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by xbudi on 10/01/2018.
 */

public class DisplayToast implements Runnable  {
    private final Activity activity;
    String mText;

    public DisplayToast(Activity activity, String text){
        this.activity = activity;
        mText = text;
    }

    public void run(){
        Toast.makeText(activity, mText, Toast.LENGTH_SHORT).show();
    }
}
