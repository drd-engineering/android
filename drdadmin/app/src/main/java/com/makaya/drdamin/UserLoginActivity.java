package com.makaya.drdamin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.makaya.drdamin.domain.UserAdmin;
import com.makaya.drdamin.service.PostDataModelUrl;

/**
 * Created by xbudi on 06/03/2017.
 */

public class UserLoginActivity extends AppCompatActivity {
    //ProgressBar progressBar;

    Activity activity;
    //CardView cvlogin;
    MainApplication global;
    SessionManager session;
    //UserAdmin user;

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
        global=(MainApplication)getApplication();
        session=new SessionManager(global);
        //user=session.getUserLogin();
        //if (!global.getIsRunning()) {
            setContentView(R.layout.admin_login);
            runAll();
        /*}else
            finish();*/
    }

    void runAll()
    {
        /*progressBar=(ProgressBar)findViewById(R.id.progressBar);
        cvlogin=(CardView)findViewById(R.id.cvlogin);

        cvlogin.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);*/

        session.setOnSessionListener(new SessionManager.OnSetSessionListener() {
            @Override
            public <T> void onDataPosted(T data) {
                if (data==null) {
                    /*cvlogin.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);*/
                    return;
                }
                //progressBar.setVisibility(View.VISIBLE);

                viewDashboard();
            }
        });

        //global.getSession().initLoginStatus();

        final EditText userId=(EditText)findViewById(R.id.userId);
        final EditText password=(EditText)findViewById(R.id.password);

        final Button btnSubmit=(Button)findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (userId.getText().toString().trim().isEmpty()){
                    Toast.makeText(getBaseContext(),"Email is not yet filled!", Toast.LENGTH_SHORT).show();
                    userId.requestFocus();
                    return;
                }

                if (password.getText().toString().trim().isEmpty()){
                    Toast.makeText(getBaseContext(),"Password data is not yet filled!", Toast.LENGTH_SHORT).show();
                    password.requestFocus();
                    return;
                }

                btnSubmit.setText("Verifying...");
                btnSubmit.setEnabled(false);

                String url= String.format(activity.getString(R.string.api_useradmin_login),
                        MainApplication.getUrlLinkApi(),userId.getText().toString(), password.getText().toString());
                final PostDataModelUrl posData = new PostDataModelUrl();
                posData.execute(url, UserAdmin.class);
                posData.setOnDataPostedListener(new PostDataModelUrl.OnSetDataPostedListener() {
                    @Override
                    public <T> void onDataPosted(T data) {
                        btnSubmit.setText("Login");
                        btnSubmit.setEnabled(true);
                        if (data==null)
                        {
                            Toast.makeText(activity,"Invalid user and password. Please try again.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        UserAdmin init=(UserAdmin) data;

                        if (init.Id!=0) {

                            /*global.setUserLogin(init);
                            global.getSession().LoginUser(global.getUserLogin());*/
                            session.LoginUser(init);

                            View view = activity.getCurrentFocus();
                            if (view != null) {
                                InputMethodManager imm = (InputMethodManager)getSystemService(activity.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                            }

                            Toast.makeText(activity, "Login success!", Toast.LENGTH_SHORT).show();
                            /*cvlogin.setVisibility(View.GONE);
                            progressBar.setVisibility(View.VISIBLE);*/

                            viewDashboard();
                        }else
                            Toast.makeText(activity,"Invalid user and password. Please try again.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public <T> void onPostedError(Exception data) {
                        btnSubmit.setText("Login");
                        btnSubmit.setEnabled(true);
                        Toast.makeText(activity,"Invalid user and password. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }
    void viewDashboard()
    {
        Intent intent = new Intent(UserLoginActivity.this, DashboardActivity.class);
        startActivity(intent);
        // close this activity
        finish();
    }
}
