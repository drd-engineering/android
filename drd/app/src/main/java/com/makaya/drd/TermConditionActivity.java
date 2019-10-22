package com.makaya.drd;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;

import com.makaya.drd.library.PublicFunction;

import java.net.URISyntaxException;

/**
 * Created by xbudi on 05/10/2016.
 */



public class TermConditionActivity extends AppCompatActivity {

    Activity activity;
    WebView webView;
    Button close;
    Button agree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.term_condition);
        activity=this;

        PublicFunction.setHeaderStatus(activity,"Terms & Conditions");
        PublicFunction.setStatusBarColor(activity, "#1f4043");

        webView=(WebView)findViewById(R.id.webView);
        webView.setWebViewClient(new MyBrowser());
        webView.setWebChromeClient(new MyChrome());
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.clearCache(true);

        String url=MainApplication.getUrlApplWeb()+"/doc/tnc/tnc.htm";
        webView.loadUrl(url);

        close=findViewById(R.id.close);
        agree=findViewById(R.id.agree);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent=new Intent(activity, RegistrationActivity.class);
                activity.startActivity(intent);
            }
        });
    }

    private class MyBrowser extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            ProgressBar webProgressBar = (ProgressBar) findViewById(R.id.webProgressBar);
            webProgressBar.setVisibility(View.INVISIBLE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("http")) return false;//open web links as usual
            //try to find browse activity to handle uri
            Uri parsedUri = Uri.parse(url);
            PackageManager packageManager = activity.getPackageManager();
            Intent browseIntent = new Intent(Intent.ACTION_VIEW).setData(parsedUri);
            if (browseIntent.resolveActivity(packageManager) != null) {
                activity.startActivity(browseIntent);
                return true;
            }
            //if not activity found, try to parse intent://
            if (url.startsWith("intent:")) {
                try {
                    Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                    if (intent.resolveActivity(activity.getPackageManager()) != null) {
                        activity.startActivity(intent);
                        return true;
                    }
                    //try to find fallback url
                    String fallbackUrl = intent.getStringExtra("browser_fallback_url");
                    if (fallbackUrl != null) {
                        webView.loadUrl(fallbackUrl);
                        return true;
                    }
                    //invite to install
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW).setData(
                            Uri.parse("market://invoices?id=" + intent.getPackage()));
                    if (marketIntent.resolveActivity(packageManager) != null) {
                        activity.startActivity(marketIntent);
                        return true;
                    }
                } catch (URISyntaxException e) {
                    //not an intent uri
                    new AlertDialog.Builder(activity)
                            .setTitle("Web View Error")
                            .setMessage(e.getMessage())
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    activity.finish();
                                }
                            }).show();
                }
            }

            return true;//do nothing in other cases
        }
    }


    private class MyChrome extends WebChromeClient {

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            //Required functionality here
            return super.onJsAlert(view, url, message, result);
        }



    }
}
