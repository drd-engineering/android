package com.makaya.drdamin.service;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.makaya.drdamin.MainApplication;
import com.makaya.drdamin.R;
import com.makaya.drdamin.domain.FilterData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xbudi on 17/03/2017.
 */

public class PublicFunction {

    public static void setNotEditable(EditText et){
        et.setKeyListener(null);
    }

    public static String formatToken(String token){
        StringBuilder s;
        s = new StringBuilder(token);

        for(int i = 4; i < s.length(); i += 5){
            s.insert(i, " ");
        }
        return s.toString();
    }

    public static void setStatusBarColor(Activity activity, int rIdColor)
    {
        if (android.os.Build.VERSION.SDK_INT >= 21){
            float[] hsv = new float[3];
            int color = activity.getResources().getColor(rIdColor);
            Color.colorToHSV(color, hsv);
            hsv[2] *= 0.8f; // value component
            color = Color.HSVToColor(hsv);
            Window window = activity.getWindow();
            window.setStatusBarColor(color);
        }
    }

    public static void setStatusBarColor(Activity activity, String strColor)
    {
        if (android.os.Build.VERSION.SDK_INT >= 21){
            float[] hsv = new float[3];
            int color = Color.parseColor(strColor);
            Color.colorToHSV(color, hsv);
            hsv[2] *= 0.8f; // value component
            color = Color.HSVToColor(hsv);
            Window window = activity.getWindow();
            window.setStatusBarColor(color);
        }
    }

    public static void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setEnabled(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
        editText.setBackgroundColor(Color.TRANSPARENT);
    }

    public static void setEnableButton(Button btn, boolean enable, String text){
        btn.setEnabled(enable);
        btn.setText(text);
    }

    public static String getDateFormat()
    {
        return MainApplication.getContext().getResources().getString(R.string.date_format);
    }
    public static String getTimeFormat()
    {
        return MainApplication.getContext().getResources().getString(R.string.time_format);
    }

    public static Date getToday()
    {
        Calendar c = Calendar.getInstance();

        return c.getTime();
    }

