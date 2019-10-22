package com.makaya.drd;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.widget.Toast;

import com.makaya.drd.domain.Dashboard;
import com.makaya.drd.domain.MemberLogin;
import com.makaya.drd.library.PublicFunction;
import com.makaya.drd.service.PostDataModelUrl2;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Timer;
import java.util.TimerTask;

import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * Created by xbudi on 25/11/2016.
 */

public class MainServiceIntent extends IntentService {
    Handler mHandler;
    Context context;
    boolean inProcess=false;
    SessionManager session;
    MemberLogin user;


    BroadcastReceiver mReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!intent.getAction().equals("DrdService"))
                return;

            boolean isready=intent.getBooleanExtra("READY",false);
            if (isready) {
                Intent localIntent = new Intent("DrdActivity");
                localIntent.putExtra("READY", true);
                LocalBroadcastManager.getInstance(context).sendBroadcast(localIntent);
            }
        }
    };

    public MainServiceIntent()
    {
        super("DrdMainService");
        mHandler = new Handler();
        //setIntentRedelivery(true);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        context=this;
        session=new SessionManager(getApplication());
        user=session.getUserLogin();
        //LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter("DrdService"));

        //proses background ada disini
        /*while(true) {
            try {
                if (inProcess)
                    continue;

                compareDashboard();

                Thread.sleep(1000*10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/

        startTimerDashboard();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //mHandler.post(new DisplayToast(this, "Service destroyed"));
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
        /*Intent intent = new Intent("com.android.ServiceStopped");
        sendBroadcast(intent);*/
    }

    private Timer timer;
    private TimerTask timerTask;
    public void startTimerDashboard() {
        timer = new Timer();
        timerTask = new TimerTask() {
            public void run() {
                if (!inProcess) {
                    compareDashboard();
                }
            }
        };
        timer.schedule(timerTask, 1000, 1000*3); //
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

    void generateNotif(Dashboard db, Dashboard lastdb) {
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
            notifMessage="Deposit balance DRD Point "+NumberFormat.getInstance().format(db.DepositBalance)+"\n\n";
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


    void broadcastNotify()
    {
        //kirim pesan ke activity
        Intent localIntent = new Intent("DrdActivity");
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

    private void fetchDashboard()
    {

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
