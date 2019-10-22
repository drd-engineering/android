package com.makaya.drd;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.makaya.drd.domain.DataList;

/**
 * Created by xbudi on 16/12/2016.
 */

public class PopupPassword extends Dialog {
    // MY EVENT HANDLER
    private OnButtonSelectedListener onButtonSelectedListener;
    public interface OnButtonSelectedListener {
        public void onSubmit(String pwd);
        public void onClose();
    }

    public void setOnButtonSelectedListener(OnButtonSelectedListener listener) {
        onButtonSelectedListener = listener;
    }
    // /MY EVENT HANDLER

    Activity activity;
    Dialog dialog;
    TextView password;
    Button close;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_password);
        dialog=this;
        password=findViewById(R.id.password);
        submit=findViewById(R.id.submit);
        close=findViewById(R.id.close);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onButtonSelectedListener!=null)
                    onButtonSelectedListener.onClose();
                dismiss();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onButtonSelectedListener!=null)
                    onButtonSelectedListener.onSubmit(password.getText().toString());
                dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public PopupPassword(Activity activity)
    {
        super(activity);
        this.activity = activity;
    }
}
