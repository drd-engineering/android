package com.makaya.drd;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

import com.makaya.drd.domain.MemberLogin;
import com.makaya.xchat.domain.XChatLogin;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xbudi on 20/10/2016.
 */

public class MainApplication extends Application {

    public enum interestPeriod
    {
        MONTH, YEAR;

        public String toString() {
            return name().charAt(0) + name().substring(1).toLowerCase();
        }
        public static final interestPeriod values[] = values();
    }

    public static String getRentPeriod(int period){
        String ret="";
        switch (period){
            case 0: ret=""; break;
            case 1: ret="Jam"; break;
            case 2: ret="Hari"; break;
            case 3: ret="Bulan"; break;
            case 4: ret="Tahun"; break;
        }
        return ret;
    }

    public enum enumFilterItemType
    {
        TEXTBOX, DROPDOWNLIST, CHECKBOX, RADIOBUTTON
    }

    public enum EnumActivityAction
    {
        SUBMIT(1), REJECT(2), REVISI(4), ALTER(8);
        private int value;
        private static Map map = new HashMap<>();

        private EnumActivityAction(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }
    }

    public enum enumDataHit
    {
        NEWS, VIDEO, PODCAST, BANNER, PARTNERPROMO
    }

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context=this;
    }

    /*
        Function variable
    */

    public static Context getApplication()
    {
        return context;//getApplication();
    }

    public static String getDateFormat()
    {
        return "dd/MM/yyyy";
    }

    public static String getUrlApplBaseApi()
    {
        return getApplication().getResources().getString(R.string.urlApplBaseApi);
    }

    public static String getUrlApplApi()
    {
        return getApplication().getResources().getString(R.string.urlApplApi);
    }

    public static String getUrlApplWeb()
    {
        return getApplication().getResources().getString(R.string.urlApplWeb);
    }


    public static int getInboxTimer()
    {
        return getApplication().getResources().getInteger(R.integer.inboxTimer);
    }
    public static int getInboxChatTimer()
    {
        return getApplication().getResources().getInteger(R.integer.inboxChatTimer);
    }

    public static int getPageSize()
    {
        return context.getResources().getInteger(R.integer.pageSize);
    }
    public static int getPaginateLoadingTriggerThreshold()
    {
        return context.getResources().getInteger(R.integer.paginateLoadingTriggerThreshold);
    }

    public static int generateColor(String status)
    {
        int color= Color.BLACK;
        if (status.equals("00"))
            color=Color.GRAY;
        else if (status.equals("01"))
            color=Color.DKGRAY;
        else if (status.equals("02"))
            color=Color.DKGRAY;
        else if (status.equals("03"))
            color=Color.YELLOW;
        else if (status.equals("90"))
            color=Color.BLUE;
        else if (status.equals("98"))
            color=Color.MAGENTA;
        else if (status.equals("99"))
            color=Color.RED;
        else if (status.equals("11"))
            color=Color.DKGRAY;
        else if (status.equals("12"))
            color=Color.parseColor("#689f38");
        else if (status.equals("13"))
            color=Color.BLUE;
        return color;
    }

    public static XChatLogin XLogin(MemberLogin login){
        XChatLogin xchat=new XChatLogin();
        xchat.Id=login.Id;
        xchat.Number=login.Number;
        xchat.Name=login.Name;
        xchat.Phone=login.Phone;
        xchat.Email=login.Email;
        xchat.ImageProfile=login.ImageProfile;
        return xchat;
    }

}
