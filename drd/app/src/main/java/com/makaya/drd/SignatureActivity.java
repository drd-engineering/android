package com.makaya.drd;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.makaya.drd.domain.MemberLogin;
import com.makaya.drd.library.PublicFunction;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SignatureActivity extends AppCompatActivity {
    SessionManager session;
    MemberLogin user;
    Activity activity;

    File file;
    View view;

    // Creating Separate Directory for saving Generated Images
    String DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/Pictures/drd/";
    String pic_name = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
    String StoredPath = DIRECTORY + pic_name + ".png";

    ///
    SignaturePad signaturePad;
    Button clearButton;
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signature);
        activity = this;
        session=new SessionManager(getApplication());
        user=session.getUserLogin();
        PublicFunction.setBackgroundColorPage(activity,new int[]{R.id.layout});
        PublicFunction.setHeaderStatus(activity,"Signature/Initial Panel");

        // Method to create Directory, if the Directory doesn't exists
        file = new File(DIRECTORY);
        if (!file.exists()) {
            file.mkdir();
        }

        //
        signaturePad = findViewById(R.id.signaturePad);
        saveButton = findViewById(R.id.saveButton);
        clearButton = findViewById(R.id.clearButton);
        //disable both buttons at start
        saveButton.setEnabled(false);
        clearButton.setEnabled(false);

        signaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {

            }

            @Override
            public void onSigned() {
                saveButton.setEnabled(true);
                clearButton.setEnabled(true);
            }

            @Override
            public void onClear() {
                saveButton.setEnabled(false);
                clearButton.setEnabled(false);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //write code for saving the signature here
                Bitmap bitmap=signaturePad.getTransparentSignatureBitmap();
                try {
                    // Output the file
                    FileOutputStream mFileOutStream = new FileOutputStream(StoredPath);

                    // Convert the output file to Image such as .png
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, mFileOutStream);

                    mFileOutStream.flush();
                    mFileOutStream.close();

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("SignatureFile", StoredPath);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                } catch (Exception e) {
                    Log.v("log_tag", e.toString());
                }

                Toast.makeText(activity, "Signature Saved", Toast.LENGTH_SHORT).show();
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signaturePad.clear();
            }
        });
    }

}