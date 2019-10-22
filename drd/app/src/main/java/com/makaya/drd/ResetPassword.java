package com.makaya.drd;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.makaya.drd.service.PostDataModelUrl;
import com.makaya.drd.library.PublicFunction;

/**
 * Created by xbudi on 29/08/2017.
 */

public class ResetPassword extends AppCompatActivity {

    Activity activity;
    Button btnReset;
    EditText email;
    PopupProgress popupProgress;

    MainApplication global;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password);
        activity=this;

        PublicFunction.setHeaderStatus(activity,"Reset Password");

        email=(EditText)findViewById(R.id.email);
        btnReset=(Button)findViewById(R.id.btn_reset);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!PublicFunction.isEmailValid(email.getText().toString()) ||
                        email.getText().toString().trim().equals("")){
                    Toast.makeText(activity,"Not valid email address.",Toast.LENGTH_SHORT).show();
                    return;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Reset Password Confirmation");
                builder.setMessage("Are you sure do you want reset your password?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        resetPassword();
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });


    }

    private void resetPassword()
    {
        popupProgress=new PopupProgress(activity);
        popupProgress.show();
        PostDataModelUrl postData=new PostDataModelUrl();

        postData.execute(MainApplication.getUrlApplApi()+"/member/resetpwd?email="+
                email.getText().toString(),Integer.class);
        postData.setOnDataPostedListener(new PostDataModelUrl.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data) {
                popupProgress.dismiss();
                if (data==null){
                    Toast.makeText(activity, "Something problem! Please try again submit.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if ((Integer)data==0) {
                    Toast.makeText(activity, "Invalid member email.", Toast.LENGTH_SHORT).show();
                }else if ((Integer)data>0) {
                    finish();
                    Toast.makeText(activity, "Reset password success!", Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(activity, "Reset password problem, please check DRD valid email.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public <T> void onPostedError(Exception data) {
                popupProgress.dismiss();
                Toast.makeText(activity, "Something problem! Please try again submit.", Toast.LENGTH_SHORT).show();
                return;
            }
        });
    }
}
