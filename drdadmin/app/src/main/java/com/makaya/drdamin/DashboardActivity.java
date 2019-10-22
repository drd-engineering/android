package com.makaya.drdamin;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.makaya.drdamin.domain.Dashboard;
import com.makaya.drdamin.library.BadgeView;
import com.makaya.drdamin.service.PublicFunction;

/**
 * Created by xbudi on 06/03/2017.
 */

public class DashboardActivity extends AppCompatActivity {
    Activity activity;

    LinearLayout boxPendingMTD;
    LinearLayout boxConfirmationMTD;
    LinearLayout boxConfirmedMTD;
    LinearLayout boxNotconfirmedMTD;
    TextView pendingMTD;
    TextView confirmationMTD;
    TextView confirmedMTD;
    TextView notconfirmedMTD;

    ImageView logout;
    ImageView inbox;

    MainApplication global;
    SessionManager session;

    BroadcastReceiver mReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
                bindDashboard();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        activity=this;

        PublicFunction.setStatusBarColor(activity,R.color.colorStatus);
        global=(MainApplication)getApplication();
        session=new SessionManager(getApplication());
        initButton();

        String permits[]=new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_PHONE_STATE};
        PublicFunction.requestPermissions(activity, permits, 1);

        startServiceReceiver();
        bindDashboard();
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
    protected void onDestroy() {
        stopServiceReceiver();
        super.onDestroy();
    }

    void sendServiceReady()
    {
        startServiceReceiver();

        Intent intent=new Intent("DrdAdmService");
        intent.putExtra("READY",true);
        /*LocalBroadcastManager.getInstance(getApplicationContext()).*/sendBroadcast(intent);
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
        /*LocalBroadcastManager.getInstance(activity).*/registerReceiver(mReceiver, new IntentFilter("DrdAdmActivity"));
    }
    void stopServiceReceiver()
    {
        /*LocalBroadcastManager.getInstance(activity).*/unregisterReceiver(mReceiver);
    }

    <T> void callIntent(String command, Class<T> cls)
    {
        Intent intent = new Intent(DashboardActivity.this, cls);
        intent.putExtra("Command",command);
        startActivity(intent);
    }

    private void initButton()
    {
        logout=(ImageView) findViewById(R.id.logout);
        inbox=(ImageView) findViewById(R.id.inbox);

        boxPendingMTD=(LinearLayout)findViewById(R.id.boxPendingMTD);
        boxConfirmationMTD=(LinearLayout)findViewById(R.id.boxConfirmationMTD);
        boxConfirmedMTD=(LinearLayout)findViewById(R.id.boxConfirmedMTD);
        boxNotconfirmedMTD=(LinearLayout)findViewById(R.id.boxNotconfirmedMTD);
        pendingMTD=(TextView)findViewById(R.id.pendingMTD);
        confirmationMTD=(TextView)findViewById(R.id.confirmationMTD);
        confirmedMTD=(TextView)findViewById(R.id.confirmedMTD);
        notconfirmedMTD=(TextView)findViewById(R.id.notconfirmedMTD);


        boxPendingMTD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callIntent("Pending", MemberTopupDepositListActivity.class);
            }
        });
        boxConfirmationMTD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callIntent("Confirmation", MemberTopupDepositListActivity.class);
            }
        });
        boxConfirmedMTD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callIntent("Confirmed", MemberTopupDepositListActivity.class);
            }
        });
        boxNotconfirmedMTD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callIntent("Notconfirmed", MemberTopupDepositListActivity.class);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(activity)
                        .setTitle("Logout Confirmation")
                        .setMessage("Are you sure you want to logout?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                session.LogoutUser();
                                Intent intent = new Intent(activity, UserLoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                activity.startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });
    }

    void bindDashboard()
    {
        Dashboard db=session.getDashboard();

        pendingMTD.setText(db.MemberTopupPending+"");
        confirmationMTD.setText(db.MemberTopupConfirmation+"");
        confirmedMTD.setText(db.MemberTopupConfirmed+"");
        notconfirmedMTD.setText(db.MemberTopupNotConfirmed+"");
    }
}
