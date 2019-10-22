package com.makaya.drd;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.makaya.drd.domain.CompanyBank;
import com.makaya.drd.domain.CompanyBankRoot;
import com.makaya.drd.domain.GenericData;
import com.makaya.drd.domain.MemberAccount;
import com.makaya.drd.domain.MemberAccountRoot;
import com.makaya.drd.domain.MemberLogin;
import com.makaya.drd.domain.MemberTopupPayment;
import com.makaya.drd.library.PublicFunction;
import com.makaya.drd.service.MemberTopupPaymentService;
import com.makaya.drd.service.PostDataModelUrl;

import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Created by xbudi on 15/11/2016.
 */

public class PaymentConfirmationActivity extends AppCompatActivity {

    ArrayList<GenericData> accounts=new ArrayList<>();
    ArrayList<CompanyBank> banks=new ArrayList<>();

    Activity activity;
    Button btnConfirm;
    ImageView btnAddBank;
    CheckBox checkBox;

    TextView trx;
    TextView trxType;
    TextView amount;
    TextView discount;
    TextView paid;
    TextView outstanding;

    TextView bankAccount;
    EditText paidAmount;
    TextView payAmount;

    LinearLayout layoutDiscount;
    LinearLayout layoutPaid;
    TextInputLayout layoutBank;

    CardView cvBank;
    TextView bankName;
    TextView branchOffice;
    TextView accountNo;
    TextView accountName;
    ImageView logobank;

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
        setContentView(R.layout.payment_confirmation);
        activity=this;

        global=(MainApplication)getApplication();
        session=new SessionManager(getApplication());
        member=session.getUserLogin();

        PublicFunction.setHeaderStatus(activity,"Payment Confirmation");

        dataMethod=(CompanyBank) getIntent().getSerializableExtra("Method");
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

        payAmount = (TextView)findViewById(R.id.payAmount);
        trx = (TextView)findViewById(R.id.trx);
        trxType = (TextView)findViewById(R.id.trxType);
        amount = (TextView)findViewById(R.id.amount);
        discount=(TextView)findViewById(R.id.discount);
        paid=(TextView)findViewById(R.id.paid);
        outstanding=(TextView)findViewById(R.id.outstanding);

        layoutBank=(TextInputLayout)findViewById(R.id.layoutBank);

        bankAccount=(TextView)findViewById(R.id.bankAccount);
        paidAmount=(EditText)findViewById(R.id.paidAmount);
        //paidAmount.addTextChangedListener(new NumberTextWatcher(paidAmount));

        layoutDiscount=(LinearLayout)findViewById(R.id.layoutDiscount);
        layoutPaid=(LinearLayout)findViewById(R.id.layoutPaid);

        trx.setText(dataTrxNo+" | "+dataTrxDate);
        trxType.setText(dataTrxDescr+" | "+dataTrxType);
        amount.setText("DRD Point "+NumberFormat.getInstance().format(dataTrxAmount));
        discount.setText("DRD Point "+NumberFormat.getInstance().format(dataTrxDisccount));
        paid.setText("DRD Point "+NumberFormat.getInstance().format(dataTrxPaid));
        outstanding.setText("DRD Point "+NumberFormat.getInstance().format(dataTrxOutstanding));
        payAmount.setText("DRD Point "+NumberFormat.getInstance().format(dataTrxPayAmount));

        paidAmount.setText(dataTrxPayAmount.intValue()+"");

        layoutDiscount=(LinearLayout)findViewById(R.id.layoutDiscount);
        layoutPaid=(LinearLayout)findViewById(R.id.layoutPaid);
        if (dataTrxDisccount==0)
            layoutDiscount.setVisibility(View.GONE);
        if (dataTrxPaid==0)
            layoutPaid.setVisibility(View.GONE);

        cvBank = (CardView)findViewById(R.id.cvBank);
        bankName = (TextView)findViewById(R.id.bankName);
        branchOffice = (TextView)findViewById(R.id.branchOffice);
        accountNo = (TextView)findViewById(R.id.accountNo);
        accountName = (TextView)findViewById(R.id.accountName);
        logobank = (ImageView)findViewById(R.id.logobank);