    public static Calendar toCalendar(String sformat, String sdate)
    {
        try {
            SimpleDateFormat df = new SimpleDateFormat(sformat, Locale.getDefault());
            Date date=df.parse(sdate);
            Calendar cal=Calendar.getInstance();
            cal.setTime(date);
            return cal;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date toDate(String sformat, String sdate)
    {
        try {
            SimpleDateFormat df = new SimpleDateFormat(sformat, Locale.getDefault());
            Date date=df.parse(sdate);

            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String dateToString(String sformat, Date date)
    {
        //try {
        SimpleDateFormat df = new SimpleDateFormat(sformat, Locale.getDefault());
        String reportDate = df.format(date);
        return reportDate;
        //} catch (ParseException e) {
        //    e.printStackTrace();
        //}
        //return null;
    }

    public static Date dateAdd(String sdate, String sformat, int field, int amount)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(sformat);
        Calendar c = Calendar.getInstance();

        try {
            c.setTime(sdf.parse(sdate));
        }catch(ParseException x){}

        c.add(field, amount);

        sdf = new SimpleDateFormat(sformat);
        Date resultdate = new Date(c.getTimeInMillis());
        String dateInString = sdf.format(resultdate);

        return toDate(sformat, dateInString);
    }

    public static Date dateAdd(Date date, int field, int amount)
    {
        //SimpleDateFormat sdf = new SimpleDateFormat(sformat);
        Calendar c = Calendar.getInstance();

        //try {
        c.setTime(date);
        //}catch(ParseException x){}

        c.add(field, amount);

        //sdf = new SimpleDateFormat(sformat);
        Date resultdate = new Date(c.getTimeInMillis());
        //String dateInString = sdf.format(resultdate);

        return resultdate;//toDate(sformat, dateInString);
    }

    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static void hideKeyboard(Activity activity)
    {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void hideKeyboard(Dialog dialog)
    {
        InputMethodManager imm = (InputMethodManager) dialog.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = dialog.getCurrentFocus();
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String filterDataParse(ArrayList<FilterData> datas)
    {
        String ret="";
        String[] columns;
        String[] columnsTo;
        for(int i=0;i<datas.size();i++){
            FilterData fd=datas.get(i);
            if (fd.ItemType== MainApplication.enumFilterItemType.TEXTBOX) {
                if (fd.CaptionTo != null) {
                    if (!fd.Value.equals("") && !fd.ValueTo.equals("")) {
                        columns = fd.ColumnName.split(",");
                        columnsTo = fd.ColumnNameTo.split(",");
                        ret += "(";
                        for (int c = 0; c < columns.length; c++) {
                            if (fd.InputType == InputType.TYPE_CLASS_TEXT)
                                ret += "(" + columns[c] + " >= \"" + fd.Value + "\" AND " + columnsTo[c] + "<=\"" + fd.ValueTo + "\")";
                            else
                                ret += "(" + columns[c] + " >= " + fd.Value + " AND " + columnsTo[c] + "<=" + fd.ValueTo + ")";
                            ret += " OR ";
                        }
                        ret += ") AND ";
                        ret = ret.replace("OR )", ")");
                    } else if (!fd.Value.equals("") && fd.ValueTo.equals("")) {
                        columns = fd.ColumnName.split(",");
                        ret += "(";
                        for (int c = 0; c < columns.length; c++) {
                            if (fd.InputType == InputType.TYPE_CLASS_TEXT)
                                ret += columns[c] + " >= \"" + fd.Value + "\"";
                            else
                                ret += columns[c] + " >= " + fd.Value;
                            ret += " OR ";
                        }
                        ret += ") AND ";
                        ret = ret.replace("OR )", ")");
                    } else if (fd.Value.equals("") && !fd.ValueTo.equals("")) {
                        columnsTo = fd.ColumnNameTo.split(",");
                        ret += "(";
                        for (int c = 0; c < columnsTo.length; c++) {
                            if (fd.InputType == InputType.TYPE_CLASS_TEXT)
                                ret += columnsTo[c] + " <= \"" + fd.ValueTo + "\"";
                            else
                                ret += columnsTo[c] + " <= " + fd.ValueTo;
                            ret += " OR ";
                        }
                        ret += ") AND ";
                        ret = ret.replace("OR )", ")");
                    }
                } else {
                    if (!fd.Value.equals("")) {
                        columns = fd.ColumnName.split(",");
                        ret += "(";
                        for (int c = 0; c < columns.length; c++) {
                            if (fd.InputType == InputType.TYPE_CLASS_TEXT)
                                ret += columns[c] + ".Contains(\"" + fd.Value + "\")";
                            else
                                ret += columns[c] + " =" + fd.Value;
                            ret += " OR ";
                        }
                        ret += ") AND ";
                        ret = ret.replace("OR )", ")");
                    }
                }
            }else if (fd.ItemType== MainApplication.enumFilterItemType.CHECKBOX){
                if (fd.CheckBox) {
                    ret += "(";
                    if (fd.ItemDataType == Boolean.class) {
                        ret += fd.ColumnName + "=" + fd.CheckBox;
                    } else {
                        ret += fd.ColumnName + "=" + fd.ValueId;
                    }
                    ret += ") AND ";
                }
            }else if (fd.ItemType== MainApplication.enumFilterItemType.RADIOBUTTON){
                if (fd.ValueId>=0) {
                    ret += "(";
                    ret += fd.ColumnName + "=" + fd.ValueId;
                    ret += ") AND ";
                }
            }else if (fd.ItemType== MainApplication.enumFilterItemType.DROPDOWNLIST){
                if (fd.ValueId>=0) {
                    ret += "(";
                    ret += fd.ColumnName + "=" + fd.ValueId;
                    ret += ") AND ";
                }
            }
        }
        if (!ret.equals(""))
            ret+=".";
        ret=ret.replace("AND .","");
        return ret;
    }

    public static double toKM(long dist)
    {
        if  (dist<100)
            return dist/1000;

        long frac = dist % 1000;
        dist = dist / 1000 ;

        return Double.parseDouble(new DecimalFormat("##.##").format(Double.parseDouble(dist + "." + frac)));
    }

    public static String toKMString(long dist)
    {
        if  (dist<100)
            return dist+" m";

        long frac = dist % 1000;
        dist = dist / 1000 ;

        return new DecimalFormat("##.##").format(Double.parseDouble(dist + "." + frac))+" km";
    }

    public static String toDuration(long dur)
    {
        long mins=dur/60;
        long hours=dur/(30*60);
        long days=dur/(12*30*60);

        String sdur="";
        if (days>0)
            sdur=days+" days ";
        if (hours>0)
            sdur=hours+" hours ";
        if (mins>0)
            sdur=mins+" mins ";

        if (sdur=="")
            sdur="0 min";

        return sdur;
    }

    public static <T> String classToJson(T dataObject)
    {
        GsonBuilder gsonBuilder = new GsonBuilder();
	gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        gsonBuilder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            @Override
            public Date deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
                    throws JsonParseException {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    return sdf.parse(json.getAsString());
                } catch(ParseException e){
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");
                        return sdf.parse(json.getAsString());
                    } catch(ParseException ee){
                        return null;
                    }
                }
            }
        });
        Gson gson =gsonBuilder.create();

        return gson.toJson(dataObject);
    }

    public static <T> T jsonToClass(String json, Class<T> type)
    {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            @Override
            public Date deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
                    throws JsonParseException {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    return sdf.parse(json.getAsString());
                } catch(ParseException e){
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");
                        return sdf.parse(json.getAsString());
                    } catch(ParseException ee){
                        return null;
                    }
                }
            }
        });
        Gson gson =gsonBuilder.create();

        return gson.fromJson(json, type);
    }

    public static void requestPermission(Activity activity, String permission, int permissionCode)
    {
        if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(activity, new String[]{permission}, permissionCode);
        }
    }

    public static void requestPermissions(Activity activity, String[] permissions, int permissionCode)
    {

        ArrayList<String> notallows=new ArrayList<>();
        for(String permission: permissions)
        {
            if (ContextCompat.checkSelfPermission(activity,permission) != PackageManager.PERMISSION_GRANTED)
                notallows.add(permission);
        }
        if (notallows.size()>0)
        {
            ActivityCompat.requestPermissions(activity, notallows.toArray(new String[notallows.size()]), permissionCode);
        }
    }

    public static boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
