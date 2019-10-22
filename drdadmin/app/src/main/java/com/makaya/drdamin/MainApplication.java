package com.makaya.drdamin;


import android.app.Application;
import android.content.Context;
import android.graphics.Color;

/**
 * Created by xbudi on 06/03/2017.
 */

public class MainApplication extends Application {

    public enum enumClientType
    {
        ADMIN(1), PRINCIPAL(2), PRINCIPALMODULE(3), MEMBER(4), PERSONAL(5);
        private final int type;
        enumClientType(int type)
        {
            this.type = type;
        }
        public int getType() {
            return type;
        }
    }
    public enum enumFilterItemType
    {
        TEXTBOX, DROPDOWNLIST, CHECKBOX, RADIOBUTTON
    }


    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context=this;
    }

    public static Context getContext()
    {
        return context;
    }

    public static String getUrlLinkApi()
    {
        return context.getResources().getString(R.string.urlApplApi);
    }
    public static String getUrlLinkWeb()
    {
        return context.getResources().getString(R.string.urlApplWeb);
    }
    public static String getUrlLinkBaseApi()
    {
        return context.getResources().getString(R.string.urlApplBaseApi);
    }

    public static int getMaxProductImage()
    {
        return context.getResources().getInteger(R.integer.MAX_PRODUCT_IMAGE);
    }
    public static int getPageSize()
    {
        return context.getResources().getInteger(R.integer.PAGE_SIZE);
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
            color=Color.YELLOW;
        else if (status.equals("13"))
            color=Color.BLUE;
        return color;
    }
}
