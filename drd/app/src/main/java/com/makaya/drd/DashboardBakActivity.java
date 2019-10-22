package com.makaya.drd;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import com.google.android.material.navigation.NavigationView;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.android.gms.ads.MobileAds;
import com.makaya.drd.domain.Dashboard;
import com.makaya.drd.domain.MemberLogin;
import com.makaya.drd.domain.Rotation;
import com.makaya.drd.domain.RotationNode;
import com.makaya.drd.domain.RotationNodeRemark;
import com.makaya.drd.library.PublicFunction;
import com.makaya.drd.service.DashboardService;
import com.makaya.drd.service.MemberService;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by xbudi on 31/10/2016.
 */

public class DashboardBakActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    Activity activity;
    MyBroadcastReceiver mReceiver;
    Dashboard curdsh;

    TextView inprogress;
    TextView pending;
    TextView signed;
    TextView declined;
    TextView completed;
    TextView inbox;

    TextView year;
    TextView month;

    FrameLayout btnCompleted;
    FrameLayout btnInbox;
    CardView btnInProgress;
    CardView btnPending;
    CardView btnSigned;
    CardView btnDecline;

    DashboardService dashsvr;

    SwipeRefreshLayout mSwipeRefreshLayout;

    SessionManager session;
    MemberLogin user;

    View header;

    ArrayList<Rotation> rotations;
    ItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        activity=this;
        //MobileAds.initialize(this, getString(R.string.admob_app_id));
        session=new SessionManager(getApplication());
        user=session.getUserLogin();

        curdsh=session.getDashboard();
        dashsvr=new DashboardService(activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        //PublicFunction2.setBackgroundColorBar(activity,new int[]{R.id.appbarlayout});
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

        startServiceReceiver();



        String permits[]=new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CALL_PHONE,
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
        //bindRecyclerView();
        fetchRotation();
        bindDashboard();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            /*super.onBackPressed();
            stopServiceReceiver();

            Intent intent = new Intent(activity, IdentityPageActivity.class);
            startActivity(intent);
            finish();*/
            if (exitCounter==1)
            {
                super.onBackPressed();
                stopServiceReceiver();
                finish();
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

        if (id == R.id.nav_myaccount) {
            Intent intent = new Intent(activity, MemberProfileActivity.class);
            startActivityForResult(intent,2);
        }else if (id == R.id.nav_deposit) {
            Intent intent = new Intent(activity, MemberTopupDepositListActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_aboutus) {
            String url = getString(R.string.urlApplWeb)+"/html/aboutus.htm";
            PublicFunction.showMiniBrowser(activity,url,false,"About...");
        }
        /*else if (id == R.id.nav_howto) {
            String url = getString(R.string.urlApplWeb)+"/html/howto.htm";
            PublicFunction.showMiniBrowser(activity,url,false,"How to");
        }*/
        else if (id == R.id.nav_logout) {
            new android.app.AlertDialog.Builder( activity )
                    .setTitle( "Konfirmasi" )
                    .setMessage( "Apakah benar akan keluar dari aplikasi?" )
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
        /*current_page = 1;
        loadingInProgress=false;
        hasLoadedAllItems=false;*/
        rotations.clear();
        fetchRotation();
    }

    /*int current_page = 1;
    boolean loadingInProgress=false;
    boolean hasLoadedAllItems=false;
    Paginate.Callbacks callbacks = new Paginate.Callbacks() {
        @Override
        public void onLoadMore() {
            // Load next page of data (e.g. network or database)
            loadingInProgress=true;
            fetchRotation();
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
    };*/

    private void bindRecyclerView()
    {
        RecyclerView rv = findViewById(R.id.rv);
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(activity.getBaseContext());
        rv.setLayoutManager(llm);

        adapter = new ItemAdapter(activity, rotations);
        rv.setAdapter(adapter);

        /*Paginate.with(rv, callbacks)
                .setLoadingTriggerThreshold(2)
                .addLoadingListItem(true)
                .build();
*/
    }

    private void fetchRotation()
    {
        /*RotationService svr=new RotationService(activity);
        svr.setOnDataPostedListener(new RotationService.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data, boolean isLoadedAllItems) {
                mSwipeRefreshLayout.setRefreshing(false);
                //loadingInProgress=false;
                if (data!=null) {
                    nodes = ((RotationRoot) data).Root;
                    bindRecyclerView();
                }
            }

            @Override
            public <T> void onDataError() {
                mSwipeRefreshLayout.setRefreshing(false);
                //loadingInProgress=false;
            }
        });
        svr.getByMemberId(user.Id);*/
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
        //PublicFunction2.setBackgroundColorBar(activity,new View[]{layoutMenu});

        name.setText(user.Name);
        email.setText(user.Email);
        regnumber.setText(user.Number);
        company.setText(user.Company.Name);

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
        /*btnCompleted=findViewById(R.id.btnCompleted);
        btnInbox=findViewById(R.id.btnInbox);
        btnInProgress=findViewById(R.id.btnInProgress);
        btnPending=findViewById(R.id.btnRotation);
        btnSigned=findViewById(R.id.btnAltered);
        btnDecline=findViewById(R.id.btnMyContact);

        inprogress=findViewById(R.id.inprogress);
        pending=findViewById(R.id.pending);
        signed=findViewById(R.id.altered);
        declined=findViewById(R.id.declined);
        completed=findViewById(R.id.completed);
        inbox=findViewById(R.id.inbox);*/

        year=findViewById(R.id.year);
        month=findViewById(R.id.month);

        btnCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dashboard dsh=session.getDashboard();
                if (dsh.Completed==0){
                    Toast.makeText(activity,"Couter is 0, data does not exist.",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent=new Intent(activity, ProgressRotationActivity.class);
                intent.putExtra("Status","90");
                intent.putExtra("Caption","Completed");
                startActivity(intent);
            }
        });
        btnInbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dashboard dsh=session.getDashboard();
                if (dsh.InProgress==0){
                    Toast.makeText(activity,"Couter is 0, data does not exist.",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent=new Intent(activity, InboxRotationActivity.class);
                intent.putExtra("Caption","Inbox");
                startActivity(intent);


            }
        });
        btnInProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dashboard dsh=session.getDashboard();
                if (dsh.InProgress==0){
                    Toast.makeText(activity,"Couter is 0, data does not exist.",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent=new Intent(activity, ProgressRotationActivity.class);
                intent.putExtra("Status","01");
                intent.putExtra("Caption","In Progress");
                startActivity(intent);
            }
        });
        btnPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity,"Under construction!",Toast.LENGTH_SHORT).show();
            }
        });
        btnSigned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity,"Under construction!",Toast.LENGTH_SHORT).show();
            }
        });
        btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dashboard dsh=session.getDashboard();
                if (dsh.Declined==0){
                    Toast.makeText(activity,"Couter is 0, data does not exist.",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent=new Intent(activity, ProgressRotationActivity.class);
                intent.putExtra("Status","98");
                intent.putExtra("Caption","Declined");
                startActivity(intent);
            }
        });
    }

    private void bindPhotoProfile(ImageView foto)
    {
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
        LocalBroadcastManager.getInstance(activity).registerReceiver(
                mReceiver, new IntentFilter("DrdActivity"));
    }
    void stopServiceReceiver()
    {
        LocalBroadcastManager.getInstance(activity).unregisterReceiver(mReceiver);
    }

    void bindDashboard()
    {
        Dashboard dsh=session.getDashboard();

        /*cart.setTargetView(btnCart);
        cart.setBadgeCount(dsh.CartCount);
        cart.setShadowLayer(2, -1, -1, Color.GREEN);
        cart.setBadgeGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);*/

        inprogress.setText(dsh.InProgress+"");
        pending.setText(dsh.Pending+"");
        signed.setText(dsh.Signed+"");
        declined.setText(dsh.Declined+"");
        completed.setText("Completed " +dsh.Completed);
        inbox.setText(dsh.Inbox+"");

        Date date=PublicFunction.getToday();
        year.setText(PublicFunction.dateToString("yyyy", date));
        month.setText(PublicFunction.dateToString("MMMM", date));
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

}