        initButton();
        fetchData();
        //fetchCompanyBank();
        bindCompanyBank(dataMethod);
        fetchDataBank();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                fetchDataBank();
            }

        }
    }

    void initButton()
    {
        btnAddBank=(ImageView)findViewById(R.id.btnAddBank);
        btnAddBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activity,MemberAccountActivity.class);
                activity.startActivityForResult(intent,1);

            }
        });

        btnConfirm=(Button)findViewById(R.id.btnConfirm);
        btnConfirm.setEnabled(false);
        btnConfirm.setTextColor(Color.DKGRAY);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPayment();
            }
        });

        checkBox=(CheckBox) findViewById(R.id.checkBox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                btnConfirm.setEnabled(isChecked);
                btnConfirm.setTextColor((isChecked?Color.WHITE:Color.DKGRAY));
            }
        });

        bankAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupDialogList pd=new PopupDialogList(v, R.id.bankAccount);
                pd.Show(R.layout.popup_list);
                pd.BindData(accounts);
                pd.setOnSelectedListener(new PopupDialogList.OnSelectedListener() {
                    @Override
                    public void onSelected(Object data) {

                    }
                });
            }
        });

        /*cvBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupCompanyBankDialog popup=new PopupCompanyBankDialog(activity, banks);
                popup.show();
                popup.setOnSelectedListener(new PopupCompanyBankDialog.OnSelectedListener() {
                    @Override
                    public void onSelected(CompanyBank cbc) {
                        bindCompanyBank(cbc);
                        if ((cbc.Bank.BankType & 4)==4)
                            layoutBank.setVisibility(View.GONE);
                        else
                            layoutBank.setVisibility(View.VISIBLE);

                    }
                });
            }
        });*/
    }

    void submitPayment() {
        if (layoutBank.getVisibility() == View.VISIBLE && bankAccount.getTag() == null) {
            Toast.makeText(activity, "Select your bank account!", Toast.LENGTH_SHORT).show();
            bankAccount.requestFocus();
            return;
        }

        if (paidAmount.getText().toString().isEmpty() || paidAmount.getText().toString().equals("0")) {
            Toast.makeText(activity, "Amount to be paid must be filled!", Toast.LENGTH_SHORT).show();
            paidAmount.requestFocus();
            return;
        }

        if (PublicFunction.toDouble(paidAmount) != dataTrxPayAmount) {
            Toast.makeText(activity, "Amount is not equal to the payment amount.", Toast.LENGTH_SHORT).show();
            paidAmount.requestFocus();
            return;
        }

        PublicFunction.hideKeyboard(activity);

        btnConfirm.setEnabled(false);
        btnConfirm.setTextColor(Color.DKGRAY);
        btnConfirm.setText("Process...");

        if (dataTrxType.contains("TU")) {

            MemberTopupPayment pay = new MemberTopupPayment();
            pay.Amount = PublicFunction.toDouble(paidAmount);

            pay.TopupDepositId = dataTrxId;
            pay.CompanyBankId = (int) ((CompanyBank) cvBank.getTag()).Id;
            pay.MemberAccountId = ((GenericData) bankAccount.getTag()).Id;
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
        } /*else {
            String url = "";
            PaymentOrder po = new PaymentOrder();
            PostDataModelUrl posData = new PostDataModelUrl();
            url = String.format(getResources().getString(R.string.api_payment_save), MainApplication.getUrlLinkApi());

            //po.PaymentStatus=dataTrxType;
            po.BillingOrderId = dataTrxId;
            po.BillingOrder.Amount = dataTrxAmount;
            po.BillingOrder.Discount = dataTrxDisccount;
            po.BillingOrder.Paid = dataTrxPaid;
            po.Amount = PublicFunction2.toDouble(paidAmount);
            po.MemberAccountId = ((GenericData) bankAccount.getTag()).Id;
            po.CompanyBankId = (int) ((CompanyBank) cvBank.getTag()).Id;
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

    void fetchDataBank()
    {
        String url=String.format(getResources().getString(R.string.api_member_account_getmember),
                global.getUrlApplApi(), member.Id);

        accounts.clear();
        final PostDataModelUrl posData = new PostDataModelUrl();
        posData.execute(url, MemberAccountRoot.class);
        posData.setOnDataPostedListener(new PostDataModelUrl.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data) {
                if (data!=null) {
                    for(MemberAccount bc: ((MemberAccountRoot)data).Root){
                        GenericData gc=new GenericData();
                        gc.Id=bc.Id;
                        gc.Descr=bc.Title+": "+bc.AccountNo+" | "+bc.Bank.Name;
                        accounts.add(gc);
                    }
                }
            }

            @Override
            public <T> void onPostedError(Exception data) {

            }
        });
    }

    void fetchCompanyBank()
    {

        String url=String.format(activity.getResources().getString(R.string.api_get_companybank), MainApplication.getUrlApplApi());
        PostDataModelUrl posData = new PostDataModelUrl();
        posData.execute(url, CompanyBankRoot.class);
        posData.setOnDataPostedListener(new PostDataModelUrl.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data) {
                if (data==null)
                    return;

                //banks=((CompanyBankRoot)data).Root;
                banks.clear();
                for(CompanyBank bank:((CompanyBankRoot)data).Root){
                    if (bank.PaymentMethod.Code.equals("BT"))
                        banks.add(bank);
                }
                bindCompanyBank(banks.get(0));
            }

            @Override
            public <T> void onPostedError(Exception data) {
                Toast.makeText(activity,activity.getResources().getString(R.string.fetch_data_problem),Toast.LENGTH_SHORT).show();
            }
        });
    }


    void fetchData()
    {
        /*int resId=R.string.api_rohistory;
        if (isCustom)
            resId=R.string.api_rocustom;

        String url=String.format(getResources().getString(resId), MainApplication.urlKlaxonapi, orderId);

        final PostDataModelUrl posData = new PostDataModelUrl();
        if (!isCustom) {
            posData.execute(url, RentOrderHistoryClass.class);
            posData.setOnDataPostedListener(new PostDataModelUrl.OnSetDataPostedListener() {
                @Override
                public <T> void onDataPosted(T data) {
                    //progressBar.setVisibility(View.GONE);
                    if (data != null) {
                        rohistory=(RentOrderHistoryClass)data;

                        billing.setText(rohistory.BillingNo+" | "+MainApplication.dateToString("dd/MM/yyyy",rohistory.BillingDate));
                        order.setText(rohistory.Nomor+" | "+MainApplication.dateToString("dd/MM/yyyy",rohistory.OrderDate));
                        orderType.setText(rohistory.OrderTypeDescr);
                        vehicle.setText(rohistory.VehicleDescr);
                        totalCharge.setText("IDR "+ NumberFormat.getInstance().format(rohistory.BillingAmount));
                        paid.setText("IDR "+ NumberFormat.getInstance().format(rohistory.BillingPaid));
                        outstanding.setText("IDR "+ NumberFormat.getInstance().format(rohistory.BillingAmount-rohistory.Discount-rohistory.BillingPaid));

                        *//*if ((MainApplication.memberContent.DataMember.MemberType & 2) != 2)
                            discountLayout.setVisibility(View.GONE);
                        else
                        {
                            discount.setText("IDR "+ NumberFormat.getInstance().format(rohistory.Discount));
                        }*//*
                        if (rohistory.Discount!=0)
                            discount.setText("IDR "+ NumberFormat.getInstance().format(rohistory.Discount));
                        else
                            discountLayout.setVisibility(View.GONE);

                        layout.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public <T> void onPostedError(Exception data) {
                    //progressBar.setVisibility(View.GONE);
                    Toast.makeText(activity,activity.getResources().getString(R.string.fetch_data_problem),Toast.LENGTH_SHORT).show();
                }
            });
        }else
        {
            posData.execute(url, RentOrderCustomClass.class);
            posData.setOnDataPostedListener(new PostDataModelUrl.OnSetDataPostedListener() {
                @Override
                public <T> void onDataPosted(T data) {
                    //progressBar.setVisibility(View.GONE);
                    if (data != null) {
                        rocustom=(RentOrderCustomClass)data;

                        billing.setText(rocustom.BillingNo+" | "+MainApplication.dateToString("dd/MM/yyyy",rocustom.BillingDate));
                        order.setText(rocustom.Nomor+" | "+MainApplication.dateToString("dd/MM/yyyy",rocustom.OrderDate));
                        totalCharge.setText("IDR "+ NumberFormat.getInstance().format(rocustom.BillingAmount));
                        paid.setText("IDR "+ NumberFormat.getInstance().format(rocustom.BillingPaid));
                        outstanding.setText("IDR "+ NumberFormat.getInstance().format(rocustom.BillingAmount-rocustom.Discount-rocustom.BillingPaid));
                        *//*if ((MainApplication.memberContent.DataMember.MemberType & 2) != 2)
                            discountLayout.setVisibility(View.GONE);
                        else
                        {
                            discount.setText("IDR "+ NumberFormat.getInstance().format(rocustom.Discount));
                        }*//*
                        if (rocustom.Discount!=0)
                            discount.setText("IDR "+ NumberFormat.getInstance().format(rocustom.Discount));
                        else
                            discountLayout.setVisibility(View.GONE);

                        layout.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public <T> void onPostedError(Exception data) {
                    //progressBar.setVisibility(View.GONE);
                    Toast.makeText(activity,activity.getResources().getString(R.string.fetch_data_problem),Toast.LENGTH_SHORT).show();
                }
            });
        }*/

    }

    void bindCompanyBank(CompanyBank cb)
    {
        cvBank.setTag(cb);
        bankName.setText(cb.Bank.Name);
        branchOffice.setText(cb.Branch);
        accountNo.setText(cb.AccountNo);
        accountName.setText(cb.AccountName);

        String path=MainApplication.getUrlApplWeb()+"/Images/bank/"+cb.Bank.Logo;
        Picasso.with(activity)
                .load(path)
                .placeholder(R.drawable.no_picture)
                .error(R.drawable.no_picture)
                .into(logobank);

    }
}
