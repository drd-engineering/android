package com.makaya.drd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.makaya.drd.domain.Bank;
import com.makaya.drd.domain.BankRoot;
import com.makaya.drd.domain.GenericData;
import com.makaya.drd.domain.MemberAccount;
import com.makaya.drd.domain.MemberLogin;
import com.makaya.drd.library.PublicFunction;
import com.makaya.drd.service.PostDataModelUrl;

import java.util.ArrayList;

/**
 * Created by xbudi on 15/11/2016.
 */

public class MemberAccountActivity extends AppCompatActivity {

    ArrayList<GenericData> banks=new ArrayList<>();
    Activity activity;
    MemberAccount memberAccount;

    long Id=0;
    Button btnSubmit;
    TextView title;
    TextView bank;
    TextView accountNo;
    TextView accountName;
    TextView branch;
    ProgressBar progressBar;

    MainApplication global;
    SessionManager session;
    MemberLogin member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_account);
        activity=this;
        global=(MainApplication)getApplication();
        session=new SessionManager(getApplication());
        member=session.getUserLogin();
        progressBar= (ProgressBar) findViewById(R.id.progressBar);

        PublicFunction.setBackgroundColorPage(activity,new int[]{R.id.layout});
        PublicFunction.setHeaderStatus(activity,"Member Account");
        initButton();
        fetchDataBank();

        Id=getIntent().getLongExtra("Id",0);
        if (Id!=0)
            fetchData();
        else
            progressBar.setVisibility(View.GONE);

    }

    void initButton()
    {
        btnSubmit=(Button)findViewById(R.id.btnSubmit);
        title=(TextView)findViewById(R.id.title);
        bank=(TextView)findViewById(R.id.bank);
        accountNo=(TextView)findViewById(R.id.accountNo);
        accountName=(TextView)findViewById(R.id.accountName);
        branch=(TextView)findViewById(R.id.branch);

        bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupDialogList pd=new PopupDialogList(v, R.id.bank);
                pd.Show(R.layout.popup_list);
                pd.BindData(banks);
                pd.setOnSelectedListener(new PopupDialogList.OnSelectedListener() {
                    @Override
                    public void onSelected(Object data) {

                    }
                });
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });
    }

    private void submitForm()
    {
        if (title.getText().toString().trim().isEmpty()){
            Toast.makeText(activity,"Title data is not yet filled!",Toast.LENGTH_SHORT).show();
            title.requestFocus();
            return;
        }
        if (accountNo.getText().toString().trim().isEmpty()){
            Toast.makeText(activity,"Account no data is not yet filled!",Toast.LENGTH_SHORT).show();
            accountNo.requestFocus();
            return;
        }
        if (accountName.getText().toString().trim().isEmpty()){
            Toast.makeText(activity,"Account name data is not yet filled!",Toast.LENGTH_SHORT).show();
            accountName.requestFocus();
            return;
        }
        if (bank.getText().toString().trim().isEmpty()){
            Toast.makeText(activity,"Bank data is not yet filled!",Toast.LENGTH_SHORT).show();
            bank.requestFocus();
            return;
        }
        if (branch.getText().toString().trim().isEmpty()){
            Toast.makeText(activity,"Branch data is not yet filled!",Toast.LENGTH_SHORT).show();
            branch.requestFocus();
            return;
        }


        if (Id==0)
            memberAccount=new MemberAccount();

        memberAccount.Id=Id;
        memberAccount.MemberId=member.Id;
        memberAccount.Title=title.getText().toString();
        memberAccount.AccountNo=accountNo.getText().toString();
        memberAccount.AccountName=accountName.getText().toString();
        memberAccount.BankId=(int)((GenericData)bank.getTag()).Id;
        memberAccount.Branch=branch.getText().toString();

        Class cls;
        int resId;
        if (Id!=0) {
            cls=Integer.class;
            resId=R.string.api_member_account_update;
        }else{
            cls=MemberAccount.class;
            resId=R.string.api_member_account_save;
        }
        btnSubmit.setText("Saving...");
        btnSubmit.setEnabled(false);

        String url=String.format(getResources().getString(resId), MainApplication.getUrlApplApi());
        PostDataModelUrl postData=new PostDataModelUrl();
        postData.execute(url, memberAccount, cls);
        postData.setOnDataPostedListener(new PostDataModelUrl.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data) {
                btnSubmit.setText("SUBMIT");
                btnSubmit.setEnabled(true);
                if (!data.getClass().equals(MemberAccount.class)) {
                    if (Integer.parseInt(data.toString())==1)
                    {
                        PublicFunction.hideKeyboard(activity);
                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                        Toast.makeText(activity, "Update account success!", Toast.LENGTH_SHORT).show();

                    }
                    else
                        Toast.makeText(activity,activity.getResources().getString(R.string.save_data_problem),Toast.LENGTH_SHORT).show();
                }else {
                    PublicFunction.hideKeyboard(activity);
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                    Toast.makeText(activity, "Save account success!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public <T> void onPostedError(Exception data) {
                btnSubmit.setText("SUBMIT");
                btnSubmit.setEnabled(true);
                Toast.makeText(activity,activity.getResources().getString(R.string.save_data_problem),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchData()
    {
        String url=String.format(getResources().getString(R.string.api_member_account_get), MainApplication.getUrlApplApi(), Id);

        final PostDataModelUrl posData = new PostDataModelUrl();
        posData.execute(url, MemberAccount.class);
        posData.setOnDataPostedListener(new PostDataModelUrl.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data) {
                progressBar.setVisibility(View.GONE);
                if (data!=null) {
                    memberAccount=(MemberAccount)data;
                    GenericData banktag=new GenericData();
                    banktag.Id=memberAccount.BankId;
                    banktag.Descr=memberAccount.Bank.Name;

                    title.setText(memberAccount.Title);
                    bank.setText(memberAccount.Bank.Name);
                    bank.setTag(banktag);
                    accountNo.setText(memberAccount.AccountNo);
                    accountName.setText(memberAccount.AccountName);
                    branch.setText(memberAccount.Branch);
                }
            }

            @Override
            public <T> void onPostedError(Exception data) {
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    void fetchDataBank()
    {

        String url=String.format(getResources().getString(R.string.api_get_bank), MainApplication.getUrlApplApi());

        final PostDataModelUrl posData = new PostDataModelUrl();
        posData.execute(url, BankRoot.class);
        posData.setOnDataPostedListener(new PostDataModelUrl.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data) {
                if (data!=null) {
                    for(Bank bc: ((BankRoot)data).Root){
                        if ((bc.BankType & 1)==1) {
                            GenericData gc = new GenericData();
                            gc.Id = bc.Id;
                            gc.Descr = bc.Name;
                            banks.add(gc);
                        }
                    }
                }
            }

            @Override
            public <T> void onPostedError(Exception data) {

            }
        });
    }
}
