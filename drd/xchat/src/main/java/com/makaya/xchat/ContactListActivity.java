package com.makaya.xchat;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.makaya.xchat.domain.MemberLite;
import com.makaya.xchat.domain.MemberLiteRoot;
import com.makaya.xchat.domain.XChatLogin;
import com.makaya.xchat.library.PublicFunction;
import com.makaya.xchat.service.MessageService;
import com.paginate.Paginate;

import java.util.ArrayList;

/**
 * Created by xbudi on 20/10/2016.
 */

public class ContactListActivity extends AppCompatActivity {
    Activity activity;
    ArrayList<MemberLite> contacts=new ArrayList<>();
    XChatLogin user;
    Handler handler;
    MainApplication main;
    String topCriteria="";
    TextView search;
    ImageView imgClear;
    ContactListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_list);
        activity=this;
        PublicFunction.setStatusBarColor(activity, android.R.color.white);
        main=new MainApplication(activity);
        user=(XChatLogin)getIntent().getSerializableExtra("User");
        handler=new Handler();
        initObject();
        bindRecyclerView();
    }


    @Override
    public void onBackPressed()
    {
        // code here to show dialog
        super.onBackPressed();  // optional depending on your needs
    }
    private void initObject()
    {
        search= findViewById(R.id.search);
        imgClear= findViewById(R.id.imgClear);

        imgClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setText("");
            }
        });
        search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    PublicFunction.hideKeyboard(activity);
                    topCriteria=search.getText().toString();
                    current_page = 1;
                    contacts=new ArrayList<>();
                    loadingInProgress=true;
                    fetchData(user.Id, topCriteria, current_page);
                    return true;
                }
                return false;
            }
        });

    }

    private void bindRecyclerView()
    {
        RecyclerView rv = findViewById(R.id.rv);
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this.getBaseContext());
        rv.setLayoutManager(llm);

        adapter = new ContactListAdapter(contacts, activity, user);
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
            fetchData(user.Id, topCriteria, current_page);
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

    private void fetchData(long userId, String topCriteria, int page)
    {
        MessageService svr=new MessageService(activity);
        svr.setOnDataPostedListener(new MessageService.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data, boolean isLoadedAllItems) {
                ArrayList<MemberLite> datas=((MemberLiteRoot) data).Root;
                current_page++;
                loadingInProgress=false;
                hasLoadedAllItems=isLoadedAllItems;

                contacts.addAll(datas);
                adapter.setData(contacts);
                adapter.notifyDataSetChanged();
            }

            @Override
            public <T> void onDataError() {

            }
        });
        svr.fetchContact(userId, topCriteria, page);
    }

}
