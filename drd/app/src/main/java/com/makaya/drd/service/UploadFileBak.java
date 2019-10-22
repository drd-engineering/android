package com.makaya.drd.service;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by xbudi on 29/09/2016.
 * android.os.AsyncTask<Params, Progress, Result>
 */
public class UploadFileBak<UploadResult> extends AsyncTask<Object, Void, UploadResult> {
    // MY EVENT HANDLER

    private OnSetDataPostedListener onSetDataPostedListener;
    public interface OnSetDataPostedListener {
        public <T> void onDataPosted(T data);
        public <T> void onPostedError(Exception data);
    }

    public void setOnDataPostedListener(OnSetDataPostedListener listener) {
        onSetDataPostedListener = listener;
    }

    // /MY EVENT HANDLER


    @Override
    protected UploadResult doInBackground(Object... params) {

        // For storing data from web service
        UploadResult data = null;

        try {
            data = postUrl(params[0].toString(), (String)params[1]);
                //Log.d("Background Task data", data.toString());
        } catch (Exception e) {
            if (onSetDataPostedListener!=null)
                onSetDataPostedListener.onPostedError(e);
            Log.d("Background Task", e.toString());
        }
        return data;
    }

    @Override
    protected void onPostExecute(UploadResult result) {
        super.onPostExecute(result);
        if (onSetDataPostedListener!=null)
            onSetDataPostedListener.onDataPosted(result);
    }

    private UploadResult postUrl(String strUrl, String filePath) throws IOException {
        DataOutputStream outputStream = null;
        String boundary = "*****";
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        UploadResult data = null;
        HttpURLConnection urlConnection = null;
        try {
            StringBuffer response = null;

            URL url = new URL(strUrl.replace(" ","%20"));

            FileInputStream fileInputStream = new FileInputStream(filePath);
            // create a buffer of  maximum size
            int bytesAvailable = fileInputStream.available();

            /*// Creating an http urlConnection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setUseCaches(false);
            urlConnection.setRequestMethod("POST");
            //urlConnection.setRequestProperty("Accept", "application/pdf");
            //urlConnection.setRequestProperty("Content-Type", "application/pdf");
            urlConnection.setRequestProperty("Connection", "Keep-Alive");
            urlConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            //urlConnection.setRequestProperty("Content-Length", "" + Integer.toString(bytesAvailable));
            urlConnection.setRequestProperty("MyImages", filePath);*/


            urlConnection = (HttpURLConnection) url.openConnection();

            // Allow Inputs & Outputs
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setUseCaches(false);
            urlConnection.setChunkedStreamingMode(1024);
            // Enable POST method
            urlConnection.setRequestMethod("POST");

            urlConnection.setRequestProperty("Connection", "Keep-Alive");
            urlConnection.setRequestProperty("Content-Type",
                    "multipart/form-data; boundary=" + boundary);

            outputStream = new DataOutputStream(urlConnection.getOutputStream());
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);

            String token = "anyvalye";
            outputStream.writeBytes("Content-Disposition: form-data; name=\"Token\"" + lineEnd);
            outputStream.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
            outputStream.writeBytes("Content-Length: " + token.length() + lineEnd);
            outputStream.writeBytes(lineEnd);
            outputStream.writeBytes(token + lineEnd);
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);

            String taskId = "anyvalue";
            outputStream.writeBytes("Content-Disposition: form-data; name=\"TaskID\"" + lineEnd);
            outputStream.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
            outputStream.writeBytes("Content-Length: " + taskId.length() + lineEnd);
            outputStream.writeBytes(lineEnd);
            outputStream.writeBytes(taskId + lineEnd);
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);

            String connstr = null;
            connstr = "Content-Disposition: form-data; name=\"UploadFile\";filename=\""
                    + filePath + "\"" + lineEnd;

            outputStream.writeBytes(connstr);
            outputStream.writeBytes(lineEnd);


            int maxBufferSize = 1 * 1024 * 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);
            byte[] buffer = new byte[bufferSize];
            int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            DataOutputStream  dos = new DataOutputStream(urlConnection.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"MyImages\";filename=\""
                    + filePath + "\"" + lineEnd);

            dos.writeBytes(lineEnd);

            while (bytesRead > 0) {

                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            }
            fileInputStream.close();
            dos.flush();
            dos.close();


            /*OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            writer.write(jsonObject);

            writer.close();
            out.close();*/

            int responseCode = urlConnection.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String inputLine;
                response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                /*if (response.toString().startsWith("[")) {
                    //Type atype=new TypeToken<ArrayList<type>>() {}.getType();
                    data = gson.fromJson("{\"Root\":"+response.toString()+"}",  type);
                }else
                    data=gson.fromJson(response.toString(), type);*/
            }else
            {
                //Toast.makeText(get)
            }

        } catch (Exception e) {
            Log.d("Exception", e.toString());
            if (onSetDataPostedListener!=null)
                onSetDataPostedListener.onPostedError(e);

        } finally {
            urlConnection.disconnect();
        }
        return data;
    }
}


