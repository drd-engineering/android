package com.makaya.drd;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

/**
 * Created by xbudi on 29/10/2016.
 */


public class DialogBox {
    // MY EVENT HANDLER
    private OnSetDialogPositiveListener onSetDialogPositiveListener;
    public interface OnSetDialogPositiveListener {
        public <T> void onDialogPositive();
    }

    public void setOnSetDialogPositiveListener(OnSetDialogPositiveListener listener) {
        onSetDialogPositiveListener = listener;
    }

    private OnSetDialogNegativeListener onSetDialogNegativeListener;
    public interface OnSetDialogNegativeListener {
        public <T> void onDialogNegative();
    }

    public void setOnSetDialogNegativeListener(OnSetDialogNegativeListener listener) {
        onSetDialogNegativeListener = listener;
    }
    // /MY EVENT HANDLER

    public Activity activity;

    public DialogBox(Activity activity)
    {
        this.activity=activity;
    }

    public void show(String title, String message, String positive, String negative)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNegativeButton(negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (onSetDialogNegativeListener!=null)
                    onSetDialogNegativeListener.onDialogNegative();
            }
        });
        builder.setPositiveButton(positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (onSetDialogPositiveListener!=null)
                    onSetDialogPositiveListener.onDialogPositive();
            }
        });
        new Thread() {
            public void run() {
                activity.runOnUiThread(new Runnable(){

                    @Override
                    public void run(){
                        AlertDialog alert=builder.create();
                        alert.show();

                    }
                });
            }
        }.start();

    }

    public void showDialog(String title, String message, String positive, String negative)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNegativeButton(negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (onSetDialogNegativeListener!=null)
                    onSetDialogNegativeListener.onDialogNegative();
            }
        });
        builder.setPositiveButton(positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (onSetDialogPositiveListener!=null)
                    onSetDialogPositiveListener.onDialogPositive();
            }
        });
        AlertDialog alert=builder.create();
        alert.show();

    }
}
