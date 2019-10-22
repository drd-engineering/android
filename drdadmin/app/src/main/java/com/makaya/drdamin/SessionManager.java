package com.makaya.drdamin;

import android.app.Application;
import android.content.SharedPreferences;

import com.makaya.drdamin.domain.Dashboard;
import com.makaya.drdamin.domain.UserAdmin;
import com.makaya.drdamin.service.PostDataModelUrl;
import com.makaya.drdamin.service.PublicFunction;

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
    //Context context;
    Application application;
    MainApplication global;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "DrdAdmin";
    public static String USERADMIN_CLASS="UserAdminClass";
    public static String DASHBOARD="Dashboard";

    // Constructor
    public SessionManager(Application application){

        this.application = application;
        pref = application.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        global=(MainApplication)application;
        //initLoginStatus();
    }

    public void initLoginStatus()
    {
        //global.setUserLogin(new UserAdmin());
        //Long userId=pref.getLong(USERADMIN_ID,0);
        UserAdmin user=getUserLogin();
        if (user.Id!=0)
        {
            String url= String.format(application.getString(R.string.api_useradmin_login),
                    MainApplication.getUrlLinkApi(), user.Email,"_init_login_xbudi_");
            final PostDataModelUrl posData = new PostDataModelUrl();
            posData.execute(url, UserAdmin.class);
            posData.setOnDataPostedListener(new PostDataModelUrl.OnSetDataPostedListener() {
                @Override
                public <T> void onDataPosted(T data) {
                    if (data==null) {
                        if (onSetSessionListener!=null)
                            onSetSessionListener.onDataPosted(null);
                        return;
                    }

                    //global.setUserLogin((UserAdmin)data);
                    LoginUser((UserAdmin)data);

                    if (onSetSessionListener!=null)
                        onSetSessionListener.onDataPosted((UserAdmin)data);
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

    public void LoginUser(UserAdmin content)
    {
        /*global.setUserLogin(content);
        editor.putLong(USERADMIN_CLASS, content.Id);
        // commit changes
        editor.commit();*/
        putGlobalClass(USERADMIN_CLASS, content);
    }

    public void LogoutUser()
    {
        // Clearing all data from Shared Preferences
        //global.setUserLogin(new UserAdmin());
        editor.clear();
        editor.commit();
    }

    public UserAdmin getUserLogin()
    {
        /*String struser=pref.getString(MEMBER_CLASS, "");
        if (struser.equals("")) {
            MemberContentClass member=new MemberContentClass();
            member.DataMember=new MemberClass();
            return member;
        }else
            return PublicFunction.jsonToClass(struser, MemberContentClass.class);*/
        UserAdmin user=getGlobalClass(USERADMIN_CLASS, UserAdmin.class);
        if(user==null)
            user=new UserAdmin();
        return user;
    }

    public void putDashboard(Dashboard dashboard)
    {
        /*String strdashboard= PublicFunction.classToJson(dashboard);
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
            return PublicFunction.jsonToClass(strdashboard, Dashboard.class);*/

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
        String strvar=PublicFunction.classToJson(var);
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
