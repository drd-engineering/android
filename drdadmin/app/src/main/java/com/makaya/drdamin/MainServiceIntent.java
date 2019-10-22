package com.makaya.drdamin;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.makaya.drdamin.domain.Dashboard;
import com.makaya.drdamin.domain.UserAdmin;
import com.makaya.drdamin.service.PostDataModelUrl2;

import java.io.IOException;

import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * Created by xbudi on 25/11/2016.
 */

public class MainServiceIntent extends IntentService {
    Handler mHandler;
    Context context;
    boolean inProcess=false;
    MainApplication global;
    SessionManager session;
    UserAdmin user;

    BroadcastReceiver mReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!intent.getAction().equals("DrdAdmService"))
                return;

            /*boolean isready=intent.getBooleanExtra("READY",false);
            if (isready) {*/
                Intent localIntent = new Intent("DrdAdmActivity");
                //localIntent.putExtra("READY", true);
                LocalBroadcastManager.getInstance(context).sendBroadcast(localIntent);
            //}
        }
    };

    public MainServiceIntent()
    {
        super("DrdAdmMainService");
        mHandler = new Handler();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        context=this;
        global=(MainApplication)getApplicationContext();
        session=new SessionManager(getApplication());
        user=session.getUserLogin();
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter("DrdAdmService"));
        //proses background ada disini
        while(true) {
            try {
                if (inProcess)
                    continue;

                compareDashboard();

                Thread.sleep(1000*5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return Service.START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
    }

    public class DisplayToast implements Runnable {
        private final Context mContext;
        String mText;

        public DisplayToast(Context mContext, String text){
            this.mContext = mContext;
            mText = text;
        }

        public void run(){
            Toast.makeText(mContext, mText, Toast.LENGTH_SHORT).show();
        }
    }


    void generateNotif(Dashboard db, Dashboard lastdb)
    {
        int badgerCount=0;

        String notifMessage="";
        /*badgerCount+=db.UnreadInbox;
        if (db.UnreadInbox!=0) {
            notifMessage=db.UnreadInbox+" messages.";
            showNotification(getString(R.string.app_name)+" notifications", notifMessage,0, DashboardActivity.class);
        }*/

        // member toppup
        badgerCount+=db.MemberTopupConfirmation;
        if (db.MemberTopupConfirmation>lastdb.MemberTopupConfirmation) {
            notifMessage=db.MemberTopupConfirmation+" member topup confirmation\n";
            showNotification(getString(R.string.app_name)+" notifications", notifMessage,5, DashboardActivity.class);
        }
        badgerCount+=db.MemberTopupPending;
        if (db.MemberTopupPending>lastdb.MemberTopupPending) {
            notifMessage=db.MemberTopupPending+" member topup pending\n";
            showNotification(getString(R.string.app_name)+" notifications", notifMessage,6, DashboardActivity.class);
        }
        //badgerCount+=db.MemberTopupConfirmed;
        if (db.MemberTopupConfirmed>lastdb.MemberTopupConfirmed) {
            notifMessage=db.MemberTopupConfirmed+" member topup confirmed\n";
            showNotification(getString(R.string.app_name)+" notifications", notifMessage,7, DashboardActivity.class);
        }
        //badgerCount+=db.MemberTopupNotConfirmed;
        if (db.MemberTopupNotConfirmed>lastdb.MemberTopupNotConfirmed) {
            notifMessage=db.MemberTopupNotConfirmed+" member topup not confirmed\n";
            showNotification(getString(R.string.app_name)+" notifications", notifMessage,8, DashboardActivity.class);
        }


        ShortcutBadger.applyCount(this, badgerCount);
    }

    public void showNotification(String title, String message, int inc, Class<?> cls){

        // define sound URI, the sound to be played when there's a notification
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent intent = new Intent(this, UserLoginActivity.class);
        intent.setAction(cls.getName());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);

        // this is it, we'll build the notification!
        // in the addAction method, if you don't want any icon, just set the first param to 0
        Notification mNotification = new Notification.Builder(this)
                .setTicker(title) //r.getString(R.string.notification_title))
                .setSmallIcon(R.drawable.logodocurate)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pi)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setPriority(Notification.PRIORITY_HIGH)
                .setVibrate(new long[]{1000, 1000})
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(132+inc, mNotification);

    }

    void broadcastNotify()
    {
        //kirim pesan ke activity
        Intent localIntent = new Intent("DrdAdmActivity");
        LocalBroadcastManager.getInstance(context).sendBroadcast(localIntent);
    }

    private void compareDashboard()
    {

        user=session.getUserLogin();
        if (user.Id==0) {
            ShortcutBadger.applyCount(this, 0);
            broadcastNotify();
            inProcess=false;
            return;
        }
        inProcess=true;

        String url=String.format(getResources().getString(R.string.api_dashboard_compare),
                MainApplication.getUrlLinkApi());
        PostDataModelUrl2 posData = new PostDataModelUrl2();

        posData.setOnDataPostedListener(new PostDataModelUrl2.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data) {
                if (data==null) {
                    inProcess=false;
                    return;
                }

                if (!(Boolean)data){
                    fetchAdminDashboard();
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

    private void fetchAdminDashboard()
    {
        String url=String.format(getResources().getString(R.string.api_dashboard_count),
                MainApplication.getUrlLinkApi());
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
            posData.postUrl(url, Dashboard.class);
        }catch (IOException x){
            inProcess=false;
        }
    }

}
