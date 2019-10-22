package com.makaya.drd;

import android.app.Activity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.makaya.drd.domain.MemberLogin;
import com.makaya.drd.library.PublicFunction;

public class MainActivity extends AppCompatActivity {
    MainApplication global;
    SessionManager session;
    MemberLogin user;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity=this;
        global=(MainApplication)getApplication();
        session=new SessionManager(getApplication());
        user=session.getUserLogin();

        TextView title = (TextView) findViewById(R.id.appBarTitle);
        title.setText("Member Account");
        PublicFunction.setStatusBarColor(activity);
    }

    private void initObject()
    {

    }

    private void bindObject()
    {

    }
}
