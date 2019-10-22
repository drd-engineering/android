package com.makaya.drd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.makaya.drd.domain.CompanyBank;
import com.makaya.drd.domain.PaymentMethod;
import com.makaya.drd.domain.PaymentMethodRoot;
import com.makaya.drd.library.PublicFunction;
import com.makaya.drd.service.PostDataModelUrl;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Created by xbudi on 15/11/2016.
 */

public class PaymentActivity extends AppCompatActivity {

    ArrayList<PaymentMethod> paymethods=new ArrayList<>();

    Activity activity;
    Button btnContinue;
    TextView trx;
    TextView trxType;
    TextView amount;
    TextView discount;
    TextView paid;
    TextView outstanding;

    LinearLayout layoutDiscount;
    LinearLayout layoutPaid;

    //LinearLayout layoutPayAmount;
    //EditText payAmount;

    String dataTrxKeyId;
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

    String dataTrxPayMehotds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment);
        activity=this;
        /*PublicFunction2.setStatusBarColor(this, R.color.colorBased);
        TextView title=(TextView)findViewById(R.id.appBarTitle);
        title.setText("PAYMENT");
        title.setVisibility(View.VISIBLE);*/
        PublicFunction.setHeaderStatus(activity,"Payment");
        dataTrxKeyId=getIntent().getStringExtra("TrxKeyId");
        dataTrxId=getIntent().getLongExtra("TrxId",0);
        dataTrxNo=getIntent().getStringExtra("TrxNo");
        dataTrxDate=getIntent().getStringExtra("TrxDate");
        dataTrxAmount=getIntent().getDoubleExtra("TrxAmount",0);
        dataTrxDisccount=getIntent().getDoubleExtra("TrxDisccount",0);
        dataTrxPaid=getIntent().getDoubleExtra("TrxPaid",0);
        dataTrxType=getIntent().getStringExtra("TrxType");
        dataTrxDescr=getIntent().getStringExtra("TrxDescr");

        dataTrxDescr=getIntent().getStringExtra("TrxDescr");
        dataTrxPayMehotds=getIntent().getStringExtra("TrxPayMethod");

        dataTrxOutstanding=dataTrxAmount-dataTrxDisccount-dataTrxPaid;

        /*layoutPayAmount=(LinearLayout)findViewById(R.id.layoutPayAmount);
        payAmount = (EditText)findViewById(R.id.payAmount);
        payAmount.setText(Long.toString(dataTrxOutstanding.longValue()));

        if (dataTrxType.contains("TU")){
            layoutPayAmount.setVisibility(View.GONE);
        }*/

        trx = (TextView)findViewById(R.id.trx);
        trxType = (TextView)findViewById(R.id.trxType);
        amount = (TextView)findViewById(R.id.amount);
        discount = (TextView)findViewById(R.id.discount);
        paid = (TextView)findViewById(R.id.paid);
        outstanding = (TextView)findViewById(R.id.outstanding);

        trx.setText(dataTrxNo+" | "+dataTrxDate);
        trxType.setText(dataTrxDescr+" | "+dataTrxType);
        amount.setText("DRD Point "+NumberFormat.getInstance().format(dataTrxAmount));
        discount.setText("DRD Point "+NumberFormat.getInstance().format(dataTrxDisccount));
        paid.setText("DRD Point "+NumberFormat.getInstance().format(dataTrxPaid));
        outstanding.setText("DRD Point "+NumberFormat.getInstance().format(dataTrxOutstanding));

        layoutDiscount=(LinearLayout)findViewById(R.id.layoutDiscount);
        layoutPaid=(LinearLayout)findViewById(R.id.layoutPaid);
        if (dataTrxDisccount==0)
            layoutDiscount.setVisibility(View.GONE);
        if (dataTrxPaid==0)
            layoutPaid.setVisibility(View.GONE);

        /*layoutChannel=(LinearLayout)findViewById(R.id.layoutChannel);
        if (!dataTrxType.equals("PUR")) {
            // untuk pembayaran order hanya menggunakan deposit, click disabled
            layoutChannel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupPaymentMethod();

                }
            });
        }*/

        initButton();
        fetchPaymentMethod();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        }
    }

    /*void popupPaymentMethod()
    {
        PopupPayMethodDialog ppm=new PopupPayMethodDialog(activity, paymethods);
        ppm.show();
        ppm.setOnSelectedListener(new PopupPayMethodDialog.OnSelectedListener() {
            @Override
            public void onSelected(PaymentMethod method) {
                bindPaymentMethod(method);
            }
        });
    }*/

    void initButton()
    {
        btnContinue=(Button)findViewById(R.id.btnContinue);
        /*btnTransfer.setEnabled(false);
        btnTransfer.setTextColor(Color.DKGRAY);*/
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //submitPayment();
                //doContinue();

            }
        });
    }

    public void doContinue(CompanyBank pmc)
    {
        dataTrxPayAmount=dataTrxAmount;
        if (pmc.PaymentMethod.ConfirmType == 0){
            /*Intent intent = new Intent(activity, PaymentViaAggregatorActivity.class);
            intent.putExtra("Method",(Serializable)pmc);
            intent.putExtra("TrxId",(long)dataTrxId);
            intent.putExtra("TrxNo",dataTrxNo);
            intent.putExtra("TrxDate",dataTrxDate);
            intent.putExtra("TrxAmount",dataTrxAmount);
            intent.putExtra("TrxDisccount",dataTrxDisccount);
            intent.putExtra("TrxPaid",dataTrxPaid);
            intent.putExtra("TrxType",dataTrxType);
            intent.putExtra("TrxDescr",dataTrxDescr);
            intent.putExtra("TrxPayAmount",dataTrxPayAmount);
            activity.startActivityForResult (intent, 1);*/
            String url="";
            /*if (pmc.PaymentMethod.Code.equals("VA"))
                url=MainApplication.getUrlApplWeb()+ "/payment/xAggregator?id=" + dataTrxKeyId+","+pmc.KeyId;
            else*/
                url=MainApplication.getUrlApplWeb()+"/payment/xAggregator?id=" + dataTrxKeyId+","+pmc.KeyId;

            PublicFunction.showMiniBrowser(activity,url,true,"Payment");

            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_OK, returnIntent);
            finish();

            return;
        }else if ((pmc.PaymentMethod.ConfirmType & 1)==1){
            Intent intent = new Intent(activity, PaymentConfirmationActivity.class);
            intent.putExtra("Method",(Serializable)pmc);
            intent.putExtra("TrxId",(long)dataTrxId);
            intent.putExtra("TrxNo",dataTrxNo);
            intent.putExtra("TrxDate",dataTrxDate);
            intent.putExtra("TrxAmount",dataTrxAmount);
            intent.putExtra("TrxDisccount",dataTrxDisccount);
            intent.putExtra("TrxPaid",dataTrxPaid);
            intent.putExtra("TrxType",dataTrxType);
            intent.putExtra("TrxDescr",dataTrxDescr);
            intent.putExtra("TrxPayAmount",dataTrxPayAmount);
            activity.startActivityForResult (intent, 1);
            return;
        }else if ((pmc.PaymentMethod.ConfirmType & 2)==2){
            Intent intent = new Intent(activity, PaymentViaGeraiActivity.class);
            intent.putExtra("Method",(Serializable)pmc);
            intent.putExtra("TrxId",(long)dataTrxId);
            intent.putExtra("TrxNo",dataTrxNo);
            intent.putExtra("TrxDate",dataTrxDate);
            intent.putExtra("TrxAmount",dataTrxAmount);
            intent.putExtra("TrxDisccount",dataTrxDisccount);
            intent.putExtra("TrxPaid",dataTrxPaid);
            intent.putExtra("TrxType",dataTrxType);
            intent.putExtra("TrxDescr",dataTrxDescr);
            intent.putExtra("TrxPayAmount",dataTrxPayAmount);
            activity.startActivityForResult (intent, 1);
            return;
        }else if ((pmc.PaymentMethod.ConfirmType & 4)==4){
            Intent intent = new Intent(activity, PaymentWithDepositActivity.class);
            intent.putExtra("Method",(Serializable)pmc);
            intent.putExtra("TrxId",(long)dataTrxId);
            intent.putExtra("TrxNo",dataTrxNo);
            intent.putExtra("TrxDate",dataTrxDate);
            intent.putExtra("TrxAmount",dataTrxAmount);
            intent.putExtra("TrxDisccount",dataTrxDisccount);
            intent.putExtra("TrxPaid",dataTrxPaid);
            intent.putExtra("TrxType",dataTrxType);
            intent.putExtra("TrxDescr",dataTrxDescr);
            intent.putExtra("TrxPayAmount",dataTrxPayAmount);
            activity.startActivityForResult (intent, 1);
            return;
        }else if ((pmc.PaymentMethod.ConfirmType & 8)==8){
            Intent intent = new Intent(activity, PaymentWithVoucherActivity.class);
            intent.putExtra("Method",(Serializable)pmc);
            intent.putExtra("TrxId",(long)dataTrxId);
            intent.putExtra("TrxNo",dataTrxNo);
            intent.putExtra("TrxDate",dataTrxDate);
            intent.putExtra("TrxAmount",dataTrxAmount);
            intent.putExtra("TrxDisccount",dataTrxDisccount);
            intent.putExtra("TrxPaid",dataTrxPaid);
            intent.putExtra("TrxType",dataTrxType);
            intent.putExtra("TrxDescr",dataTrxDescr);
            intent.putExtra("TrxPayAmount",dataTrxPayAmount);
            activity.startActivityForResult (intent, 1);
            return;
        }

    }

    private void bindRecyclerView()
    {
        RecyclerView rv = (RecyclerView)findViewById(R.id.rv);
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(activity);
        rv.setLayoutManager(llm);

        PaymentAdapter adapter = new PaymentAdapter(activity, paymethods);
        rv.setAdapter(adapter);
        /*adapter.setOnSelectedListener(new PaymentAdapter.OnSelectedListener() {
            @Override
            public void onSelected(PaymentMethod method) {
                doContinue(method);
            }
        });*/
    }

    void fetchPaymentMethod()
    {

        String url=String.format(activity.getResources().getString(R.string.api_get_paymethod),
                MainApplication.getUrlApplApi());
        PostDataModelUrl posData = new PostDataModelUrl();
        posData.execute(url, PaymentMethodRoot.class);
        posData.setOnDataPostedListener(new PostDataModelUrl.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data) {
                if (data==null)
                    return;

                /*for(PaymentMethod method:((PaymentMethodRoot)data).Root) {
                    if ((method.UsingType & 1)==1 || ("PUR").contains(dataTrxType))
                        paymethods.add(method);
                }*/
                /*if (dataTrxType.equals("PUR")){
                    // hanya payment dari deposit saja
                    for (PaymentMethod method : ((PaymentMethodRoot) data).Root) {
                        if (method.UsingType == 2)
                            paymethods.add(method);
                    }
                }else {
                    for (PaymentMethod method : ((PaymentMethodRoot) data).Root) {
                        if ((method.UsingType & 1) == 1)
                            paymethods.add(method);
                    }
                }*/
                for (PaymentMethod method : ((PaymentMethodRoot) data).Root) {
                    if (dataTrxPayMehotds.contains(method.Code))
                        paymethods.add(method);
                }
                //bindPaymentMethod(paymethods.get(0));
                bindRecyclerView();
            }

            @Override
            public <T> void onPostedError(Exception data) {
                Toast.makeText(activity,activity.getResources().getString(R.string.fetch_data_problem),Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*void bindPaymentMethod(PaymentMethod method)
    {
        layoutChannel.setTag(method);
        payName.setText(method.Name);
        payDescr.setText(method.Descr);

        String path=MainApplication.getUrlApplBaseApi()+"/Images/bank/"+method.Logo;
        Picasso.with(activity)
                .load(path)
                .placeholder(R.drawable.city_loading)
                .error(R.drawable.city_loading)
                .into(payLogo);
    }*/
}
