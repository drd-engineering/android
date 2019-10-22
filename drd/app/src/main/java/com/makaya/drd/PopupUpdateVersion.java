package com.makaya.drd;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by xbudi on 16/12/2016.
 */

public class PopupUpdateVersion extends Dialog {

    Activity activity;
    Dialog dialog;

    TextView latestVersion;
    TextView descr;
    String newVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_update_version);
        dialog=this;

        descr=(TextView)findViewById(R.id.descr);
        latestVersion=(TextView)findViewById(R.id.latestVersion);
        String text=descr.getText().toString();
        text=text.replace("Google Play","<b>Google Play</b>");
        descr.setText(Html.fromHtml(text));
        latestVersion.setText("Latest Version "+newVersion);

        Button btnCancel=(Button)findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
                dismiss();
            }
        });

        Button btnUpdate=(Button)findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = activity.getPackageName(); // getPackageName() from Context or Activity object
                try {
                    activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://invoices?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/invoices?id=" + appPackageName)));
                }

                activity.finish();
                dismiss();
            }
        });
    }

    public PopupUpdateVersion(Activity activity, String newVersion)
    {
        super(activity);
        this.activity = activity;
        this.newVersion=newVersion;
    }
}
