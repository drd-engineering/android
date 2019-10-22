package com.makaya.drd.library;

import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;

import com.makaya.drd.domain.UploadParam;
import com.makaya.drd.domain.UploadResultRoot;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by xbudi on 08/01/2018.
 */

public class UploadImage {
    /*// MY EVENT HANDLER

    private OnSetUploadedListener onSetUploadedListener;
    public interface OnSetUploadedListener {
        public <T> void onUploadSuccess(UploadResultRoot result);
        public <T> void onUploadError();
    }

    public void setOnDataPostedListener(OnSetUploadedListener listener) {
        onSetUploadedListener = listener;
    }

    // /MY EVENT HANDLER*/

    public static final String TAG = "Upload Image Apache";

    public void doImageUpload(final String url, final Bitmap bmp, final Handler handler){
        doImageUpload(url, bmp, handler, 1);
    }

    public void doImageUpload(final String url, final Bitmap bmp, final Handler handler, final int returnCode){

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                Log.i(TAG, "Starting Upload...");
                final ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("image", convertBitmapToString(bmp)));

                Message msg=new Message();

                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(url);
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = httpclient.execute(httppost);
                    String responseStr = EntityUtils.toString(response.getEntity());
                    Log.i(TAG, "doImageUpload Response : " + responseStr);

                    msg.what=returnCode;
                    msg.obj=responseStr;

                    handler.sendMessage(msg);
                } catch (Exception e) {
                    System.out.println("Error in http connection " + e.toString());
                    msg.what=0;
                    handler.sendMessage(msg);
                }
            }
        });
        t.start();

    }


    public void doFilesUpload(final String url, final ArrayList<UploadParam> params, final Handler handler){

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                Log.i(TAG, "Starting Upload...");
                final ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                for(int i=0;i< params.size();i++) {
                    nameValuePairs.add(new BasicNameValuePair("idx"+i, params.get(i).idx+""));
                    nameValuePairs.add(new BasicNameValuePair("fileType"+i, params.get(i).fileType+""));
                    nameValuePairs.add(new BasicNameValuePair("image"+i, convertBitmapToString(params.get(i).bmp)));
                }

                Message msg=new Message();
                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(url);
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = httpclient.execute(httppost);
                    String responseStr = EntityUtils.toString(response.getEntity());
                    Log.i(TAG, "doImageUpload Response : " + responseStr);

                    msg.what=1;
                    msg.obj=PublicFunction.jsonToClass("{\"Root\":"+responseStr+"}", UploadResultRoot.class);
                    handler.sendMessage(msg);
                    /*if (onSetUploadedListener!=null)
                        onSetUploadedListener.onUploadSuccess(PublicFunction2.jsonToClass("{\"Root\":"+responseStr+"}", UploadResultRoot.class));
                    */

                } catch (Exception e) {
                    System.out.println("Error in http connection " + e.toString());
                    msg.what=0;
                    handler.sendMessage(msg);
                    /*if (onSetUploadedListener!=null)
                        onSetUploadedListener.onUploadError();*/
                }
            }
        });
        t.start();

    }

    public String convertBitmapToString(Bitmap bmp){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 90, stream); //compress to which format you want.
        byte[] byte_arr = stream.toByteArray();
        String imageStr = Base64.encodeToString(byte_arr, Base64.DEFAULT);
        return imageStr;
    }


    public void doFileUpload(final String url, final String filePath, final Handler handler, final int returnCode){

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] buffer=null;
                try {
                    FileInputStream fileInputStream = new FileInputStream(filePath);
                    int bytesAvailable = fileInputStream.available();
                    buffer = new byte[bytesAvailable];
                    fileInputStream.read(buffer, 0, bytesAvailable);
                }catch (FileNotFoundException x){

                }catch (IOException x){

                }

                Log.i(TAG, "Starting Upload...");
                final ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("image", Base64.encodeToString(buffer, Base64.DEFAULT)));

                Message msg=new Message();

                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(url);
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = httpclient.execute(httppost);
                    String responseStr = EntityUtils.toString(response.getEntity());
                    Log.i(TAG, "doImageUpload Response : " + responseStr);

                    msg.what=returnCode;
                    msg.obj=responseStr;

                    handler.sendMessage(msg);
                } catch (Exception e) {
                    System.out.println("Error in http connection " + e.toString());
                    msg.what=0;
                    handler.sendMessage(msg);
                }
            }
        });
        t.start();

    }
}
