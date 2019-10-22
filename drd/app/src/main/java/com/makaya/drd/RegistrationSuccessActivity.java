package com.makaya.drd;

import android.graphics.Paint;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.makaya.drd.domain.SubscriptionRegistryResult;

public class RegistrationSuccessActivity extends AppCompatActivity {

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

        setContentView(R.layout.registration_success);

        SubscriptionRegistryResult result=(SubscriptionRegistryResult)getIntent().getSerializableExtra("SubscriptionRegistryResult");

        TextView info=(TextView)findViewById(R.id.info);
        String strinfo=getString(R.string.register_success);
        strinfo=strinfo.replace("[_SUBCR_ID_]",result.SubscriptionId);
        strinfo=strinfo.replace("[_REG_NUMBER_]",result.UserNumber);
        strinfo=strinfo.replace("[_EMAIL_]",result.Email);
        info.setText(Html.fromHtml(strinfo));


        TextView btnClose=(TextView)findViewById(R.id.btnClose);
        btnClose.setPaintFlags(btnClose.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
