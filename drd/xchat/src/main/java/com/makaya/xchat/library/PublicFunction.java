package com.makaya.xchat.library;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.makaya.xchat.R;
import com.makaya.xchat.domain.DatePart;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by xbudi on 17/03/2017.
 */

public class PublicFunction {

    public static void setPhotoProfile(Bitmap bitmap, ImageView imageView)
    {
        PickImage pickImg=new PickImage();
        Bitmap circularBitmap = pickImg.getRoundedCornerBitmap(bitmap, 100);

        imageView.setImageBitmap(circularBitmap);
    }

    public static void setPhotoProfile(ImageView imageView)
    {
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        setPhotoProfile(bitmap, imageView);
    }

    public static void setNotEditable(EditText et){
        et.setKeyListener(null);
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                InputMethodManager imm = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });
    }
    public interface BackCallbackInterface {

        void onClickBack();
    }

    public static void setHeaderStatus(final Activity activity, String caption, final BackCallbackInterface callback)
    {
        PublicFunction.setStatusBarColor(activity, R.color.colorStatusBar);
        PublicFunction.setBackgroundColorBar(activity,new int[]{R.id.layoutAppBar}, R.color.colorStatusBar);
        TextView title =  activity.findViewById(R.id.titleBar);
        ImageView back=activity.findViewById(R.id.back);
        title.setText(caption);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onClickBack();
            }
        });
    }

    public static void setHeaderStatus(final Activity activity, String caption)
    {
        PublicFunction.setStatusBarColor(activity, R.color.colorStatusBar);
        PublicFunction.setBackgroundColorBar(activity,new int[]{R.id.layoutAppBar}, R.color.colorStatusBar);
        TextView title =  activity.findViewById(R.id.titleBar);
        ImageView back=activity.findViewById(R.id.back);
        title.setText(caption);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
            }
        });
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

    public static void setBackgroundColorBar(Activity activity, int[] viewIds, int idColor)
    {
        int color = activity.getResources().getColor(idColor);
        for(int i=0;i<viewIds.length;i++) {
            View bar = activity.findViewById(viewIds[i]);
            if (bar!=null)
                bar.setBackgroundColor(color);
        }
    }
    public static void setBackgroundColorGradient(View v, String colorFrom, String colorTo)
    {
        //Color.parseColor() method allow us to convert
        // a hexadecimal color string to an integer value (int color)
        int[] colors = {Color.parseColor(colorFrom),Color.parseColor(colorTo)};

        //create a new gradient color
        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM, colors);

        gd.setCornerRadius(0f);
        //apply the button background to newly created drawable gradient
        v.setBackground(gd);
    }

    public static void setBackgroundColorGradient(Activity activity, int id, String colorFrom, String colorTo)
    {
        setBackgroundColorGradient(activity.findViewById(id),  colorFrom, colorTo);
    }

    public static DatePart getTimeInterval(Date dateStart, Date dateEnd)
    {
        DatePart dp=new DatePart();

        try {
            //in milliseconds
            long diff = dateEnd.getTime() - dateStart.getTime();

            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);

            dp.day=diffDays;
            dp.hour=diffHours;
            dp.minute=diffMinutes;
            dp.second=diffSeconds;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dp;
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
        if (date==null){
            return "date null";
        }
        //try {
            SimpleDateFormat df = new SimpleDateFormat(sformat, Locale.getDefault());
        if (df==null){
            return "df null";
        }
            String reportDate = df.format(date);
        if (reportDate==null){
            return "reportDate null";
        }
            return reportDate;
        /*} catch (ParseException e) {
            e.printStackTrace();
        }*/
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

    public static boolean isValidDate(String inDate, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }

    public static void setEnableButton(Button btn, boolean enable, String text){
        btn.setEnabled(enable);
        btn.setText(text);
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
        if (view==null)
            return;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void hideKeyboard(Dialog dialog)
    {
        InputMethodManager imm = (InputMethodManager) dialog.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = dialog.getCurrentFocus();
        if (view==null)
            return;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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

    public static String formatDuration(int second)
    {
        String result;
        if ((second % 3600)<0)
            second=0;

        if (second / 3600==0)
            result=String.format("%02d:%02d", (second % 3600) / 60, (second % 60));
        else
            result=String.format("%02d:%02d:%02d", second / 3600, (second % 3600) / 60, (second % 60));

        return result;

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

}


