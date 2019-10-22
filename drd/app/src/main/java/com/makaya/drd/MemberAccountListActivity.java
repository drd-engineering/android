package com.makaya.drd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.makaya.drd.domain.MemberAccount;
import com.makaya.drd.domain.MemberAccountRoot;
import com.makaya.drd.domain.MemberLogin;
import com.makaya.drd.library.PublicFunction;
import com.makaya.drd.service.PostDataModelUrl;

import java.util.ArrayList;

/**
 * Created by xbudi on 15/11/2016.
 */

public class MemberAccountListActivity extends AppCompatActivity {

    ArrayList<MemberAccount> accounts = new ArrayList<>();
    Activity activity;
    ProgressBar progressBar;
    MainApplication global;
    SessionManager session;
    MemberLogin member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_account_list);
        global=(MainApplication)getApplication();
        session=new SessionManager(getApplication());
        member=session.getUserLogin();
        activity = this;
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        /*TextView title = (TextView) findViewById(R.id.appBarTitle);
        title.setText("Member Account List");
        PublicFunction2.setStatusBarColor(this,R.color.colorBased);*/
        PublicFunction.setHeaderStatus(activity,"Member Account List");

        FloatingActionButton fabAdd= (FloatingActionButton) findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MemberAccountActivity.class);
                intent.putExtra("Id", 0);
                activity.startActivityForResult(intent,1);
            }
        });

        fetchData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode==1){
                progressBar.setVisibility(View.VISIBLE);
                fetchData();
            }
        }
    }

    private void bindRecyclerView()
    {
        RecyclerView rv = (RecyclerView)findViewById(R.id.rv);
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this.getBaseContext());
        rv.setLayoutManager(llm);

        MemberAccountListAdapter adapter = new MemberAccountListAdapter(activity, accounts);
        rv.setAdapter(adapter);
    }

    void fetchData()
    {

        String url=String.format(getResources().getString(R.string.api_member_account_getmember),
                MainApplication.getUrlApplApi(), member.Id);

        final PostDataModelUrl posData = new PostDataModelUrl();
        posData.execute(url, MemberAccountRoot.class);
        posData.setOnDataPostedListener(new PostDataModelUrl.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data) {
                progressBar.setVisibility(View.GONE);
                if (data!=null) {
                    accounts=((MemberAccountRoot)data).Root;
                    bindRecyclerView();
                }
            }

            @Override
            public <T> void onPostedError(Exception data) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
