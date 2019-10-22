package com.makaya.drd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;

import com.google.zxing.Result;
import com.makaya.drd.library.PublicFunction;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MerchantQrCodeScannerActivity extends BaseScannerActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    Activity activity;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.merchant_qrcode_scanner);
        activity=this;
        setupToolbar();
        /*PublicFunction2.setStatusBarColor(activity, R.color.colorBased);
        TextView title = (TextView) findViewById(R.id.appBarTitle);
        title.setText("Merchant ID Scanner");*/
        PublicFunction.setHeaderStatus(activity,"Merchant ID Scanner");
        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(this);
        contentFrame.addView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        /*Toast.makeText(this, "Contents = " + rawResult.getText() +
                ", Format = " + rawResult.getBarcodeFormat().toString(), Toast.LENGTH_SHORT).show();*/

        Intent returnIntent = new Intent();
        returnIntent.putExtra("code", rawResult.getText());
        setResult(Activity.RESULT_OK,returnIntent);
        finish();

        /*// Note:
        // * Wait 2 seconds to resume the preview.
        // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
        // * I don't know why this is the case but I don't have the time to figure out.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScannerView.resumeCameraPreview(MerchantQrCodeScannerActivity.this);
            }
        }, 2000);*/
    }
}
