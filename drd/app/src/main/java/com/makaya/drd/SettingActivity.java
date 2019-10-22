package com.makaya.drd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.makaya.drd.library.PublicFunction;

/**
 * Created by xbudi on 28/07/2017.
 */

public class SettingActivity extends AppCompatActivity {

    Activity activity;

    TextView profile;
    TextView account;
    TextView changepwd;
    TextView legality;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        activity = this;
        PublicFunction.setBackgroundColorPage(activity,new int[]{R.id.layout});
        PublicFunction.setHeaderStatus(activity,"Setting");
        initObject();
    }

    private void initObject()
    {
        profile=(TextView)findViewById(R.id.profile);
        account=(TextView)findViewById(R.id.account);
        changepwd=(TextView)findViewById(R.id.changepwd);
        legality=(TextView)findViewById(R.id.legality);

        /*profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, MemberProfileActivity.class);
                startActivity(intent);
            }
        });
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, MemberAccountListActivity.class);
                startActivity(intent);
            }
        });*/

        changepwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

    }
}
