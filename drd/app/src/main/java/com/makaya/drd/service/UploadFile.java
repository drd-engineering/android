package com.makaya.drd.service;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.makaya.drd.domain.UploadResult;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import me.echodev.resizer.Resizer;

/**
 * Created by xbudi on 29/09/2016.
 * android.os.AsyncTask<Params, Progress, Result>
 */
public class UploadFile extends AsyncTask<Object, Void, UploadResult> {
    // MY EVENT HANDLER

    private OnSetDataPostedListener onSetDataPostedListener;
    public interface OnSetDataPostedListener {
        public <T> void onDataPosted(T data);
        public <T> void onPostedError(final Exception data);
        public <T> void onProgress(final long totalSize, final long processSize);
    }

    public void setOnDataPostedListener(OnSetDataPostedListener listener) {
        onSetDataPostedListener = listener;
    }

    private boolean isCanceled=false;
    // /MY EVENT HANDLER


    @Override
    protected UploadResult doInBackground(Object... params) {
        // For storing data from web service
        UploadResult data = null;

        try {
            data = postUrl((String)params[0], (String)params[1]);
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

    public void throwCancel()
    {
        isCanceled=true;
    }

    private UploadResult postUrl(String strUrl, String filePath) throws IOException {

        UploadResult uploadResult=new UploadResult();
        int totalSize;
        String response = "error";
        HttpURLConnection urlConnection = null;
        DataOutputStream outputStream = null;

        String pathToOurFile = filePath;
        String urlServer = strUrl;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024;
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(
                    pathToOurFile));

            //URL url = new URL(urlServer);
            URL url = new URL(urlServer.replace(" ","%20"));
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
            connstr = "Content-Disposition: form-data; name=\"MyImages\";filename=\""
                    + pathToOurFile + "\"" + lineEnd;

            outputStream.writeBytes(connstr);
            outputStream.writeBytes(lineEnd);

            totalSize = bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            // Read file
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            System.out.println("Image length " + bytesAvailable + "");
            try {
                int totalWrite=0;
                while (bytesRead > 0) {
                    try {
                        outputStream.write(buffer, 0, bufferSize);
                        totalWrite+=bufferSize;
                        if (isCanceled && onSetDataPostedListener!=null) {

                            isCanceled=false;
                            uploadResult.idx=-2;
                            uploadResult.message="Process canceled";
                            //onSetDataPostedListener.onDataPosted(uploadResult);
                            fileInputStream.close();
                            outputStream.flush();
                            urlConnection.disconnect();
                            return uploadResult;
                        }

                        if (onSetDataPostedListener!=null)
                            onSetDataPostedListener.onProgress(totalSize, totalWrite);
                    } catch (OutOfMemoryError e) {
                        fileInputStream.close();
                        outputStream.flush();
                        urlConnection.disconnect();
                        e.printStackTrace();
                        response = "outofmemoryerror";
                        uploadResult.idx=-1;
                        uploadResult.message=response;//e.getMessage();
                        return uploadResult; //response
                    }
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }
            } catch (Exception e) {
                fileInputStream.close();
                outputStream.flush();
                urlConnection.disconnect();
                e.printStackTrace();
                response = "error";
                uploadResult.idx=-1;
                uploadResult.message=e.getClass().toString();
                return uploadResult; //response
            }
            outputStream.writeBytes(lineEnd);
            outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            fileInputStream.close();
            outputStream.flush();
            // Responses from the server (code and message)
            int serverResponseCode = urlConnection.getResponseCode();
            String serverResponseMessage = urlConnection.getResponseMessage();
            System.out.println("Server Response Code " + " " + serverResponseCode);
            System.out.println("Server Response Message " + serverResponseMessage);
            if (serverResponseCode==200) {
                /*if (serverResponseCode == 200) {
                    response = "true";
                }else{
                    response = "false";
                }*/

                /*fileInputStream.close();
                outputStream.flush();*/

                //urlConnection.getInputStream();
                //for android InputStream is = urlConnection.getInputStream();
                InputStream is = urlConnection.getInputStream();

                int ch;
                StringBuffer b = new StringBuffer();
                while ((ch = is.read()) != -1) {
                    b.append((char) ch);
                }

                String responseString = b.toString();
                System.out.println("response string is" + responseString); //Here is the actual output

                outputStream.close();
                outputStream = null;

                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                uploadResult = gson.fromJson(responseString.toString(), UploadResult.class);
            }else{
                uploadResult.idx=-1;
                uploadResult.message="Send file response "+serverResponseCode+": "+serverResponseMessage;
            }
        } catch (Exception ex) {
            outputStream.flush();
            urlConnection.disconnect();
            // Exception handling
            response = "error";
            uploadResult.idx=-1;
            uploadResult.message="Send file Exception " + ex.getClass().toString();
            /*if (onSetDataPostedListener!=null)
                onSetDataPostedListener.onPostedError(ex);*/
            System.out.println("Send file Exception " + ex.getMessage());
            ex.printStackTrace();
        }finally {
            urlConnection.disconnect();
        }
        return uploadResult;//response
    }
}


