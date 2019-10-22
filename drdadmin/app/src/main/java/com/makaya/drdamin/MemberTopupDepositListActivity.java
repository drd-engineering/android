package com.makaya.drdamin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.makaya.drdamin.domain.FilterData;
import com.makaya.drdamin.domain.MemberTopupDeposit;
import com.makaya.drdamin.domain.SortData;
import com.makaya.drdamin.service.MemberTopupDepositService;
import com.makaya.drdamin.service.PublicFunction;
import com.paginate.Paginate;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by xbudi on 19/09/2016.
 */
public class MemberTopupDepositListActivity extends Activity {

    ArrayList<MemberTopupDeposit> topups=new ArrayList<>();
    Activity activity;
    MemberTopupDepositListAdapter adapter;
    FloatingActionButton fabAdd;
    MainApplication global;
    MemberTopupDepositService dataService;
    String command;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_topup_deposit_list);
        activity=this;
        command=getIntent().getStringExtra("Command");
        PublicFunction.setStatusBarColor(this, R.color.colorStatus);
        global=(MainApplication)getApplication();
        TextView title=(TextView)findViewById(R.id.appBarTitle);
        title.setText("Topup Deposit List | "+command.toUpperCase());
        title.setVisibility(View.VISIBLE);

        dataService=new MemberTopupDepositService(activity);
        dataService.setOnDataPostedListener(new MemberTopupDepositService.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data, boolean isLoadedAllItems) {
                current_page++;
                loadingInProgress=false;
                hasLoadedAllItems=isLoadedAllItems;
                topups.addAll((ArrayList<MemberTopupDeposit>)data);
                if (isLoadedAllItems && topups.size()==0)
                    Toast.makeText(getBaseContext(), "Data not found.", Toast.LENGTH_SHORT).show();

                adapter.setData(topups);
                adapter.notifyDataSetChanged();
            }

            @Override
            public <T> void onDataError() {
                loadingInProgress=false;
            }
        });

        //fetchData();
        bindRecyclerView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                refresh();
            }
        }
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
        adapter.setOnProcessListener(new MemberTopupDepositListAdapter.OnSetProcessListener() {
            @Override
            public <T> void onProcess() {
                refresh();
            }
        });
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
            dataService.fetchDataCommand(command, current_page,sortdata,filterdata);
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

}
