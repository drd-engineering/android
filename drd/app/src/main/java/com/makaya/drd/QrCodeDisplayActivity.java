package com.makaya.drd;

import android.app.Activity;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.ImageView;
import android.widget.TextView;

import com.makaya.drd.domain.MemberLogin;
import com.makaya.drd.library.PublicFunction;
import com.squareup.picasso.Picasso;

/**
 * Created by xbudi on 10/12/2016.
 */

public class QrCodeDisplayActivity extends AppCompatActivity {
    Activity activity;
    SessionManager session;
    MemberLogin user;
    ImageView imgQrCode;
    TextView regNumber;
    TextView clickqr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_code_display);
        activity = this;
        session=new SessionManager(getApplication());
        user=session.getUserLogin();

        PublicFunction.setHeaderStatus(activity,"Member Qr Code");
        bindObject();
    }

    private void bindObject()
    {
        clickqr=findViewById(R.id.clickqr);
        imgQrCode=findViewById(R.id.imgQrCode);
        regNumber=findViewById(R.id.regNumber);

        regNumber.setText(user.Number);

        if (user.ImageQrCode!=null){
            String path = MainApplication.getUrlApplWeb() + "/Images/member/qrbarcode/" + user.ImageQrCode;
            Picasso.with(activity)
                    .load(path)
                    .placeholder(R.drawable.qrbarcode)
                    .error(R.drawable.qrbarcode)
                    .into(imgQrCode);
        }

    }

}
