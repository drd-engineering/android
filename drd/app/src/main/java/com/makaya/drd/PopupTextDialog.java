package com.makaya.drd;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by xbudi on 03/03/2017.
 */

public class PopupTextDialog extends Dialog {

    // MY EVENT HANDLER
    private OnSubmitListener onSubmitListener;
    public interface OnSubmitListener {
        public void onSubmit(String data);
    }

    public void setOnSubmitListener(OnSubmitListener listener) {
        onSubmitListener = listener;
    }
    // /MY EVENT HANDLER

    Dialog dialog;
    Activity activity;
    String text;
    EditText editText;
    Button btnCancel;
    Button btnSubmit;

    public PopupTextDialog(Activity activity)
    {
        super(activity);
        this.activity=activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_text);
        dialog=this;
        editText=findViewById(R.id.editText);
        btnCancel=findViewById(R.id.btnCancel);
        btnSubmit=findViewById(R.id.btnSubmit);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onSubmitListener!=null)
                    onSubmitListener.onSubmit(editText.getText().toString());
                dismiss();
            }
        });
    }

    public void setText(String text){
        editText.setText(text);
        editText.requestFocus();
        editText.selectAll();
    }
}
