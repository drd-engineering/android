package com.makaya.drd;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.makaya.drd.domain.Company;
import com.makaya.drd.domain.MemberLogin;
import com.makaya.drd.service.PostDataModelUrl;

/**
 * Created by xbudi on 02/10/2016.
 */
public class UserLoginActivity extends AppCompatActivity {

    Activity activity;
    SessionManager session;
    MemberLogin user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window w = getWindow();
        w.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        w.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        activity=this;
        session=new SessionManager(getApplication());
        user=session.getUserLogin();
        setContentView(R.layout.user_login);
        Company zone=(Company)getIntent().getSerializableExtra("AppZone");
        /*if (zone!=null) {
            LinearLayout layout=findViewById(R.id.layout);
            PublicFunction2.setBackgroundColorGradient(layout, zone.BackColorBar, "#e5e3e3");
        }*/
        runAll();

    }

    void runAll()
    {
        session.setOnSessionListener(new SessionManager.OnSetSessionListener() {
            @Override
            public <T> void onDataPosted(T data) {
                if (data==null) {
                    return;
                }
                viewDashboard((MemberLogin)data);
            }
        });

        TextView register=(TextView)findViewById(R.id.register);
        register.setPaintFlags(register.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(activity, TermConditionActivity.class);
                activity.startActivity(intent);
            }
        });

        TextView resetpwd=(TextView)findViewById(R.id.resetpwd);
        resetpwd.setPaintFlags(resetpwd.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        resetpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activity, ResetPassword.class);
                activity.startActivity(intent);
            }
        });

        final EditText userId=(EditText)findViewById(R.id.userId);
        final EditText password=(EditText)findViewById(R.id.password);

        final Button btnSubmit=(Button)findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (userId.getText().toString().trim().isEmpty()){
                    Toast.makeText(getBaseContext(),"User Id data is not yet filled!",Toast.LENGTH_SHORT).show();
                    userId.requestFocus();
                    return;
                }

                if (password.getText().toString().trim().isEmpty()){
                    Toast.makeText(getBaseContext(),"Password data is not yet filled!",Toast.LENGTH_SHORT).show();
                    password.requestFocus();
                    return;
                }

                btnSubmit.setText("Verifying...");
                btnSubmit.setEnabled(false);

                String url=String.format(activity.getString(R.string.api_member_login),
                        MainApplication.getUrlApplApi(),userId.getText().toString(), password.getText().toString());
                final PostDataModelUrl posData = new PostDataModelUrl();
                posData.execute(url, MemberLogin.class);
                posData.setOnDataPostedListener(new PostDataModelUrl.OnSetDataPostedListener() {
                    @Override
                    public <T> void onDataPosted(T data) {
                        btnSubmit.setText("Login");
                        btnSubmit.setEnabled(true);
                        if (data==null)
                        {
                            Toast.makeText(activity,"Invalid user and password. Please try again.",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        MemberLogin uc =(MemberLogin) data;
                        if (uc.Id!=0) {

                            session.LoginUser(uc);
                            View view = activity.getCurrentFocus();
                            if (view != null) {
                                InputMethodManager imm = (InputMethodManager)getSystemService(activity.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                            }

                            Toast.makeText(activity, "Login success!", Toast.LENGTH_SHORT).show();

                            viewDashboard(uc);
                        }else
                            Toast.makeText(activity,"Invalid user and password. Please try again.",Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public <T> void onPostedError(Exception data) {
                        btnSubmit.setText("Login");
                        btnSubmit.setEnabled(true);
                        Toast.makeText(activity,"Login problem. Please try again.",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    void viewDashboard(MemberLogin mem)
    {
        Intent intent = new Intent(UserLoginActivity.this, DashboardActivity.class);
        //Intent intent = new Intent(UserLoginActivity.this, IdentityPageActivity.class);
        startActivity(intent);

        // close this activity
        finish();
    }

}
