package com.makaya.drd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.makaya.drd.library.PublicFunction;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by xbudi on 11/10/2016.
 */

public class MemberTopupDepositSuccessActivity extends AppCompatActivity {

    Activity activity;

    long dataTrxId;
    String dataTrxNo;
    String dataTrxDate;
    Double dataTrxAmount;
    Double dataTrxDisccount;
    Double dataTrxPaid;
    Double dataTrxOutstanding;
    String dataTrxType;
    String dataTrxDescr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_topup_deposit_success);
        activity=this;

        /*PublicFunction2.setStatusBarColor(this, R.color.colorBased);
        TextView title=(TextView)findViewById(R.id.txtHeader);
        title.setText("Topup Wallet");
        title.setVisibility(View.VISIBLE);*/
        PublicFunction.setHeaderStatus(activity,"DRD Point");

        dataTrxId=getIntent().getLongExtra("TrxId",0);
        dataTrxNo=getIntent().getStringExtra("TrxNo");
        dataTrxDate=getIntent().getStringExtra("TrxDate");
        dataTrxAmount=getIntent().getDoubleExtra("TrxAmount",0);
        dataTrxDisccount=getIntent().getDoubleExtra("TrxDisccount",0);
        dataTrxPaid=getIntent().getDoubleExtra("TrxPaid",0);
        dataTrxType=getIntent().getStringExtra("TrxType");
        dataTrxDescr=getIntent().getStringExtra("TrxDescr");

        dataTrxOutstanding=dataTrxAmount-dataTrxDisccount-dataTrxPaid;


        TextView detailInfo=(TextView)findViewById(R.id.detailInfo);
        TextView topupNomor=(TextView)findViewById(R.id.topupNomor);
        TextView topupDate=(TextView)findViewById(R.id.topupDate);
        TextView topupAmount=(TextView)findViewById(R.id.topupAmount);

        Date odate=PublicFunction.dateAdd(PublicFunction.getToday(), Calendar.HOUR,2);
        String info=getResources().getText(R.string.topup_success_text).toString();
        info=info.replace("[_EXPIRED_TIME_]", PublicFunction.dateToString("dd/MM/yyyy HH:mm",odate));
        detailInfo.setText(Html.fromHtml(info));

        topupNomor.setText(dataTrxNo);
        topupDate.setText(dataTrxDate);
        topupAmount.setText("DRD Point "+NumberFormat.getInstance().format(dataTrxAmount));

        Button btnPayment=(Button)findViewById(R.id.btnPayment);
        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
                Intent intent = new Intent(activity, PaymentActivity.class);
                intent.putExtra("TrxId",(long)dataTrxId);
                intent.putExtra("TrxNo",dataTrxNo);
                intent.putExtra("TrxDate",dataTrxDate);
                intent.putExtra("TrxAmount",dataTrxAmount);
                intent.putExtra("TrxType","MTU");
                intent.putExtra("TrxDescr","Member Topup Deposit");
                intent.putExtra("TrxPayMethod",getString(R.string.api_topup_paymethodtype));
                activity.startActivity (intent);
                finish();
            }
        });

        Button btnClose=(Button)findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}

