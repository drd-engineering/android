package com.makaya.drd;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.makaya.drd.domain.MemberTopupPayment;
import com.makaya.drd.domain.PaymentMethod;
import com.makaya.drd.library.PublicFunction;
import com.makaya.drd.service.MemberTopupPaymentService;

import java.text.NumberFormat;

/**
 * Created by xbudi on 15/11/2016.
 */

public class PaymentWithDepositActivity extends AppCompatActivity {

    Activity activity;
    Button btnConfirm;
    TextView textDescr;
    TextView textBalance;
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

    PaymentMethod dataMethod;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_with_deposit);
        activity=this;

        global=(MainApplication)getApplication();
        session=new SessionManager(getApplication());

        /*PublicFunction2.setStatusBarColor(this, R.color.colorBased);
        TextView title=(TextView)findViewById(R.id.appBarTitle);
        title.setText("PAYMENT USING DEPOSIT");
        title.setVisibility(View.VISIBLE);*/
        PublicFunction.setHeaderStatus(activity,"Payment Using Deposit");

        dataMethod=(PaymentMethod)getIntent().getSerializableExtra("Method");
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
        textBalance = (TextView)findViewById(R.id.textBalance);

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

        btnConfirm=(Button)findViewById(R.id.btnConfirm);

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

        double balance=session.getDashboard().DepositBalance;

        String text=textDescr.getText().toString();
        text=text.replace("[_BALANCE_]","<b>"+NumberFormat.getInstance().format(balance)+"</b>");
        textDescr.setText(Html.fromHtml(text));

        if (balance<dataTrxPayAmount /*dataTrxOutstanding*/) {
            textBalance.setTextColor(Color.RED);
            btnConfirm.setBackgroundColor(Color.RED);
            btnConfirm.setText("BACK");
        }else
        {
            textBalance.setText("Deposit balances sufficient");
        }

        initButton();
    }

    void initButton()
    {
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (session.getDashboard().DepositBalance<dataTrxPayAmount /*dataTrxOutstanding*/)
                    finish();
                else
                    submitPayment();
            }
        });
    }

    void submitPayment()
    {
        btnConfirm.setEnabled(false);
        btnConfirm.setTextColor(Color.DKGRAY);
        btnConfirm.setText("Process...");

        if (dataTrxType.contains("TU")){

            MemberTopupPayment pay = new MemberTopupPayment();
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
                    btnConfirm.setText("CONFIRM");

                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }

                @Override
                public <T> void onDataError() {
                    btnConfirm.setEnabled(true);
                    btnConfirm.setTextColor(Color.WHITE);
                    btnConfirm.setText("CONFIRM");
                }
            });
            srv.saveData(pay);
        }/*else {

            String url="";
            PaymentOrder po=new PaymentOrder();
            PostDataModelUrl posData = new PostDataModelUrl();
            
            url = String.format(getResources().getString(R.string.api_payment_save), MainApplication.getUrlLinkApi());

            //po.PaymentStatus = (dataTrxType.equals("RO") ? "R" : "C");
            //po.Id = dataTrxId;
            po.Amount = dataTrxPayAmount;
            po.MemberAccountId = 0;
            po.CompanyBankId = -dataMethod.Id;
            po.BillingOrderId=dataTrxId;
            String sss = PublicFunction2.classToJson(po);
            posData.execute(url, po, PaymentOrder.class);

            posData.setOnDataPostedListener(new PostDataModelUrl.OnSetDataPostedListener() {
                @Override
                public <T> void onDataPosted(T data) {
                    btnConfirm.setEnabled(true);
                    btnConfirm.setTextColor(Color.WHITE);
                    btnConfirm.setText("CONFIRM");
                    if (data == null) {
                        Toast.makeText(activity, activity.getResources().getString(R.string.save_data_problem), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Toast.makeText(activity, "Save payment success!", Toast.LENGTH_SHORT).show();
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }

                @Override
                public <T> void onPostedError(Exception data) {
                    btnConfirm.setEnabled(true);
                    btnConfirm.setTextColor(Color.WHITE);
                    btnConfirm.setText("CONFIRM");
                    Toast.makeText(activity, activity.getResources().getString(R.string.save_data_problem), Toast.LENGTH_SHORT).show();
                }
            });
        }*/
    }

    void bindPaymentMethod(PaymentMethod method)
    {
        payName.setText(method.Name);
        payDescr.setText(method.Descr);

        String path=MainApplication.getUrlApplWeb()+"/Images/bank/"+method.Logo;
        Picasso.with(activity)
                .load(path)
                .placeholder(R.drawable.no_picture)
                .error(R.drawable.no_picture)
                .into(payLogo);
    }
}
