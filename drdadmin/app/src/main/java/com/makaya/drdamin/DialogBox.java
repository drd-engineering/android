package com.makaya.drdamin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

/**
 * Created by xbudi on 29/10/2016.
 */


public class DialogBox {
    // MY EVENT HANDLER
    private OnSetDialogListener onSetDialogListener;
    public interface OnSetDialogListener {
        public <T> void onDialogPositive();
        public <T> void onDialogNegative();
    }

    public void setOnSetDialogListener(OnSetDialogListener listener) {
        onSetDialogListener = listener;
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
                if (onSetDialogListener!=null)
                    onSetDialogListener.onDialogNegative();
            }
        });
        builder.setPositiveButton(positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (onSetDialogListener!=null)
                    onSetDialogListener.onDialogPositive();
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
                if (onSetDialogListener!=null)
                    onSetDialogListener.onDialogNegative();
            }
        });
        builder.setPositiveButton(positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (onSetDialogListener!=null)
                    onSetDialogListener.onDialogPositive();
            }
        });
        AlertDialog alert=builder.create();
        alert.show();

    }
}
