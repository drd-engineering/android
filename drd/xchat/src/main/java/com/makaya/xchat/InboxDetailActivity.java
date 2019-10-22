package com.makaya.xchat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.makaya.xchat.domain.XChatLogin;
import com.makaya.xchat.domain.MessageClass;
import com.makaya.xchat.domain.MessageSumDetailClass;
import com.makaya.xchat.domain.MessageSumDetailContent;
import com.makaya.xchat.library.PublicFunction;
import com.makaya.xchat.library.PostDataModelUrl;
import com.makaya.xchat.service.MessageService;
import com.paginate.Paginate;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by xbudi on 25/10/2016.
 */

public class InboxDetailActivity extends AppCompatActivity {

    ArrayList<MessageSumDetailClass> msgs=new ArrayList<>();
    ImageView imgSend;
    EditText textMessage;
    RecyclerView rv;

    long yourId;
    String yourName;
    String yourFoto;
    String yourProfession;
    int yourType;
    long meId;
    String meName;

    Timer timer;
    MyTimerTask myTimerTask;
    XChatLogin user;
    Activity activity;
    MainApplication main;
    InboxDetailAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inbox_detail);
        activity=this;
        PublicFunction.setStatusBarColor(activity, android.R.color.white);

        main=new MainApplication(activity);
        user=(XChatLogin)getIntent().getSerializableExtra("User");

        meId=getIntent().getLongExtra("meId",0);
        meName=getIntent().getStringExtra("meName");
        yourFoto=getIntent().getStringExtra("yourFoto");
        yourName=getIntent().getStringExtra("yourName");
        yourId=getIntent().getLongExtra("yourId",0);
        yourProfession=getIntent().getStringExtra("yourProfession");
        yourType=getIntent().getIntExtra("yourType",0);

        final ImageView foto = findViewById(R.id.foto);
        String path = main.getUrlApplWeb() + "/Images/member/" + yourFoto;
        Picasso.with(activity)
                .load(path)
                .placeholder(R.drawable.ic_user)
                .error(R.drawable.ic_user)
                .into(foto, new Callback() {
                    @Override
                    public void onSuccess() {
                        PublicFunction.setPhotoProfile(foto);
                    }

                    @Override
                    public void onError() {

                    }
                });
        TextView name = findViewById(R.id.name);
        TextView profession = findViewById(R.id.profession);
        name.setText(yourName);
        profession.setText(yourProfession);
        if (yourType<4096)
            profession.setVisibility(View.GONE);
        else
            profession.setVisibility(View.VISIBLE);

        imgSend=findViewById(R.id.imgSend);
        imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textMessage.getText().toString().trim().equals(""))
                    return;
                sendMessage();
            }
        });
        textMessage=(EditText)findViewById(R.id.textMessage);
        yourId=getIntent().getLongExtra("yourId",0);

        bindRecyclerView();
        timer = new Timer();
        myTimerTask = new MyTimerTask();

        timer.schedule(myTimerTask, main.getInboxChatTimer(), main.getInboxChatTimer());
    }

    @Override
    public void onBackPressed()
    {
        // code here to show dialog
        timer.cancel();
        Intent returnIntent = new Intent();
        activity.setResult(Activity.RESULT_OK, returnIntent);
        activity.finish();
        //super.onBackPressed();  // optional depending on your needs
    }

    void sendMessage()
    {
        loadingInProgress=false;
        hasLoadedAllItems=true;

        MessageSumDetailClass newmsg=new MessageSumDetailClass();
        newmsg.IsMe=true;
        newmsg.TextMessage=textMessage.getText().toString();
        newmsg.DateMessage= PublicFunction.getToday();
        msgs.add(0, newmsg);
        adapter.setData(msgs);
        adapter.notifyDataSetChanged();

        textMessage.setText("");
        rv.scrollToPosition(0);

        MessageClass msg=new MessageClass();
        msg.FromId=user.Id;
        msg.ToId=yourId;
        msg.TextMessage=newmsg.TextMessage;

        String url = String.format(getResources().getString(R.string.api_save_msg),
                main.getUrlApplApi());
        final PostDataModelUrl posData = new PostDataModelUrl();
        posData.execute(url, msg, MessageClass.class);
        posData.setOnDataPostedListener(new PostDataModelUrl.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data) {

            }

            @Override
            public <T> void onPostedError(Exception data) {

            }
        });
    }

    private void bindRecyclerView() {
        rv = findViewById(R.id.rv);
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this.getBaseContext());
        llm.setStackFromEnd(true);
        llm.setReverseLayout(true);
        rv.setLayoutManager(llm);

        adapter = new InboxDetailAdapter(msgs);
        rv.setAdapter(adapter);

        //rv.scrollToPosition(msgs.size()-1);

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
            fetchData(current_page);
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

    private void fetchData(int page)
    {
        MessageService svr=new MessageService(activity);
        svr.setOnDataPostedListener(new MessageService.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data, boolean isLoadedAllItems) {
                ArrayList<MessageSumDetailClass> datas=((MessageSumDetailContent) data).Root;
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
        svr.fetchChat(meId, yourId, page);
    }

    private void refreshData()
    {
        MessageService svr=new MessageService(activity);
        svr.setOnDataPostedListener(new MessageService.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data, boolean isLoadedAllItems) {
                ArrayList<MessageSumDetailClass> datas=((MessageSumDetailContent) data).Root;
                current_page++;
                loadingInProgress=false;
                hasLoadedAllItems=isLoadedAllItems;

                msgs.addAll(0, datas);
                adapter.setData(msgs);
                adapter.notifyDataSetChanged();
            }

            @Override
            public <T> void onDataError() {

            }
        });
        svr.fetchNewChat(meId, yourId);

    }


    class MyTimerTask extends TimerTask {

        @Override
        public void run() {

            runOnUiThread(new Runnable(){

                @Override
                public void run() {
                    refreshData();
                }});
        }

    }
}