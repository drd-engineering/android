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
import android.widget.Toast;

import com.makaya.drd.domain.MemberLogin;
import com.makaya.drd.domain.MemberTopupDeposit;
import com.makaya.drd.library.NumberTextWatcher;
import com.makaya.drd.library.PublicFunction;
import com.makaya.drd.service.MemberTopupDepositService;

import java.text.NumberFormat;

/**
 * Created by xbudi on 15/11/2016.
 */

public class MemberTopupDepositActivity extends AppCompatActivity {

    Activity activity;
    Button btnContinue;
    CheckBox checkBox;

    EditText paidAmount;
    TextInputLayout inputLayoutPaidAmount;
    int minTopupDeposit;

    CardView cv;
    MainApplication global;
    SessionManager session;
    MemberLogin member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_topup_deposit);
        activity=this;
        global=(MainApplication)getApplication();
        session=new SessionManager(getApplication());
        member=session.getUserLogin();

        PublicFunction.setHeaderStatus(activity,"DRD Point");

        cv=findViewById(R.id.cv);

        minTopupDeposit=getResources().getInteger(R.integer.minTopupDeposit);
        String hint="Buy DRD Point Amount (Minimal "+NumberFormat.getInstance().format(minTopupDeposit)+")";
        inputLayoutPaidAmount=(TextInputLayout) findViewById(R.id.inputLayoutPaidAmount);
        inputLayoutPaidAmount.setHint(hint);

        paidAmount=(EditText)findViewById(R.id.paidAmount);
        paidAmount.addTextChangedListener(new NumberTextWatcher(paidAmount));
        paidAmount.setHint("");

        initButton();
    }

    void initButton()
    {
        btnContinue=(Button)findViewById(R.id.btnContinue);
        btnContinue.setEnabled(false);
        btnContinue.setTextColor(Color.DKGRAY);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doContinue();
            }
        });

        checkBox=(CheckBox) findViewById(R.id.checkBox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                btnContinue.setEnabled(isChecked);
                btnContinue.setTextColor((isChecked?Color.WHITE:Color.DKGRAY));
            }
        });

    }

    void doContinue()
    {
        if (paidAmount.getText().toString().isEmpty() || paidAmount.getText().toString().equals("0")){
            Toast.makeText(activity,"Amount to be paid must be filled!",Toast.LENGTH_SHORT).show();
            paidAmount.requestFocus();
            return;
        }

        //double balance=(isCustom?rocustom.DealAmount-rocustom.BillingPaid:rohistory.BillingAmount-rohistory.BillingPaid);
        int minTopupDeposit=getResources().getInteger(R.integer.minTopupDeposit);
        if (PublicFunction.toDouble(paidAmount)<minTopupDeposit)
        {
            Toast.makeText(activity,"Minimum DRD Point "+ NumberFormat.getInstance().format(minTopupDeposit)+".",Toast.LENGTH_SHORT).show();
            paidAmount.requestFocus();
            return;
        }

        PublicFunction.hideKeyboard(activity);

        btnContinue.setEnabled(false);
        btnContinue.setTextColor(Color.DKGRAY);
        btnContinue.setText("Process...");

        MemberTopupDeposit tu=new MemberTopupDeposit();
        tu.Amount=PublicFunction.toDouble(paidAmount);
        tu.MemberId=member.Id;
        /*Toast.makeText(activity,tu.Amount+" - "+PublicFunction.toLong(paidAmount),Toast.LENGTH_SHORT).show();
        return;*/
        MemberTopupDepositService srv=new MemberTopupDepositService(activity);
        srv.setOnDataPostedListener(new MemberTopupDepositService.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data, boolean isLoadedAllItems) {
                btnContinue.setEnabled(true);
                btnContinue.setTextColor(Color.WHITE);
                btnContinue.setText("Continue");

                MemberTopupDeposit mtc=(MemberTopupDeposit)data;
                Intent intent = new Intent(activity, MemberTopupDepositSuccessActivity.class);
                intent.putExtra("TrxId",(long)mtc.Id);
                intent.putExtra("TrxNo",mtc.TopupNo);
                intent.putExtra("TrxDate",PublicFunction.dateToString("dd/MM/yyyy",mtc.TopupDate));
                intent.putExtra("TrxAmount",mtc.Amount);
                intent.putExtra("TrxType","MTU");
                intent.putExtra("TrxDescr","Member buy DRD Point");
                activity.startActivity (intent);

                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }

            @Override
            public <T> void onDataError() {
                btnContinue.setEnabled(true);
                btnContinue.setTextColor(Color.WHITE);
                btnContinue.setText("Continue");
            }
        });
        srv.saveData(tu);
    }

}
