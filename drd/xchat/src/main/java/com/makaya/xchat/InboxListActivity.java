package com.makaya.xchat;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.makaya.xchat.domain.MemberLite;
import com.makaya.xchat.domain.MemberLiteRoot;
import com.makaya.xchat.domain.XChatLogin;
import com.makaya.xchat.domain.MessageSumClass;
import com.makaya.xchat.domain.MessageSumContent;
import com.makaya.xchat.library.PublicFunction;
import com.makaya.xchat.service.MessageService;
import com.paginate.Paginate;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by xbudi on 20/10/2016.
 */

public class InboxListActivity extends AppCompatActivity {
    Activity activity;
    ArrayList<MessageSumClass> msgs=new ArrayList<>();
    ArrayList<MemberLite> advisors;
    Timer timer;
    MyTimerTask myTimerTask;
    FloatingActionButton fabAdd;
    XChatLogin user;
    Handler handler;
    MainApplication main;
    String topCriteria="";

    RecyclerView rvAdvisor;
    InboxListAdapter adapter;
    int screenWidth;
    long lastId=0;
    TextView search;
    ImageView imgClear;

    BroadcastReceiver mReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            /*if (!intent.getAction().equals("DrdService"))
                return;

            boolean isready=intent.getBooleanExtra("READY",false);
            if (isready) {
                Intent localIntent = new Intent("DrdActivity");
                localIntent.putExtra("READY", true);
                LocalBroadcastManager.getInstance(context).sendBroadcast(localIntent);
            }*/
            //Toast.makeText(activity,"terima di xchat",Toast.LENGTH_SHORT).show();
            fetchDataRefresh(user.Id, lastId, topCriteria);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inbox_list);
        activity=this;
        main=new MainApplication(activity);
        user=(XChatLogin)getIntent().getSerializableExtra("User");
        PublicFunction.setHeaderStatus(activity, "Chat", new PublicFunction.BackCallbackInterface() {
            @Override
            public void onClickBack() {
                onBackPressed();
            }
        });
        handler=new Handler();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        //int height = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;
        initObject();

        fetchAdvisor();
        startServiceReceiver();


        // sementara tidak pake service
        timer = new Timer();
        myTimerTask = new MyTimerTask();
        timer.schedule(myTimerTask,main.getInboxTimer(), main.getInboxTimer());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;

        if (requestCode == 1){
            fetchDataRefresh(user.Id, lastId, topCriteria);
        }else if (requestCode == 2){
            Intent intent = new Intent(activity, InboxDetailActivity.class);
            MemberLite msg=(MemberLite)data.getSerializableExtra("MemberLite");
            intent.putExtra("yourFoto",msg.ImageProfile);
            intent.putExtra("yourId",msg.Id);
            intent.putExtra("yourName",msg.Name);
            intent.putExtra("yourProfession",msg.Profession);
            intent.putExtra("yourType",msg.MemberType);
            intent.putExtra("meId",user.Id);
            intent.putExtra("meName",user.Name);
            intent.putExtra("User",user);
            startActivityForResult (intent, 1);
        }
    }

    @Override
    public void onBackPressed()
    {
        stopServiceReceiver();
        // code here to show dialog
        //timer.cancel();
        finish();
        //super.onBackPressed();  // optional depending on your needs
    }

    private void startServiceReceiver()
    {
        LocalBroadcastManager.getInstance(activity).registerReceiver(mReceiver, new IntentFilter("XchatReceiver"));
    }
    private void stopServiceReceiver()
    {
        LocalBroadcastManager.getInstance(activity).unregisterReceiver(mReceiver);
    }

    private void initObject()
    {
        fabAdd = findViewById(R.id.fabAdd);
        search= findViewById(R.id.search);
        imgClear= findViewById(R.id.imgClear);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activity, ContactListActivity.class);
                intent.putExtra("User",user);
                startActivityForResult(intent, 2);
            }
        });
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
                    msgs=new ArrayList<>();
                    loadingInProgress=true;
                    fetchData(user.Id, 0, topCriteria, current_page);
                    return true;
                }
                return false;
            }
        });
    }

    private void bindRecyclerView() {
        RecyclerView rv = findViewById(R.id.rv);
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this.getBaseContext());
        rv.setLayoutManager(llm);

        adapter = new InboxListAdapter(msgs, activity, user);
        rv.setAdapter(adapter);

        int paginateLoadingTriggerThreshold=getResources().getInteger(R.integer.paginateLoadingTriggerThreshold);
        Paginate.with(rv, callbacks)
                .setLoadingTriggerThreshold(paginateLoadingTriggerThreshold)
                .addLoadingListItem(true)
                //.setLoadingListItemCreator(new CustomLoadingListItemCreator())
                //.setLoadingListItemSpanSizeLookup(new CustomLoadingListItemSpanLookup())
                .build();
    }

    private void bindRecyclerViewAdvisor() {
        rvAdvisor = findViewById(R.id.rvAdvisor);
        rvAdvisor.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this.getBaseContext(),
                LinearLayoutManager.HORIZONTAL, false);
        rvAdvisor.setLayoutManager(llm);

        InboxListAdvisorAdapter adapter = new InboxListAdvisorAdapter(advisors, activity, user,screenWidth);
        rvAdvisor.setAdapter(adapter);
    }

    int current_page = 1;
    boolean loadingInProgress=false;
    boolean hasLoadedAllItems=false;
    Paginate.Callbacks callbacks = new Paginate.Callbacks() {
        @Override
        public void onLoadMore() {
            // Load next page of data (e.g. network or database)
            loadingInProgress=true;
            fetchData(user.Id, lastId, topCriteria, current_page);
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

    private void fetchData(long userId, final long maxId, String topCriteria, int page)
    {
        MessageService svr=new MessageService(activity);
        svr.setOnDataPostedListener(new MessageService.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data, boolean isLoadedAllItems) {
                ArrayList<MessageSumClass> datas=((MessageSumContent) data).Root;
                if (datas.size()>0)
                    lastId=datas.get(0).Id;

                current_page++;
                loadingInProgress=false;
                hasLoadedAllItems=isLoadedAllItems;

                msgs.addAll(datas);
                adapter.setData(msgs);
                adapter.notifyDataSetChanged();
            }

            @Override
            public <T> void onDataError() {

            }
        });
        svr.fetchDataSummary(userId, maxId, topCriteria, page);
    }

    private void fetchDataRefresh(long userId, long maxId, String topCriteria)
    {
        MessageService svr=new MessageService(activity);
        svr.setOnDataPostedListener(new MessageService.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data, boolean isLoadedAllItems) {
                ArrayList<MessageSumClass> datas=((MessageSumContent) data).Root;
                if (datas.size()==0)
                    return;
                lastId=datas.get(0).Id;
                for(MessageSumClass dt:datas){
                    for(int i=msgs.size()-1;i>=0;i--){
                        MessageSumClass msg=msgs.get(i);
                        if (msg.SenderId==dt.SenderId) {
                            dt.Unread+=msgs.get(i).Unread;
                            msgs.remove(i);
                            break;
                        }
                    }
                }

                msgs.addAll(0, datas);
                adapter.setData(msgs);
                adapter.notifyDataSetChanged();
            }

            @Override
            public <T> void onDataError() {

            }
        });
        svr.fetchDataSummary(userId, maxId, topCriteria, 1);
    }

    private void fetchAdvisor()
    {
        MessageService svr=new MessageService(activity);
        svr.setOnDataPostedListener(new MessageService.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data, boolean isLoadedAllItems) {
                bindRecyclerView();
                if (data==null)
                    return;

                advisors=((MemberLiteRoot)data).Root;
                bindRecyclerViewAdvisor();
            }

            @Override
            public <T> void onDataError() {

            }
        });
        svr.fetchAdvisor();
    }

    class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(new Runnable(){

                @Override
                public void run() {
                    fetchDataRefresh(user.Id, lastId, topCriteria);
                }});
        }
    }
}
