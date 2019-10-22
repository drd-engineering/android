package com.makaya.drd;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

import com.makaya.drd.domain.Dashboard;
import com.makaya.drd.domain.MemberLogin;
import com.makaya.drd.library.PublicFunction;
import com.makaya.drd.service.PostDataModelUrl;

/**
 * Created by xbudi on 01/10/2016.
 */
public class SessionManager {

    // MY EVENT HANDLER

    private OnSetSessionListener onSetSessionListener;
    public interface OnSetSessionListener {
        public <T> void onDataPosted(T data);
    }

    public void setOnSessionListener(OnSetSessionListener listener) {
        onSetSessionListener = listener;
    }
    // /MY EVENT HANDLER

    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;
    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "Drd";
    public static String MEMBER_CLASS="Member";
    public static String DASHBOARD="Dashboard";

    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            pref = _context.getSharedPreferences(PREF_NAME, 0 | Context.MODE_MULTI_PROCESS);
        } else {
            pref = _context.getSharedPreferences(PREF_NAME, 0);
        }*/
        editor = pref.edit();

        //initLoginStatus();
    }

    public void initLoginStatus()
    {
        MemberLogin user=getUserLogin();
        if (user.Id!=0)
        {
            String url=String.format(_context.getString(R.string.api_member_login),
                    MainApplication.getUrlApplApi(),user.Email,"_init_login_xbudi_");
            final PostDataModelUrl posData = new PostDataModelUrl();
            posData.execute(url, MemberLogin.class);
            posData.setOnDataPostedListener(new PostDataModelUrl.OnSetDataPostedListener() {
                @Override
                public <T> void onDataPosted(T data) {
                    if (data==null) {
                        if (onSetSessionListener!=null)
                            onSetSessionListener.onDataPosted(null);
                        return;
                    }

                    LoginUser((MemberLogin)data);

                    if (onSetSessionListener!=null)
                        onSetSessionListener.onDataPosted((MemberLogin)data);
                }

                @Override
                public <T> void onPostedError(Exception data) {
                    if (onSetSessionListener!=null)
                        onSetSessionListener.onDataPosted(null);
                }
            });
        }else
        {
            if (onSetSessionListener!=null)
                onSetSessionListener.onDataPosted(null);
        }
    }

    public void LoginUser(MemberLogin content)
    {
        putGlobalClass(MEMBER_CLASS, content);
    }

    public void LogoutUser()
    {
        // Clearing all data from Shared Preferences
        //MainApplication.setUserLogin(new Member());
        editor.clear();
        editor.commit();
    }

    public MemberLogin getUserLogin()
    {
        MemberLogin user=getGlobalClass(MEMBER_CLASS, MemberLogin.class);
        if(user==null)
            user=new MemberLogin();
        return user;
    }

    public void putDashboard(Dashboard dashboard)
    {
       /* String strdashboard=PublicFunction2.classToJson(dashboard);
        editor.putString(DASHBOARD, strdashboard);
        // commit changes
        editor.commit();*/
        putGlobalClass(DASHBOARD, dashboard);
    }
    public Dashboard getDashboard()
    {
        /*String strdashboard=pref.getString(DASHBOARD, "");
        if (strdashboard.equals(""))
            return new Dashboard();
        else
            return PublicFunction2.jsonToClass(strdashboard, Dashboard.class);*/
        Dashboard db=getGlobalClass(DASHBOARD, Dashboard.class);
        if (db==null)
            db=new Dashboard();
        return db;
    }

    /*
        PUBLIC FUNCTION
     */

    public <T> void putGlobalClass(String className, T var)
    {
        String strvar= PublicFunction.classToJson(var);
        editor.putString(className, strvar);
        // commit changes
        editor.commit();
    }
    public <T> T getGlobalClass(String className, Class<T> cls)
    {
        String strvar=pref.getString(className, "");
        if (strvar.equals(""))
            return null;
        else
            return PublicFunction.jsonToClass(strvar, cls);
    }


    public void putInteger(String key, int value)
    {
        editor.putInt(key, value);
        // commit changes
        editor.commit();
    }
    public int getInteger(String key)
    {
        return pref.getInt(key, 0);
    }

    public void putBoolean(String key, boolean value)
    {
        editor.putBoolean(key, value);
        // commit changes
        editor.commit();
    }
    public boolean getBoolean(String key)
    {
        return pref.getBoolean(key, false);
    }

}
