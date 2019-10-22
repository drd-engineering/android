package com.makaya.drd;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.makaya.drd.domain.MemberLite;
import com.makaya.drd.domain.MemberLogin;
import com.makaya.drd.domain.StampLite;
import com.makaya.drd.library.PublicFunction;

import java.net.URISyntaxException;

/**
 * Created by xbudi on 05/10/2016.
 */



public class DocRevisiBrowserActivity extends AppCompatActivity {

    Activity activity;
    WebView webView;

    RelativeLayout header;
    ImageView pointer;
    ImageView pen;
    ImageView penx;
    ImageView highlighter;
    ImageView highlighterx;
    ImageView text;
    ImageView signature;
    ImageView initial;
    ImageView privatestamp;
    ImageView stamp;

    LinearLayout header2;
    ImageView popuser;
    ImageView popstamp;
    ImageView trash;
    ImageView textedit;

    String selectedData;

    Button btnSend;
    int viewType;

    WebAppInterface webAppInterface;
    SessionManager session;
    MemberLogin user;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doc_revisi_browser);
        activity=this;
        session=new SessionManager(getApplication());
        user=session.getUserLogin();
        handler=new Handler();
        String url=getIntent().getStringExtra("URL");
        String title=getIntent().getStringExtra("Title");
        viewType=getIntent().getIntExtra("ViewType", 1);

        PublicFunction.setHeaderStatus(activity,(title.equals("")?"Mini Browser":title));

        webView=findViewById(R.id.webView);

        // supaya bisa zoom in/out
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setLoadWithOverviewMode(true);

        webView.getSettings().setUseWideViewPort(true);
        webView.setInitialScale(100);
        //
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new MyBrowser());
        webView.setWebChromeClient(new MyChrome());
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.clearCache(true);

        webAppInterface=new WebAppInterface(this);
        webAppInterface.setOnReleasedListener(new WebAppInterface.OnReleasedPostedListener() {
            @Override
            public <T> void onRelease() {
                setActiveButton("pointer");
            }
        });
        webAppInterface.setOnSelectedListener(new WebAppInterface.OnSelectedPostedListener() {
            @Override
            public <T> void onSelected(final String tbtype, final String data) {
                activity.runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        selectedData=data;
                        setVisibleSecondToolbox(tbtype);
                    }
                });
            }
        });
        webAppInterface.setOnPdfRenderListener(new WebAppInterface.OnPdfRenderPostedListener() {
            @Override
            public <T> void onPdfRender(int page) {
                /*if (page==1)
                    webView.loadUrl("javascript:setFromAndroid()");*/
            }
        });
        webAppInterface.setOnSaveListener(new WebAppInterface.OnSavePostedListener() {
            @Override
            public <T> void onSaved() {
                Toast.makeText(activity,"Save annotations successfully",Toast.LENGTH_SHORT).show();
            }
        });
        webView.addJavascriptInterface(webAppInterface, "Android");
        webView.loadUrl(url);

        btnSend=findViewById(R.id.btnSend);
        btnSend.setVisibility(View.GONE);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity,"Wait until the complete dialog is show...",Toast.LENGTH_SHORT).show();
                webView.loadUrl("javascript:saveAnnos("+user.Id+")");
            }
        });

        initButton();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;

        if (requestCode == 2){
            MemberLite usr= (MemberLite)data.getSerializableExtra("MemberLite");
            webView.loadUrl("javascript:setMemberDetail("+usr.Id+",'"+usr.Number+"','"+usr.Name+"','"+usr.ImageProfile+"')");
        }else if (requestCode == 3){
            StampLite stamp= (StampLite)data.getSerializableExtra("StampLite");
            webView.loadUrl("javascript:setStampDetail("+stamp.Id+",'"+stamp.Descr+"','"+stamp.StampFile+"')");
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    private void setVisibleSecondToolbox(String tbtype){
        if (!tbtype.equals("")) {
            textedit.setVisibility(View.GONE);
            popuser.setVisibility(View.GONE);
            popstamp.setVisibility(View.GONE);

            if (tbtype.startsWith("text-tool"))
                textedit.setVisibility(View.VISIBLE);
            else if (tbtype.startsWith("signature") || tbtype.startsWith("initial") || tbtype.startsWith("privatestamp"))
                popuser.setVisibility(View.VISIBLE);
            else if (tbtype.startsWith("stamp"))
                popstamp.setVisibility(View.VISIBLE);

            header2.setVisibility(View.VISIBLE);
        }else
            header2.setVisibility(View.GONE);
    }

    private void initButton() {
        header=findViewById(R.id.header);
        if (viewType==2)
            header.setVisibility(View.GONE);

        header2=findViewById(R.id.header2);
        popuser=findViewById(R.id.popuser);
        popstamp=findViewById(R.id.popstamp);
        trash=findViewById(R.id.trash);
        textedit=findViewById(R.id.textedit);
        header2.setVisibility(View.GONE);

        pointer=findViewById(R.id.pointer);
        pen=findViewById(R.id.pen);
        penx=findViewById(R.id.penx);
        highlighter=findViewById(R.id.highlighter);
        highlighterx=findViewById(R.id.highlighterx);
        text=findViewById(R.id.text);
        signature=findViewById(R.id.signature);
        initial=findViewById(R.id.initial);
        privatestamp=findViewById(R.id.privatestamp);
        stamp=findViewById(R.id.stamp);

        setActiveButton("pointer");
        pointer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doAnnotation("pointer");
            }
        });
        pen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doAnnotation("pen");
            }
        });
        penx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doAnnotation("penx");
            }
        });
        highlighter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doAnnotation("highlighter");
            }
        });
        highlighterx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doAnnotation("highlighterx");
            }
        });
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doAnnotation("text");
            }
        });
        signature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doAnnotation("signature");
            }
        });
        initial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doAnnotation("initial");
            }
        });
        privatestamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doAnnotation("pstamp");
            }
        });
        stamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doAnnotation("stamp");
            }
        });


        popuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupUser();
            }
        });
        popstamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupStamp();
            }
        });
        textedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText();
            }
        });
        trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAnno();
            }
        });
    }

    private void setActiveButton(String tbtype){
        setBackgroundColorButton(pointer, 0);
        setBackgroundColorButton(pen, 0);
        setBackgroundColorButton(penx, 0);
        setBackgroundColorButton(highlighter, 0);
        setBackgroundColorButton(highlighterx, 0);
        setBackgroundColorButton(text, 0);
        setBackgroundColorButton(signature, 0);
        setBackgroundColorButton(initial, 0);
        setBackgroundColorButton(privatestamp, 0);
        setBackgroundColorButton(stamp, 0);

        ImageView button=pointer;
        if (tbtype == "pen") {
            button=pen;
        } else if (tbtype == "penx") {
            button=penx;
        } else if (tbtype == "highlighter") {
            button=highlighter;
        } else if (tbtype == "highlighterx") {
            button=highlighterx;
        } else if (tbtype == "text") {
            button=text;
        } else if (tbtype == "signature") {
            button=signature;
        } else if (tbtype == "initial") {
            button=initial;
        } else if (tbtype == "pstamp") {
            button=privatestamp;
        } else if (tbtype == "stamp") {
            button=stamp;
        }
        setBackgroundColorButton(button, Color.RED);
    }

    private void setBackgroundColorButton(ImageView button, int color) {
        handler.post(new ChangeColor(button, color));
    }

    private void doAnnotation(String tbtype){
        setActiveButton(tbtype);
        webView.loadUrl("javascript:doAnnotation('"+tbtype+"')");
    }

    private void deleteAnno(){
        new android.app.AlertDialog.Builder( activity )
                .setTitle( "Confirmation" )
                .setMessage( "This annotate will be deleted, are you sure?" )
                .setNegativeButton( "No", null )
                .setPositiveButton( "Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        webView.loadUrl("javascript:deleteAnno()");

                    }
                })
                .show();

    }

    private void popupUser(){
        Intent intent=new Intent(activity, RecipientListActivity.class);
        startActivityForResult(intent, 2);
    }
    private void popupStamp(){
        Intent intent=new Intent(activity, StampListActivity.class);
        startActivityForResult(intent, 3);
    }
    private void editText(){
        PopupTextDialog text=new PopupTextDialog(activity);
        text.setOnSubmitListener(new PopupTextDialog.OnSubmitListener() {
            @Override
            public void onSubmit(String data) {
                webView.loadUrl("javascript:setTextDetail('"+data+"')");
            }
        });
        text.show();
        text.setText(selectedData);
    }
    class ChangeColor implements Runnable  {
        ImageView image;
        int color;

        public ChangeColor(ImageView image, int color){
            this.image = image;
            this.color=color;
        }

        public void run(){
            if (color==0)
                image.setBackgroundResource(R.drawable.image_button_inactive);
            else
                image.setBackgroundResource(R.drawable.image_button_active);
        }
    }

    private class MyBrowser extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            ProgressBar webProgressBar = (ProgressBar) findViewById(R.id.webProgressBar);
            webProgressBar.setVisibility(View.GONE);
            if (viewType==1) { // revision, not view
                btnSend.setVisibility(View.VISIBLE);
                //webView.loadUrl("javascript:setFromAndroid()");
            }
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
