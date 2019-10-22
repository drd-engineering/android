package com.makaya.drd.service;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by xbudi on 29/09/2016.
 * android.os.AsyncTask<Params, Progress, Result>
 */
public class PostDataModelUrl2<T>  {
    // MY EVENT HANDLER

    private OnSetDataPostedListener onSetDataPostedListener;
    public interface OnSetDataPostedListener {
        public <T> void onDataPosted(T data);
        public <T> void onPostedError(Exception data);
    }

    public void setOnDataPostedListener(OnSetDataPostedListener listener) {
        onSetDataPostedListener = listener;
    }

    public <T> T postUrl(String strUrl, T dataObject, Class<T> typex) throws IOException {

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            @Override
            public Date deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
                    throws JsonParseException {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    return sdf.parse(json.getAsString());
                } catch(ParseException e){
                    return null;
                }
            }
        });
        Gson gson =gsonBuilder.create();;//new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        String jsonObject= gson.toJson(dataObject);

        T data = null;
        HttpURLConnection urlConnection = null;
        try {
            StringBuffer response = null;

            URL url = new URL(strUrl.replace(" ","%20"));

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setUseCaches(false);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Content-Length", "" + Integer.toString(jsonObject.getBytes().length));

            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            writer.write(jsonObject);

            writer.close();
            out.close();

            int responseCode = urlConnection.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String inputLine;
                response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                Type type = typex; //new TypeToken<T>() {}.getType();
                data=gson.fromJson(response.toString(), type);
            }else
            {
                //Toast.makeText(get)
            }
            if (onSetDataPostedListener!=null)
                onSetDataPostedListener.onDataPosted(data);
        } catch (Exception e) {
            Log.d("Exception", e.toString());
            if (onSetDataPostedListener!=null)
                onSetDataPostedListener.onPostedError(e);

        } finally {
            urlConnection.disconnect();
        }
        return data;
    }


    public <T> T postUrl(String strUrl, Class<T> type) throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            @Override
            public Date deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
                    throws JsonParseException {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    return sdf.parse(json.getAsString());
                } catch(ParseException e){
                    return null;
                }
            }
        });
        Gson gson =gsonBuilder.create();

        T data = null;
        HttpURLConnection urlConnection = null;
        try {

            StringBuffer response = null;

            URL url = new URL(strUrl.replace(" ","%20"));

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.connect();
            int responseCode = urlConnection.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String inputLine;
                response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                //Type typex=new TypeToken<ArrayList<type>>() {}.getType();
                if (response.toString().startsWith("[")) {
                    //Type atype=new TypeToken<ArrayList<type>>() {}.getType();
                    data = gson.fromJson("{\"Root\":"+response.toString()+"}",  type);
                }else
                    data=gson.fromJson(response.toString(), type);

                if (onSetDataPostedListener!=null)
                    onSetDataPostedListener.onDataPosted(data);
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


