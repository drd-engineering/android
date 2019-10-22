package com.makaya.drd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.makaya.drd.domain.CompanyBank;
import com.makaya.drd.domain.MemberLogin;
import com.makaya.drd.library.PublicFunction;

import java.text.NumberFormat;

/**
 * Created by xbudi on 15/11/2016.
 */

public class PaymentViaAggregatorActivity extends AppCompatActivity {

    Activity activity;
    Button btnConfirm;
    TextView textDescr;
    TextView trx;
    TextView trxType;
    TextView amount;
    TextView discount;
    TextView paid;
    TextView outstanding;
    TextView payAmount;

    TextView payName;
    TextView payDescr;
    ImageView payLogo;
    LinearLayout layoutDiscount;
    LinearLayout layoutPaid;

    CompanyBank dataMethod;
    long dataTrxId;
    String dataTrxNo;
    String dataTrxDate;
    Double dataTrxAmount;
    Double dataTrxDisccount;
    Double dataTrxPaid;
    Double dataTrxOutstanding;
    Double dataTrxPayAmount;
    String dataTrxType;
    String dataTrxDescr;

    MainApplication global;
    SessionManager session;
    MemberLogin member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_via_aggregator);
        activity=this;
        global=(MainApplication)getApplication();
        session=new SessionManager(getApplication());
        member=session.getUserLogin();

        /*TextView title=(TextView)findViewById(R.id.appBarTitle);
        title.setText("PAYMENT VIA AGGREGATOR");
        title.setVisibility(View.VISIBLE);
        PublicFunction2.setStatusBarColor(this, R.color.colorBased);*/
        PublicFunction.setHeaderStatus(activity,"Payment Via Aggregator");

        dataMethod=(CompanyBank)getIntent().getSerializableExtra("Method");
        dataTrxId=getIntent().getLongExtra("TrxId",0);
        dataTrxNo=getIntent().getStringExtra("TrxNo");
        dataTrxDate=getIntent().getStringExtra("TrxDate");
        dataTrxAmount=getIntent().getDoubleExtra("TrxAmount",0);
        dataTrxDisccount=getIntent().getDoubleExtra("TrxDisccount",0);
        dataTrxPaid=getIntent().getDoubleExtra("TrxPaid",0);
        dataTrxPayAmount=getIntent().getDoubleExtra("TrxPayAmount",0);
        dataTrxType=getIntent().getStringExtra("TrxType");
        dataTrxDescr=getIntent().getStringExtra("TrxDescr");

        dataTrxOutstanding=dataTrxAmount-dataTrxDisccount-dataTrxPaid;

        textDescr = (TextView)findViewById(R.id.textDescr);

        payAmount = (TextView)findViewById(R.id.payAmount);
        trx = (TextView)findViewById(R.id.trx);
        trxType = (TextView)findViewById(R.id.trxType);
        amount = (TextView)findViewById(R.id.amount);
        discount = (TextView)findViewById(R.id.discount);
        paid = (TextView)findViewById(R.id.paid);
        outstanding = (TextView)findViewById(R.id.outstanding);

        payName = (TextView)findViewById(R.id.methodName);
        payDescr = (TextView)findViewById(R.id.payDescr);
        payLogo = (ImageView)findViewById(R.id.payLogo);

        trx.setText(dataTrxNo+" | "+dataTrxDate);
        trxType.setText(dataTrxDescr+" | "+dataTrxType);
        amount.setText("DRD Point "+NumberFormat.getInstance().format(dataTrxAmount));
        discount.setText("DRD Point "+NumberFormat.getInstance().format(dataTrxDisccount));
        paid.setText("DRD Point "+NumberFormat.getInstance().format(dataTrxPaid));
        outstanding.setText("DRD Point "+NumberFormat.getInstance().format(dataTrxOutstanding));
        payAmount.setText("DRD Point "+NumberFormat.getInstance().format(dataTrxPayAmount));

        bindPaymentMethod(dataMethod);

        layoutDiscount=(LinearLayout)findViewById(R.id.layoutDiscount);
        layoutPaid=(LinearLayout)findViewById(R.id.layoutPaid);
        if (dataTrxDisccount==0)
            layoutDiscount.setVisibility(View.GONE);
        if (dataTrxPaid==0)
            layoutPaid.setVisibility(View.GONE);

        /*String text=textDescr.getText().toString();
        text=text.replace("[_NOMOR_]","<b>"+dataTrxNo+"</b>");
        text=text.replace("[_AMOUNT_]","<b>IDR. "+NumberFormat.getInstance().format(dataTrxOutstanding)+"</b>");
        textDescr.setText(Html.fromHtml(text));*/

        initButton();
    }

    void initButton()
    {
        btnConfirm=(Button)findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPayment();

            }
        });
    }

    void submitPayment()
    {
        /*btnConfirm.setEnabled(false);
        btnConfirm.setTextColor(Color.DKGRAY);
        btnConfirm.setText("Process...");*/

        if (dataTrxType.contains("TU")){

            /*MemberTopupPayment pay = new MemberTopupPayment();
            pay.Amount = dataTrxPayAmount;
            pay.TopupDepositId = dataTrxId;
            pay.CompanyBankId = -dataMethod.Id;
            pay.MemberAccountId = 0;
            MemberTopupPaymentService srv = new MemberTopupPaymentService(activity);
            srv.setOnDataPostedListener(new MemberTopupPaymentService.OnSetDataPostedListener() {
                @Override
                public <T> void onDataPosted(T data, boolean isLoadedAllItems) {
                    btnConfirm.setEnabled(true);
                    btnConfirm.setTextColor(Color.WHITE);
                    btnConfirm.setText("CONFIRM");*/

                    String name = member.Name;
                    String email = member.Email;

                    String url = getString(R.string.urlApplBaseApi)+"/Faspay/Payment?invoiceId=" + dataTrxId + "&invoiceNo=" + dataTrxNo + "&trxtype=" + dataTrxType + "&type=";// +
                            //dataMethod.Code + "&amount=" + Math.round(dataTrxPayAmount) + ".00&descr=" + dataTrxDescr + "&bank=&name=" + name + "&email=" + email;
                    // lihat di project lain file ini

                    /*Intent intent = new Intent(activity, MiniBrowserActivity.class);
                    intent.putExtra("ShowWarning", true);
                    intent.putExtra("URL", url);
                    activity.startActivity(intent);*/
                    PublicFunction.showMiniBrowser(activity,url,true,"Payment");

                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                /*}

                @Override
                public <T> void onDataError() {
                    btnConfirm.setEnabled(true);
                    btnConfirm.setTextColor(Color.WHITE);
                    btnConfirm.setText("CONFIRM");
                }
            });
            srv.saveData(pay);*/
        }/*else
        {
        btnConfirm.setEnabled(false);
        btnConfirm.setTextColor(Color.DKGRAY);
        btnConfirm.setText("Process...");

            String url="";
            PaymentOrder po=new PaymentOrder();
            PostDataModelUrl posData = new PostDataModelUrl();

            url=String.format(getResources().getString(R.string.api_payment_save), MainApplication.getUrlLinkApi());

            po.PaymentStatus=(dataTrxType.equals("RO")?"R":"C");
            po.Id=dataTrxId;
            po.Amount= dataTrxPayAmount;
            po.MemberAccountId=0;
            po.CompanyBankId=-dataMethod.Id;
            String sss= PublicFunction2.classToJson(po);
            posData.execute(url, po, PaymentOrder.class);

            posData.setOnDataPostedListener(new PostDataModelUrl.OnSetDataPostedListener() {
                @Override
                public <T> void onDataPosted(T data) {
                    btnConfirm.setEnabled(true);
                    btnConfirm.setTextColor(Color.WHITE);
                    btnConfirm.setText("CONFIRM");
                    if (data==null)
                    {
                        Toast.makeText(activity,activity.getResources().getString(R.string.save_data_problem),Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!dataTrxType.contains("TU")) {
                        PaymentOrder po = (PaymentOrder) data;
                        dataTrxId=po.Id;
                        dataTrxNo=po.PaymentNo;
                    }
                    String name=member.Name;
                    String email=member.EMail;

                    String url="http://vti.api.klaxon.biz/Faspay/Payment?invoiceId="+dataTrxId+"&invoiceNo="+dataTrxNo+"&trxtype="+dataTrxType+"&type="+
                            dataMethod.Code+"&amount="+Math.round(dataTrxPayAmount)+".00&descr="+dataTrxDescr+"&bank=&name="+name+"&email="+email;

                    Intent intent = new Intent(activity, MiniBrowserActivity.class);
                    intent.putExtra("ShowWarning", true);
                    intent.putExtra("URL",url);
                    activity.startActivity (intent);


                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();
                }

                @Override
                public <T> void onPostedError(Exception data) {
                    btnConfirm.setEnabled(true);
                    btnConfirm.setTextColor(Color.WHITE);
                    btnConfirm.setText("CONFIRM");
                    Toast.makeText(activity,activity.getResources().getString(R.string.save_data_problem),Toast.LENGTH_SHORT).show();
                }
            });
        }*/
    }

    void bindPaymentMethod(CompanyBank method)
    {
        /*payName.setText(method.Name);
        payDescr.setText(method.Descr);

        String path=MainApplication.getUrlApplWeb()+"/Images/bank/"+method.Logo;
        Picasso.with(activity)
                .load(path)
                .placeholder(R.drawable.no_picture)
                .error(R.drawable.no_picture)
                .into(payLogo);*/
    }
}
