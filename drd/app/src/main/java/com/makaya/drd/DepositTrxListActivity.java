package com.makaya.drd;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.InputType;

import com.makaya.drd.domain.FilterData;
import com.makaya.drd.domain.MemberDepositTrx;
import com.makaya.drd.domain.MemberLogin;
import com.makaya.drd.domain.SortData;
import com.makaya.drd.library.DisplayToast;
import com.makaya.drd.library.PublicFunction;
import com.makaya.drd.service.DepositTrxService;
import com.paginate.Paginate;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by xbudi on 15/11/2016.
 */

public class DepositTrxListActivity extends AppCompatActivity {

    ArrayList<MemberDepositTrx> trxs = new ArrayList<>();
    Activity activity;
    MainApplication global;
    SessionManager session;
    MemberLogin member;

    String sortdata="TrxDate desc";
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

    DepositTrxService dataService;
    DepositTrxListAdapter adapter;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deposit_trx_list);
        global=(MainApplication)getApplication();
        session=new SessionManager(getApplication());
        member=session.getUserLogin();
        activity = this;
        PublicFunction.setHeaderStatus(activity,"Deposit Transaction");
        handler=new Handler();
        dataService=new DepositTrxService(activity);
        dataService.setOnDataPostedListener(new DepositTrxService.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data, boolean isLoadedAllItems) {
                current_page++;
                loadingInProgress=false;
                hasLoadedAllItems=isLoadedAllItems;
                trxs.addAll((ArrayList<MemberDepositTrx>)data);
                if (isLoadedAllItems && trxs.size()==0)
                    handler.post(new DisplayToast(activity, "Data not found."));

                adapter.setData(trxs);
                adapter.notifyDataSetChanged();
            }

            @Override
            public <T> void onDataError() {
                loadingInProgress=false;
            }
        });

        bindRecyclerView();
    }


    private void bindRecyclerView()
    {
        RecyclerView rv = findViewById(R.id.rv);
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this.getBaseContext());
        llm.setStackFromEnd(true);
        llm.setReverseLayout(true);
        rv.setLayoutManager(llm);

        adapter = new DepositTrxListAdapter(activity, trxs);
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
            dataService.getLite(member.Id, null, current_page,sortdata,filterdata);
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
