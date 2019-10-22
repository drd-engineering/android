package com.makaya.drd.library;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.text.Html;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
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
import com.makaya.drd.MainApplication;
import com.makaya.drd.MiniBrowserActivity;
import com.makaya.drd.R;
import com.makaya.drd.SessionManager;
import com.makaya.drd.domain.DatePart;
import com.makaya.drd.domain.FilterData;
import com.makaya.drd.domain.MemberLogin;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by xbudi on 17/03/2017.
 */

public class PublicFunction {


    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html){
        if(html == null){
            // return an empty spannable if the html is null
            return new SpannableString("");
        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // FROM_HTML_MODE_LEGACY is the behaviour that was used for versions below android N
            // we are using this flag to give a consistent behaviour
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(html);
        }
    }

    public static Boolean isActivityRunning(Context context, Class activityClass)
    {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);

        for (ActivityManager.RunningTaskInfo task : tasks) {
            if (activityClass.getCanonicalName().equalsIgnoreCase(task.baseActivity.getClassName()))
                return true;
        }

        return false;
    }

    public static String toJamak(String title, int value){
        return title+(value>1?"s":"");
    }

    public static void setPhotoProfile(Bitmap bitmap, ImageView imageView, int pixel)
    {
        PickImage pickImg=new PickImage();
        Bitmap circularBitmap = pickImg.getRoundedCornerBitmap(bitmap, pixel);

        imageView.setImageBitmap(circularBitmap);
    }

    public static void setPhotoProfile(Bitmap bitmap, ImageView imageView)
    {
        setPhotoProfile(bitmap, imageView,20);
    }

    public static void setPhotoProfile(ImageView imageView)
    {
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        setPhotoProfile(bitmap, imageView);
    }



    public static void showMiniBrowser(Activity activity,String url,boolean showWarning, String title)
    {
        Intent intent = new Intent(activity, MiniBrowserActivity.class);
        intent.putExtra("ShowWarning", showWarning);
        intent.putExtra("URL", url);
        intent.putExtra("Title", title);
        activity.startActivity(intent);

    }

    public static void showDetailBanner(Activity activity, long id, String title)
    {
        if (title==null || title.equals("")) { // url di set ke title
            /*Intent intent = new Intent(activity, BannerDetailActivity.class);
            intent.putExtra("DataId", id);
            activity.startActivity(intent);*/
        }else{
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(title));
            activity.startActivity(browserIntent);
        }
    }

    public interface BackCallbackInterface {

        void onClickBack();
    }

    public static void setHeaderStatus(final Activity activity, String caption, final BackCallbackInterface callback)
    {
        PublicFunction.setStatusBarColor(activity);
        PublicFunction.setBackgroundColorBar(activity,new int[]{R.id.layoutAppBar});
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
        PublicFunction.setStatusBarColor(activity);
        PublicFunction.setBackgroundColorBar(activity,new int[]{R.id.layoutAppBar});
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

    public static String formatToken(String token){
        StringBuilder s;
        s = new StringBuilder(token);

        for(int i = 4; i < s.length(); i += 5){
            s.insert(i, " ");
        }
        return s.toString();
    }

    public static double toDouble(EditText et){
        double num = 0; // Variable name should start with small letter
        try {
            String angka=et.getText().toString().replace(",", "");
            angka=angka.replace(".", "");
            num = Double.parseDouble(angka);
        } catch (NumberFormatException e) {
            // EditText EtPotential does not contain a valid double
            return 0;
        }
        return num;
    }

    public static long toLong(EditText et){
        long num = 0; // Variable name should start with small letter
        try {
            String angka=et.getText().toString().replace(",", "");
            angka=angka.replace(".", "");
            num = Long.parseLong(angka);
        } catch (NumberFormatException e) {
            // EditText EtPotential does not contain a valid double
            return 0;
        }
        return num;
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

    public static void setStatusBarColor(Activity activity)
    {
        SessionManager session;
        MemberLogin user;

        session=new SessionManager(activity);
        user=session.getUserLogin();
        if (user.Id==0)
            return;
        setStatusBarColor(activity, user.Company.BackColorBar);
    }

    public static void setBackgroundColorBar(Activity activity, int[] viewIds)
    {
        SessionManager session;
        MemberLogin user;

        session=new SessionManager(activity);
        user=session.getUserLogin();
        if (user.Id==0)
            return;
        for(int i=0;i<viewIds.length;i++) {
            View bar = activity.findViewById(viewIds[i]);
            if (bar!=null)
                bar.setBackgroundColor(Color.parseColor(user.Company.BackColorBar));
        }
    }
    public static void setBackgroundColorBar(Activity activity, View[] views)
    {
        SessionManager session;
        MemberLogin user;

        session=new SessionManager(activity);
        user=session.getUserLogin();
        if (user.Id==0)
            return;
        for(int i=0;i<views.length;i++) {
            views[i].setBackgroundColor(Color.parseColor(user.Company.BackColorBar));
        }
    }

    public static void setBackgroundColorPage(Activity activity, int[] viewIds)
    {
        SessionManager session;
        MemberLogin user;

        session=new SessionManager(activity);
        user=session.getUserLogin();
        if (user.Id==0)
            return;
        for(int i=0;i<viewIds.length;i++) {
            View bar = activity.findViewById(viewIds[i]);
            if (bar!=null)
                bar.setBackgroundColor(Color.parseColor(user.Company.BackColorPage));
        }
    }

    public static void setBackgroundColorPage(Activity activity, View[] views)
    {
        SessionManager session;
        MemberLogin user;

        session=new SessionManager(activity);
        user=session.getUserLogin();
        if (user.Id==0)
            return;
        for(int i=0;i<views.length;i++) {
            views[i].setBackgroundColor(Color.parseColor(user.Company.BackColorPage));
        }
    }

    public static void setLogo(Activity activity, int imageId)
    {
        SessionManager session;
        MemberLogin user;

        session=new SessionManager(activity);
        user=session.getUserLogin();
        if (user.Id==0)
            return;
        if (user.Company.Image2==null)
            return;

        ImageView logo=activity.findViewById(imageId);

        String path = MainApplication.getUrlApplWeb() + "/Images/appzone/" + user.Company.Image2;
        Picasso.with(activity)
                .load(path)
                //.placeholder(R.drawable.barcode)
                //.error(R.drawable.qrbarcode)
                .into(logo);
    }

    public static void setLogo(Activity activity, ImageView img)
    {
        SessionManager session;
        MemberLogin user;

        session=new SessionManager(activity);
        user=session.getUserLogin();
        if (user.Id==0)
            return;
        if (user.Company.Image2==null)
            return;

        String path = MainApplication.getUrlApplWeb() + "/Images/appzone/" + user.Company.Image2;
        Picasso.with(activity)
                .load(path)
                //.placeholder(R.drawable.barcode)
                //.error(R.drawable.qrbarcode)
                .into(img);
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

    //
    // QR barcode generator
    //
    /*public static Bitmap TextToImageEncode(Activity activity, String Value) throws WriterException {
        int QRcodeWidth = 500 ;

        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        activity.getResources().getColor(R.color.colorQRCodeBlack):activity.getResources().getColor(R.color.colorQRCodeWhite);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }*/
}


