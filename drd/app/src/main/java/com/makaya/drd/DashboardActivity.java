package com.makaya.drd;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andremion.counterfab.CounterFab;
import com.makaya.drd.domain.NewsLite;
import com.makaya.drd.domain.Rotation;
import com.makaya.drd.domain.RotationNode;
import com.makaya.drd.domain.RotationNodeRemark;
import com.makaya.drd.service.NewsService;
import com.makaya.drd.service.PostDataModelUrl2;
import com.paginate.Paginate;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.makaya.drd.domain.Dashboard;
import com.makaya.drd.domain.MemberLogin;
import com.makaya.drd.library.PublicFunction;
import com.makaya.drd.service.MemberService;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * Created by xbudi on 31/10/2016.
 */

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    Activity activity;
    MyBroadcastReceiver mReceiver;
    Dashboard curdsh;

    ImageView foto;

    TextView inbox;
    TextView rotation;
    TextView myplan;
    TextView mycontact;

    TextView year;
    TextView month;

    CardView btnInbox;
    CardView btnRotation;
    CardView btnMyPlan;
    CardView btnMyContact;

    NewsService dashsvr;

    SwipeRefreshLayout mSwipeRefreshLayout;

    SessionManager session;
    MemberLogin user;
    Boolean isUnreg=false;

    View header;
    CounterFab cfChat;
    ArrayList<NewsLite> newsLites=new ArrayList<>();
    EventAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        activity=this;
        session=new SessionManager(getApplication());
        user=session.getUserLogin();

        curdsh=session.getDashboard();
        dashsvr=new NewsService(activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        header = navigationView.getHeaderView(0);
        bindNavHeader(header);

        PublicFunction.setStatusBarColor(activity);
        mReceiver = new MyBroadcastReceiver();
        initObject();


        /*if (Build.BRAND.equalsIgnoreCase("xiaomi")) {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
            startActivity(intent);
        }*/

        startServiceReceiver();

        String permits[]=new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CAMERA};
        PublicFunction.requestPermissions(activity, permits, 1);

        mSwipeRefreshLayout=findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                refresh();
            }
        });

        bindObject();
        bindRecyclerView();
        bindDashboard();

        // sementara tidak pake service
        startTimer();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == 1) {
            user=session.getUserLogin();
        }if (requestCode == 2) {
            boolean update=data.getBooleanExtra("UpdateFotoProfile",false);
            if (update) {
                user=session.getUserLogin();
                refresh();
                bindNavHeader(header);
            }

        }
    }

    @Override
    protected void onDestroy() {

        stopServiceReceiver();
        super.onDestroy();
    }

    int exitCounter=0;
    Handler timerHandler;
    Runnable timerRunnable;
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (exitCounter==1)
            {

                stopServiceReceiver();
                //finish();
                super.onBackPressed();
            }else{
                Toast.makeText(activity,"Press again to exit!",Toast.LENGTH_SHORT).show();
                exitCounter++;
                timerHandler = new Handler();
                timerRunnable = new Runnable() {

                    @Override
                    public void run() {
                        exitCounter=0;
                        timerHandler.removeCallbacks(timerRunnable);

                    }
                };

                timerHandler.postDelayed(timerRunnable, 1000*5);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(activity, SettingActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_myprofile) {
            Intent intent = new Intent(activity, MemberProfileActivity.class);
            startActivityForResult(intent,2);
        }else if (id == R.id.nav_deposit) {
            Intent intent = new Intent(activity, MemberTopupDepositListActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_myplan) {
            Intent intent = new Intent(activity, MyPlanActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_myaccount) {
            Intent intent = new Intent(activity, MemberAccountListActivity.class);
            startActivity(intent);
        }/*else if (id == R.id.nav_chat) {
            Intent intent = new Intent(activity, com.makaya.xchat.InboxListActivity.class);
            intent.putExtra("User", MainApplication.XLogin(user));
            startActivity(intent);
        }*/else if (id == R.id.nav_invitation) {
            Intent intent = new Intent(activity, InvitationActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_aboutus) {
            String url = getString(R.string.urlApplWeb)+"/html/aboutus.htm";
            PublicFunction.showMiniBrowser(activity,url,false,"About...");
        }/*else if (id == R.id.nav_howto) {
            String url = getString(R.string.urlApplWeb)+"/html/howto.htm";
            PublicFunction.showMiniBrowser(activity,url,false,"How to");
        }*/
        else if (id == R.id.nav_podcast) {
            Intent intent = new Intent(activity, PodCastListActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_videotube) {
            Intent intent = new Intent(activity, NewsVideoListActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_news) {
            Intent intent = new Intent(activity, NewsListActivity.class);
            intent.putExtra("Title","News & Info List");
            intent.putExtra("ContentType","1,2");
            startActivity(intent);
        }else if (id == R.id.nav_profile) {
            Intent intent = new Intent(activity, NewsListActivity.class);
            intent.putExtra("Title","Figure");
            intent.putExtra("ContentType","4");
            startActivity(intent);
        }else if (id == R.id.nav_logout) {
            new android.app.AlertDialog.Builder( activity )
                    .setTitle( "Confirmation" )
                    .setMessage( "Will you log out, continue?" )
                    .setNegativeButton( "No", null )
                    .setPositiveButton( "Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            logout();

                        }
                    })
                    .show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void refresh()
    {
        current_page = 1;
        loadingInProgress=false;
        hasLoadedAllItems=false;
        newsLites.clear();
        bindRecyclerView();
    }

    int current_page = 1;
    boolean loadingInProgress=false;
    boolean hasLoadedAllItems=false;
    Paginate.Callbacks callbacks = new Paginate.Callbacks() {
        @Override
        public void onLoadMore() {
            // Load next page of data (e.g. network or database)
            loadingInProgress=true;
            dashsvr.fetchNewsLite2(user.Id, "1,2", "",current_page,"DateCreated desc","");
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

    private void bindRecyclerView()
    {
        RecyclerView rv = findViewById(R.id.rv);
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(activity.getBaseContext());
        rv.setLayoutManager(llm);

        adapter = new EventAdapter(activity, newsLites);
        rv.setAdapter(adapter);

        Paginate.with(rv, callbacks)
                .setLoadingTriggerThreshold(2)
                .addLoadingListItem(true)
                .build();
    }

    private void logout(){
        MemberService svr=new MemberService(activity);
        svr.setOnDataPostedListener(new MemberService.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data, boolean isLoadedAllItems) {
                session.LogoutUser();
                finish();
            }

            @Override
            public <T> void onDataError() {
                session.LogoutUser();
                finish();
            }
        });
        svr.Logout(user.Id);
    }

    public MemberLogin getUserLogin(){
        return user;
    }

    private void bindNavHeader(View header)
    {
        user=session.getUserLogin();
        final ImageView image=header.findViewById(R.id.image);
        TextView name=header.findViewById(R.id.name);
        TextView email=header.findViewById(R.id.email);
        TextView regnumber=header.findViewById(R.id.regnumber);
        LinearLayout layoutMenu=header.findViewById(R.id.layoutMenu);
        TextView company=header.findViewById(R.id.company);

        name.setText(user.Name);
        email.setText(user.Email);
        regnumber.setText(user.Number);
        company.setText(user.Company.Name);

        bindPhotoProfile();

        String path = MainApplication.getUrlApplWeb() + "/Images/member/" + user.ImageProfile;
        Picasso.with(activity)
                .load(path)
                .placeholder(R.drawable.pic_male)
                .error(R.drawable.pic_male)
                .into(image, new Callback() {
                    @Override
                    public void onSuccess() {
                        PublicFunction.setPhotoProfile(image);
                    }

                    @Override
                    public void onError() {

                    }
                });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, QrCodeDisplayActivity.class);
                activity.startActivity(intent);
            }
        });
    }


    void initObject()
    {
        foto=findViewById(R.id.foto);
        bindPhotoProfile();

        btnInbox=findViewById(R.id.btnInbox);
        btnRotation=findViewById(R.id.btnRotation);
        btnMyPlan=findViewById(R.id.btnMyPlan);
        btnMyContact =findViewById(R.id.btnMyContact);

        inbox=findViewById(R.id.inbox);
        rotation=findViewById(R.id.rotation);
        myplan =findViewById(R.id.myplan);
        mycontact=findViewById(R.id.mycontact);

        year=findViewById(R.id.year);
        month=findViewById(R.id.month);

        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, QrCodeDisplayActivity.class);
                activity.startActivity(intent);
            }
        });

        btnInbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dashboard dsh=session.getDashboard();
                if (dsh.Inbox==0){
                    Toast.makeText(activity,"Couter is 0, data does not exist.",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent=new Intent(activity, InboxRotationActivity.class);
                intent.putExtra("Caption","Inbox");
                intent.putExtra("Status","00");
                startActivity(intent);


            }
        });
        btnRotation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dashboard dsh=session.getDashboard();
                if (dsh.InProgress+dsh.Completed+dsh.Declined+dsh.Pending==0){
                    Toast.makeText(activity,"Couter is 0, data does not exist.",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent=new Intent(activity, ProgressRotationActivity.class);
                intent.putExtra("Status","01,02,90,98");
                intent.putExtra("Caption","Rotation");
                startActivity(intent);
            }
        });
        btnMyPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, MyPlanActivity.class);
                startActivity(intent);
            }
        });
        btnMyContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, InvitationActivity.class);
                startActivity(intent);
            }
        });

    }

    private void bindPhotoProfile()
    {
        if (foto==null) return;

        String path = MainApplication.getUrlApplWeb() + "/Images/member/" + user.ImageProfile;
        Picasso.with(activity)
                .load(path)
                .placeholder(R.drawable.pic_male)
                .error(R.drawable.pic_male)
                .into(foto);
    }

    void sendServiceReady()
    {
        startServiceReceiver();

        Intent intent=new Intent("DrdService");
        intent.putExtra("READY",true);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    void startService(boolean receiver)
    {
        Intent serviceIntent = new Intent(getApplicationContext(), MainService.class);
        getApplicationContext().startService(serviceIntent);
        if (receiver)
            startServiceReceiver();
    }

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

    private void bindDashboard()
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Dashboard dsh=session.getDashboard();

                inbox.setText(dsh.Inbox+"");
                rotation.setText(dsh.InProgress+dsh.Completed+dsh.Declined+dsh.Pending+"");
                myplan.setText("");
                mycontact.setText(dsh.InviteAccepted+"");

                cfChat.setCount(dsh.UnreadChat);

                Date date=PublicFunction.getToday();
                year.setText(PublicFunction.dateToString("yyyy", date));
                month.setText(PublicFunction.dateToString("MMMM", date));

                //if (PublicFunction.isActivityRunning(activity, com.makaya.xchat.InboxListActivity.class)){
                Intent localIntent = new Intent("XchatReceiver");
                localIntent.putExtra("Refresh", true);
                Boolean ret = LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(localIntent);
                //}
            }
        });



    }

    private  class MyBroadcastReceiver extends BroadcastReceiver {
        //terima pesan, update UI
        //tapi jika app berada di background, tidak akan terupdate

        public void onReceive(Context context, Intent intent) {
            /*boolean isready = intent.getBooleanExtra("READY", false);
            if (isready) {
                isServiceReady = true;
            }

            if (isServiceReady)*/
                bindDashboard();
        }
    }

    public void bindObject() {



        cfChat=new CounterFab(activity);
        cfChat=findViewById(R.id.cfChat);
        cfChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, com.makaya.xchat.InboxListActivity.class);
                intent.putExtra("User", MainApplication.XLogin(user));
                startActivity(intent);
            }
        });

        dashsvr.setOnDataPostedListener(new NewsService.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data, boolean isLoadedAllItems) {
                mSwipeRefreshLayout.setRefreshing(false);
                current_page++;
                loadingInProgress=false;
                hasLoadedAllItems=isLoadedAllItems;

                newsLites.addAll((ArrayList<NewsLite>)data);

                adapter.setData(newsLites);
                adapter.notifyDataSetChanged();

            }

            @Override
            public <T> void onDataError() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ContentViewHolder> {

        Activity activity;
        ArrayList<NewsLite> datas;
        //View view;

        public class ContentViewHolder extends RecyclerView.ViewHolder {
            ImageView image;
            TextView title;
            TextView descr;
            TextView dateCreated;
            CardView cvEvent;

            public ContentViewHolder(View itemView) {
                super(itemView);
                //view = itemView;
                image = (ImageView) itemView.findViewById(R.id.image);
                title = (TextView) itemView.findViewById(R.id.title);
                descr = (TextView) itemView.findViewById(R.id.descr);
                dateCreated = (TextView) itemView.findViewById(R.id.dateCreated);
                cvEvent = itemView.findViewById(R.id.cvEvent);

            }
        }

        public EventAdapter(Activity activity, ArrayList<NewsLite> datas) {
            this.activity=activity;
            this.datas = datas;
        }

        public void setData(ArrayList<NewsLite> datas) {
            this.datas = datas;
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        @Override
        public int getItemViewType(int position) {
            return datas.get(position).type;
        }

        @Override
        public ContentViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dashboard_event_item, viewGroup, false);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView image = (ImageView) v.findViewById(R.id.image);
                    NewsLite pack = (NewsLite) image.getTag();
                    //if (pack.type==0) {
                    Intent intent = new Intent(activity, NewsDetailActivity.class);
                    intent.putExtra("DataId", pack.Id);
                    activity.startActivity(intent);
                    /*}else{
                        PublicFunction2.showDetailBanner(activity,pack.Id,pack.Title);
                    }*/
                }
            });
            return new ContentViewHolder(v);


        }

        @Override
        public void onBindViewHolder(ContentViewHolder holder, int i) {

            NewsLite pack=datas.get(i);
            holder.image.setTag(pack);
            holder.title.setText(PublicFunction.fromHtml(pack.Title));
            holder.descr.setText(PublicFunction.fromHtml(pack.Descr));

            String created = "Created " + PublicFunction.dateToString("dd/MM/yyyy", pack.DateCreated);
            if (pack.DateUpdated != null)
                created += " | Modified " + PublicFunction.dateToString("dd/MM/yyyy", pack.DateUpdated);
            holder.dateCreated.setText(created);

            holder.cvEvent.setVisibility(View.VISIBLE);

            String path = MainApplication.getUrlApplWeb() + "/Images/news/" + pack.Image;
            Picasso.with(activity)
                    .load(path)
                    .placeholder(R.drawable.initbanner)
                    .error(R.drawable.initbanner)
                    .into(holder.image);
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }
    }

    public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ContentViewHolder> {

        Activity activity;
        ArrayList<Rotation> datas;

        public class ContentViewHolder extends RecyclerView.ViewHolder {
            TextView sep;
            TextView name;
            RecyclerView rv;

            public ContentViewHolder(View itemView) {
                super(itemView);
                sep = itemView.findViewById(R.id.sep);
                name = itemView.findViewById(R.id.name);
                rv = itemView.findViewById(R.id.rv);
            }
        }

        public ItemAdapter(Activity activity, ArrayList<Rotation> datas) {
            this.activity=activity;
            this.datas = datas;
        }

        public void setData(ArrayList<Rotation> datas) {
            this.datas = datas;
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        @Override
        public ContentViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dashboard_rotation_item, viewGroup, false);
            return new ContentViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ContentViewHolder holder, int i) {
            Rotation pack=datas.get(i);
            holder.name.setText(pack.Subject);
            bindItemRecyclerView(holder.rv, pack.RotationNodes);
            if (i==0)
                holder.sep.setVisibility(View.VISIBLE);
            else
                holder.sep.setVisibility(View.GONE);
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        public void bindItemRecyclerView(RecyclerView rv, ArrayList<RotationNode> rtnodes)
        {
            rv.setHasFixedSize(true);

            LinearLayoutManager llm = new LinearLayoutManager(activity.getBaseContext());
            rv.setLayoutManager(llm);

            ItemItemAdapter adapter = new ItemItemAdapter(activity, rtnodes);
            rv.setAdapter(adapter);
        }
    }

    public class ItemItemAdapter extends RecyclerView.Adapter<ItemItemAdapter.ContentViewHolder> {

        Activity activity;
        ArrayList<RotationNode> datas;

        public class ContentViewHolder extends RecyclerView.ViewHolder {
            ImageView image;
            TextView title;
            TextView status;
            TextView name;
            TextView remark;
            TextView note;

            public ContentViewHolder(View itemView) {
                super(itemView);
                image = itemView.findViewById(R.id.image);
                title = itemView.findViewById(R.id.title);
                status = itemView.findViewById(R.id.status);
                name = itemView.findViewById(R.id.name);
                remark = itemView.findViewById(R.id.remark);
                note = itemView.findViewById(R.id.note);
            }
        }

        public ItemItemAdapter(Activity activity, ArrayList<RotationNode> datas) {
            this.activity=activity;
            this.datas = datas;
        }

        public void setData(ArrayList<RotationNode> datas) {
            this.datas = datas;
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        @Override
        public ContentViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

            View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dashboard_rotation_detail_item, viewGroup, false);
            return new ContentViewHolder(v);

        }

        @Override
        public void onBindViewHolder(final ContentViewHolder holder, int i) {

            RotationNode pack=datas.get(i);

            holder.title.setText(PublicFunction.dateToString("dd/MM/yyyy HH:mm:ss", pack.DateCreated));
            holder.status.setText(pack.StatusCode.Descr);
            holder.status.setTextColor(Color.parseColor(pack.StatusCode.TextColor));
            holder.status.setBackgroundColor(Color.parseColor(pack.StatusCode.BackColor));
            holder.name.setText(Html.fromHtml(pack.Member.Name+", Activity: <b>"+pack.WorkflowNode.Caption+"</b>"));
            holder.note.setText(pack.Note);
            String remark="";
            for(RotationNodeRemark rnr: pack.RotationNodeRemarks){
                if (!remark.equals(""))
                    remark+="\r\n";
                remark+=PublicFunction.dateToString("dd/MM/yyyy HH:mm:ss", rnr.DateStamp)+": "+rnr.Remark;
            }
            holder.remark.setText(remark);

            if (holder.note==null)
                holder.note.setVisibility(View.GONE);
            else
                holder.note.setVisibility(View.VISIBLE);

            String path = MainApplication.getUrlApplWeb() + "/Images/member/" + pack.Member.ImageProfile;
            Picasso.with(activity)
                    .load(path)
                    .placeholder(R.drawable.pic_male)
                    .error(R.drawable.pic_male)
                    .into(holder.image, new Callback() {
                        @Override
                        public void onSuccess() {
                            PublicFunction.setPhotoProfile(holder.image);
                        }

                        @Override
                        public void onError() {

                        }
                    });
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }
    }

    /*
            sementara tidak pake service
     */

    private Timer timer;
    private TimerTask timerTask;
    boolean inProcess=false;
    private void startTimer() {
        timer = new Timer();
        timerTask = new TimerTask() {
            public void run() {
                /*Log.i("Count", "=========  "+ (counter++));*/
                if (!inProcess) {
                    compareDashboard();
                }
            }
        };
        timer.schedule(timerTask, 1000*5, 1000*5); //
    }

    private void stoptimertask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void generateNotif(Dashboard db, Dashboard lastdb) {
        String notifTitle="DRD notification";
        String notifMessage="";
        if (db.UnreadChat>lastdb.UnreadChat) {
            notifMessage=db.UnreadChat+" messages\n\n";
            showNotification(notifTitle, notifMessage,9);
        }

        if (db.Inbox>lastdb.Inbox) {
            notifMessage=db.Inbox+" activities inbox\n\n";
            showNotification(notifTitle, notifMessage,10);
        }
        if (db.Altered>lastdb.Altered) {
            notifMessage=db.Altered+" activities altered\n\n";
            showNotification(notifTitle, notifMessage,11);
        }
        if (db.Pending>lastdb.Pending) {
            notifMessage=db.Pending+" activities pending\n\n";
            showNotification(notifTitle, notifMessage,12);
        }
        if (db.Declined>lastdb.Declined) {
            notifMessage=db.Declined+" activities declined\n\n";
            showNotification(notifTitle, notifMessage,13);
        }
        if (db.Completed>lastdb.Completed) {
            notifMessage=db.Completed+" activities completed\n\n";
            showNotification(notifTitle, notifMessage,14);
        }
        if (db.DepositBalance!=lastdb.DepositBalance) {
            notifMessage="Deposit balance DRD Point "+ NumberFormat.getInstance().format(db.DepositBalance)+"\n\n";
            showNotification(notifTitle, notifMessage,15);
        }

        if (db.Invitation!=lastdb.Invitation && db.Invitation>lastdb.Invitation) {
            notifMessage=(db.Invitation-lastdb.Invitation)+" invitations\n\n";
            showNotification(notifTitle, notifMessage,20);
        }
        if (db.InviteAccepted!=lastdb.InviteAccepted && db.InviteAccepted>lastdb.InviteAccepted) {
            notifMessage=(db.InviteAccepted-lastdb.InviteAccepted)+" invitations accepted\n\n";
            showNotification(notifTitle, notifMessage,21);
        }
        int badgeno=db.Inbox;
        badgeno+= db.Altered;
        badgeno+= db.Pending;
        badgeno+= db.Declined;
        badgeno+= db.Completed;
        badgeno+= db.UnreadChat;
        badgeno+= db.Invitation;

        ShortcutBadger.applyCount(this, badgeno);
    }

    private void showNotification(String title, String message, int seqNo){
        Context context=getApplicationContext();
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = seqNo;
        String channelId = getString(R.string.app_id); // harus huruf kecil semua
        String channelName = getString(R.string.app_name);
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel =
                    new NotificationChannel(channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        // define sound URI, the sound to be played when there's a notification
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.logodocurate)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setVibrate(new long[]{1000, 1000});

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(new Intent(context, DashboardActivity.class));
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                notificationId,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        mBuilder.setContentIntent(resultPendingIntent);

        notificationManager.notify(notificationId, mBuilder.build());
    }

    private void broadcastNotify() {
        //kirim pesan ke activity
        /*Intent localIntent = new Intent("DrdActivity");
        sendBroadcast(localIntent);*/
        //LocalBroadcastManager.getInstance(context).sendBroadcast(localIntent);

        bindDashboard();
    }

    private void compareDashboard() {
        user=session.getUserLogin();
        if (user.Id==0) {
            ShortcutBadger.applyCount(this, 0);
            broadcastNotify();
            inProcess=false;
            return;
        }
        inProcess=true;

        String ssss= PublicFunction.classToJson(session.getDashboard());

        String url=String.format(getResources().getString(R.string.api_dashboard_compare),
                MainApplication.getUrlApplApi(),user.Id);
        final PostDataModelUrl2 posData = new PostDataModelUrl2();

        posData.setOnDataPostedListener(new PostDataModelUrl2.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data) {
                if (data==null) {
                    inProcess=false;
                    return;
                }

                if (!(Boolean)data){
                    fetchDashboard();
                }else
                    inProcess=false;
            }

            @Override
            public <T> void onPostedError(Exception data) {
                inProcess=false;
            }
        });
        try {
            posData.postUrl(url, session.getDashboard(), Boolean.class);
        }catch (IOException x){
            inProcess=false;
        }
    }

    private void fetchDashboard() {
        String url=String.format(getResources().getString(R.string.api_dashboard_count),
                MainApplication.getUrlApplApi(),user.Id);
        final PostDataModelUrl2 posData = new PostDataModelUrl2();
        posData.setOnDataPostedListener(new PostDataModelUrl2.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data) {
                inProcess=false;
                if (data==null)
                    return;

                Dashboard ori=session.getDashboard();
                Dashboard newdb=(Dashboard)data;
                session.putDashboard(newdb);

                generateNotif(newdb,ori);
                broadcastNotify();

            }

            @Override
            public <T> void onPostedError(Exception data) {
                inProcess=false;
            }
        });
        try {
            posData.postUrl(url, session.getDashboard(), Dashboard.class);
        }catch (IOException x){
            inProcess=false;
        }
    }

}
