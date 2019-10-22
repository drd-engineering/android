package com.makaya.drd;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.text.InputType;
import android.view.View;
import android.widget.TextView;

import com.makaya.drd.domain.Dashboard;
import com.paginate.Paginate;
import com.makaya.drd.domain.FilterData;
import com.makaya.drd.domain.MemberLogin;
import com.makaya.drd.domain.MemberTopupDeposit;
import com.makaya.drd.domain.SortData;
import com.makaya.drd.library.DisplayToast;
import com.makaya.drd.library.PublicFunction;
import com.makaya.drd.service.MemberTopupDepositService;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by xbudi on 19/09/2016.
 */
public class MemberTopupDepositListActivity extends AppCompatActivity {

    ArrayList<MemberTopupDeposit> topups=new ArrayList<>();
    Activity activity;
    MemberTopupDepositListAdapter adapter;
    FloatingActionButton fabAdd;
    MainApplication global;
    SessionManager session;
    MemberLogin member;
    MemberTopupDepositService dataService;
    MyBroadcastReceiver mReceiver;
    Boolean isUnreg=false;
    TextView balance;

    String sortdata="PaymentStatus, TopupDate desc";
    String filterdata="";
    SortData current;
    ArrayList<SortData> sortdatas=new ArrayList<>(
            Arrays.asList(
                    new SortData("Nomor","TopupNo"),
                    new SortData("Date Created from newer","DateCreated desc"),
                    new SortData("Date Created from older","DateCreated")
            )
    );

    ArrayList<FilterData> filterdatas=new ArrayList<>(
            Arrays.asList(
                    new FilterData("Nomor","TopupNo", InputType.TYPE_CLASS_TEXT, 50)
            )
    );

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_topup_deposit_list);
        activity=this;

        global=(MainApplication)getApplication();
        session=new SessionManager(getApplication());
        member=session.getUserLogin();

        PublicFunction.setHeaderStatus(activity,"DRPoint", onBackButton);

        mReceiver = new MyBroadcastReceiver();

        handler=new Handler();
        fabAdd=(FloatingActionButton)findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, MemberTopupDepositActivity.class);
                activity.startActivityForResult (intent,1);
            }
        });

        dataService=new MemberTopupDepositService(activity);
        dataService.setOnDataPostedListener(new MemberTopupDepositService.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data, boolean isLoadedAllItems) {
                current_page++;
                loadingInProgress=false;
                hasLoadedAllItems=isLoadedAllItems;
                topups.addAll((ArrayList<MemberTopupDeposit>)data);
                if (isLoadedAllItems && topups.size()==0)
                    handler.post(new DisplayToast(activity, "Data not found."));

                adapter.setData(topups);
                adapter.notifyDataSetChanged();
            }

            @Override
            public <T> void onDataError() {
                loadingInProgress=false;
            }
        });

        startServiceReceiver();
        initObject();
        bindRecyclerView();
        bindDashboard();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                refresh();
            }
        }
    }

    @Override
    protected void onDestroy() {

        stopServiceReceiver();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {

        stopServiceReceiver();
        super.onBackPressed();
        //finish();
    }

    PublicFunction.BackCallbackInterface onBackButton=new PublicFunction.BackCallbackInterface() {
        @Override
        public void onClickBack() {
            onBackPressed();
        }
    };

    private void initObject()
    {
        balance=findViewById(R.id.balance);
        balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(activity, DepositTrxListActivity.class);
                startActivity(intent);
            }
        });
    }

    void refresh()
    {
        current_page = 1;
        loadingInProgress=false;
        hasLoadedAllItems=false;
        topups.clear();
        bindRecyclerView();
    }

    private void bindRecyclerView()
    {
        RecyclerView rv = (RecyclerView)findViewById(R.id.rv);
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this.getBaseContext());
        rv.setLayoutManager(llm);

        adapter = new MemberTopupDepositListAdapter(topups,activity);
        rv.setAdapter(adapter);

        int paginateLoadingTriggerThreshold=getResources().getInteger(R.integer.paginateLoadingTriggerThreshold);
        Paginate.with(rv, callbacks)
                .setLoadingTriggerThreshold(paginateLoadingTriggerThreshold)
                .addLoadingListItem(true)
                //.setLoadingListItemCreator(new CustomLoadingListItemCreator())
                //.setLoadingListItemSpanSizeLookup(new CustomLoadingListItemSpanLookup())
                .build();
    }

    int current_page = 1;
    boolean loadingInProgress=false;
    boolean hasLoadedAllItems=false;
    Paginate.Callbacks callbacks = new Paginate.Callbacks() {
        @Override
        public void onLoadMore() {
            // Load next page of data (e.g. network or database)
            loadingInProgress=true;
            dataService.fetchDataLite(member.Id, current_page,sortdata,filterdata);
        }

        @Override
        public boolean isLoading() {
            // Indicate whether new page loading is in progress or not
            return loadingInProgress;
        }

        @Override
        public boolean hasLoadedAllItems() {
            // Indicate whether all data (pages) are loaded or not
            return hasLoadedAllItems;
        }
    };

    void startServiceReceiver()
    {
        /*LocalBroadcastManager.getInstance(activity).*/registerReceiver(mReceiver, new IntentFilter("DrdActivity"));
    }
    void stopServiceReceiver()
    {
        if (isUnreg) return;
        isUnreg=true;
        /*LocalBroadcastManager.getInstance(activity).*/unregisterReceiver(mReceiver);
    }

    void bindDashboard()
    {
        Dashboard dsh=session.getDashboard();
        TextView balance=findViewById(R.id.balance);
        balance.setText(Html.fromHtml("Balance DRD Point <b>"+ NumberFormat.getInstance().format(dsh.DepositBalance)+"</b>"));
    }

    private  class MyBroadcastReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            bindDashboard();
        }
    }


}
