package com.makaya.drd;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.makaya.drd.domain.MemberLogin;
import com.makaya.drd.library.DisplayToast;
import com.makaya.drd.library.PublicFunction;
import com.makaya.drd.service.PostDataModelUrl;

/**
 * Created by xbudi on 02/10/2016.
 */


public class ChangePasswordActivity extends AppCompatActivity {
    EditText oldpassword;
    EditText newpassword;
    EditText confirmpassword;

    Context context;
    Activity activity;
    MainApplication global;
    SessionManager session;
    MemberLogin user;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_pwd);
        global=(MainApplication)getApplication();
        context=getBaseContext();
        activity=this;
        handler=new Handler();

        session=new SessionManager(getApplication());
        user=session.getUserLogin();

        PublicFunction.setBackgroundColorPage(activity,new int[]{R.id.layout});
        PublicFunction.setHeaderStatus(activity,"Change Password");

        oldpassword=(EditText)findViewById(R.id.oldpassword);
        newpassword=(EditText)findViewById(R.id.newpassword);
        confirmpassword=(EditText)findViewById(R.id.confirmpassword);

        Button btnSubmit=(Button)findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });
    }

    private void submitForm()
    {
        if (oldpassword.getText().toString().trim().isEmpty()){
            Toast.makeText(context,"Old password is not yet filled!",Toast.LENGTH_SHORT).show();
            oldpassword.requestFocus();
            return;
        }
        if (newpassword.getText().toString().trim().isEmpty()){
            Toast.makeText(context,"New password is not yet filled!",Toast.LENGTH_SHORT).show();
            newpassword.requestFocus();
            return;
        }
        if (confirmpassword.getText().toString().trim().isEmpty()){
            Toast.makeText(context,"Confirmation password is not yet filled!",Toast.LENGTH_SHORT).show();
            confirmpassword.requestFocus();
            return;
        }
        if (!newpassword.getText().toString().equals(confirmpassword.getText().toString())){
            Toast.makeText(context,"Password and confirmation password is not match!",Toast.LENGTH_SHORT).show();
            newpassword.requestFocus();
            return;
        }
        /*if (!oldpassword.getText().toString().equals(user.Password)){
            Toast.makeText(context,"Current password not valid!!",Toast.LENGTH_SHORT).show();
            oldpassword.requestFocus();
            return;
        }
        if (oldpassword.getText().toString().equals(newpassword.getText().toString())){
            Toast.makeText(context,"The new password is the same as the old password. Please repeat.",Toast.LENGTH_SHORT).show();
            oldpassword.requestFocus();
            return;
        }*/

        //global.getUserLogin().Password=newpassword.getText().toString();

        PostDataModelUrl postData=new PostDataModelUrl();
        String url=String.format(context.getString(R.string.api_member_changepwd),
                MainApplication.getUrlApplApi(),user.Id, oldpassword.getText().toString(), newpassword.getText().toString());

        postData.execute(url,Integer.class);
        postData.setOnDataPostedListener(new PostDataModelUrl.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data) {
                if (data!=null) {
                    int val=Integer.parseInt(data.toString());
                    if (val==-1) {
                        handler.post(new DisplayToast(activity, "Current password not valid!!"));
                        return;
                    } else/* if (val==1)*/ {
                        //user.Password=newpassword.getText().toString();
                        //session.LoginUser(user);
                        PublicFunction.hideKeyboard(activity);
                        finish();
                        handler.post(new DisplayToast(activity, "Change password success!"));
                        return;
                    }
                }
                handler.post(new DisplayToast(activity, activity.getResources().getString(R.string.save_data_problem)));
            }

            @Override
            public <T> void onPostedError(Exception data) {
                handler.post(new DisplayToast(activity, activity.getResources().getString(R.string.save_data_problem)));
            }
        });

    }

}
