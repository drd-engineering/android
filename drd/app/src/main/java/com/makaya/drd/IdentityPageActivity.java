package com.makaya.drd;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.makaya.drd.domain.Dashboard;
import com.makaya.drd.domain.MemberLogin;
import com.makaya.drd.library.BadgeView;
import com.makaya.drd.library.PickImage;
import com.makaya.drd.library.PublicFunction;
import com.makaya.drd.service.MemberService;
import com.squareup.picasso.Picasso;

/**
 * Created by xbudi on 03/01/2018.
 */

public class IdentityPageActivity extends AppCompatActivity {
    Activity activity;

    ImageView foto;
    ImageView barcode;
    ImageView home;
    ImageView profile;

    TextView name;
    TextView email;
    TextView idNumber;
    TextView joinDate;
    TextView expDate;
    ImageView btnLogout;

    PickImage pickImg;
    SessionManager session;
    MemberLogin user;
    BadgeView badgeHome;
    Boolean isUnreg=false;

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            bindDashboard();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.identity_page);

        activity=this;

        session=new SessionManager(activity);
        user=session.getUserLogin();
        badgeHome=new BadgeView(activity);

        PublicFunction.setStatusBarColor(activity);
        PublicFunction.setBackgroundColorBar(activity,new int[]{R.id.content, R.id.footer});
        PublicFunction.setLogo(activity, R.id.logo);
        pickImg=new PickImage(activity);
        initObject();
        bindObject();
        PublicFunction.setLogo(activity, R.id.logo);
        startServiceReceiver();

        bindDashboard();

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode != Activity.RESULT_OK)
            return;
        if (requestCode == 1) {
            user=session.getUserLogin();
            bindPhotoProfile();
        }else if (requestCode == 2) {
            int ackClose=data.getIntExtra("AckClose",0);
            if (ackClose==1) {
                session.LogoutUser();
                finish();
            }else {
                user = session.getUserLogin();
                finish();
                startActivity(getIntent());
            }
        }
    }

    /*private void bindLogo()
    {
        if (user.Company.Image2==null)
            return;

        String path = MainApplication.getUrlApplWeb() + "/Images/appzone/" + user.Company.Image2;
        Picasso.with(activity)
                .load(path)
                //.placeholder(R.drawable.barcode)
                //.error(R.drawable.qrbarcode)
                .into(logo);
    }*/

    void bindDashboard()
    {
        Dashboard dsh=session.getDashboard();

        /*badgeHome.setTargetView(home);
        badgeHome.setBadgeCount(dsh.InboxUnread);
        badgeHome.setShadowLayer(2, -1, -1, Color.GREEN);
        badgeHome.setBadgeGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);*/

        /*int badgeno=dsh.InboxUnread;
        badgeno+= dsh.ContentProfileCount;
        badgeno+= dsh.ContentInformationCount;
        badgeno+= dsh.VideoTubeCount;
        badgeno+= dsh.PodcastCount;
        badgeno+= dsh.ERegistrationCount;
        // cukup di pasang di service
        boolean success = ShortcutBadger.applyCount(activity, badgeno);*/
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

    private void initObject()
    {
        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        idNumber=findViewById(R.id.idNumber);
        joinDate=findViewById(R.id.joinDate);
        expDate=findViewById(R.id.expDate);

        foto=findViewById(R.id.foto);
        barcode=findViewById(R.id.barcode);
        profile=findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(activity, MemberProfileActivity.class);
                startActivityForResult(intent,1);*/
            }
        });
        home=findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewDashboard();
            }
        });

        btnLogout=(ImageView)findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new android.app.AlertDialog.Builder( activity )
                        .setTitle( "Confirmation" )
                        .setMessage( "Are you sure will logout?" )
                        .setNegativeButton( "No", null )
                        .setPositiveButton( "Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                logout();

                            }
                        })
                        .show();

            }
        });

        barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if (user.PartnerId!=0) {
                    /*
                    Intent intent = new Intent(activity, MerchantTransactionActivity.class);
                    activity.startActivity(intent);*/
                //}
            }
        });


    }

    private void bindObject(){
        name.setText(user.Name);
        email.setText(user.Email);
        /*idNumber.setText(user.RegNumber);
        joinDate.setText(PublicFunction2.dateToString("dd/MM/yyyy", user.DateCreated));*/
        email.setText(user.Email);

        //expDate.setText(PublicFunction.dateToString("dd/MM/yyyy", PublicFunction.dateAdd(user.DateCreated,Calendar.YEAR,1)));

        if (user.ImageProfile!=null) {
            bindPhotoProfile();
        }

        if (user.ImageQrCode!=null){
            String path = MainApplication.getUrlApplWeb() + "/Images/member/qrbarcode/" + user.ImageQrCode;
            Picasso.with(activity)
                    .load(path)
                    .placeholder(R.drawable.barcode)
                    .error(R.drawable.qrbarcode)
                    .into(barcode);
        }
    }

    private void bindPhotoProfile()
    {
        String path = MainApplication.getUrlApplWeb() + "/Images/member/" + user.ImageProfile;
        Picasso.with(activity)
                .load(path)
                .placeholder(R.drawable.pic_male)
                .error(R.drawable.pic_male)
                .into(foto);
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

    void viewDashboard()
    {
        Intent intent = new Intent(activity, DashboardActivity.class);
        startActivity(intent);
        finish();
    }

    private void setPhotoProfile(Bitmap bitmap)
    {
        /*Bitmap circularBitmap = pickImg.getRoundedCornerBitmap(bitmap, 20);

        ImageView profileFoto = (ImageView)findViewById(R.id.profileFoto);
        profileFoto.setImageBitmap(circularBitmap);*/
    }
}
