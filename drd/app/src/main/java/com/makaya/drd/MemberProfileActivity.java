package com.makaya.drd;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.makaya.drd.domain.MemberLogin;
import com.makaya.drd.domain.UploadResult;
import com.makaya.drd.library.FilePath;
import com.makaya.drd.library.PickImage;
import com.makaya.drd.library.PublicFunction;
import com.makaya.drd.service.MemberService;
import com.makaya.drd.service.UploadFile;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.NumberFormat;

import me.echodev.resizer.Resizer;

/**
 * Created by xbudi on 28/07/2017.
 */

public class MemberProfileActivity extends AppCompatActivity {

    SessionManager session;
    MemberLogin user;
    Activity activity;

    UploadFile upload;

    TextView number;
    TextView name;
    TextView phone;
    TextView email;
    TextView ktpNo;
    ImageView ktp;
    ImageView ktpfoto;
    ImageView ktpupload;
    ImageView ktp2;
    ImageView ktp2foto;
    ImageView ktp2upload;

    ImageView picture;
    ImageView signature;
    ImageView signupload;
    ImageView signpad;
    ImageView initial;
    ImageView iniupload;
    ImageView inipad;
    ImageView stamp;
    ImageView stampupload;
    Button changePwd;
    PopupProgressUpload progress;
    String focusFileUpload;
    int selectImageCode;
    boolean isUpdateFotoProfile=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_profile);
        activity = this;
        session=new SessionManager(getApplication());
        user=session.getUserLogin();
        progress=new PopupProgressUpload(activity);
        progress.setOnListener(new PopupProgressUpload.OnSetListener() {
            @Override
            public <T> void onProcessCanceled() {
                upload.throwCancel();
            }
        });
        PublicFunction.setBackgroundColorPage(activity,new int[]{R.id.layout});
        PublicFunction.setHeaderStatus(activity,"My Profile",onBackButton);
        initObject();
        bindObject();
    }

    PublicFunction.BackCallbackInterface onBackButton=new PublicFunction.BackCallbackInterface() {
        @Override
        public void onClickBack() {
            onBackPressed();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode >= 0 && requestCode<=5) {
            if(data == null){
                Toast.makeText(this,"No data present",Toast.LENGTH_SHORT).show();
                return;
            }
            Uri selectedFileUri = data.getData();
            String selectedFilePath = FilePath.getPath(this,selectedFileUri);

            if(selectedFilePath != null && !selectedFilePath.equals("")){
                //int posX=selectedFilePath.lastIndexOf('/');
                //String filename=selectedFilePath.substring(posX+1);
                focusFileUpload=getFileName(selectedFilePath);//filename;
                uploadFile(selectedFilePath);

            }else{
                Toast.makeText(this,"Cannot upload file to server",Toast.LENGTH_SHORT).show();
            }
            //user=session.getUserLogin();
        }else if (requestCode >= 6 && requestCode<=7){
            /*if(data == null){
                Toast.makeText(this,"No image present",Toast.LENGTH_SHORT).show();
                return;
            }*/
            /*Bitmap bitmap = (Bitmap)data.getExtras().get("data");
            if (requestCode==6)
                ktp.setImageBitmap(bitmap);
            else
                ktp2.setImageBitmap(bitmap);*/

            /*Uri selectedFileUri = data.getData();
            String selectedFilePath = FilePath.getPath(this,selectedFileUri);
            focusFileUpload=getFileName(selectedFilePath);
            uploadFile(selectedFilePath);*/



            File   storageDir    = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File   image         = new File(storageDir.getAbsolutePath() + "/my_picture.jpg");
            String imagePath = image.getAbsolutePath();
            focusFileUpload=getFileName(imagePath);
            uploadFile(imagePath);
        }else if (requestCode >= 10 && requestCode<=11){
            String path=data.getStringExtra("SignatureFile");
            focusFileUpload="Signature/Initial";
            uploadFile(path);
        }
    }



    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (!ktpNo.getText().toString().equals(user.KtpNo))
            updateKtpNo(ktpNo.getText().toString());
        else
            closePage();

    }

    private String getFileName(String fullPath){
        int posX=fullPath.lastIndexOf('/');
        String filename=fullPath.substring(posX+1);

        return filename;
    }

    private void initObject()
    {
        picture=findViewById(R.id.picture);
        number=findViewById(R.id.number);
        name=findViewById(R.id.name);
        phone=findViewById(R.id.phone);
        email=findViewById(R.id.email);
        ktpNo=findViewById(R.id.ktpNo);
        changePwd=findViewById(R.id.changePwd);

        ktp=findViewById(R.id.ktp);
        ktpfoto=findViewById(R.id.ktpfoto);
        ktpupload=findViewById(R.id.ktpupload);
        ktp2=findViewById(R.id.ktp2);
        ktp2foto=findViewById(R.id.ktp2foto);
        ktp2upload=findViewById(R.id.ktp2upload);

        signature=findViewById(R.id.signature);
        signupload=findViewById(R.id.signupload);
        signpad=findViewById(R.id.signpad);
        initial=findViewById(R.id.initial);
        iniupload=findViewById(R.id.iniupload);
        inipad=findViewById(R.id.inipad);
        stamp=findViewById(R.id.stamp);
        stampupload=findViewById(R.id.stampupload);

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImageCode=0;
                showFileChooser(0);
            }
        });
        ktpupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImageCode=4;
                showFileChooser(4);
            }
        });
        ktpfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                selectImageCode=4;
                showCamera(6);
            }
        });
        ktp2upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImageCode=5;
                showFileChooser(5);
            }
        });
        ktp2foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                selectImageCode=5;
                showCamera(7);
            }
        });
        signpad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImageCode=1;
                Intent intent=new Intent(activity, SignatureActivity.class);
                startActivityForResult(intent, 10);
            }
        });
        signupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImageCode=1;
                showFileChooser(1);
            }
        });
        inipad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImageCode=2;
                Intent intent=new Intent(activity, SignatureActivity.class);
                startActivityForResult(intent, 11);
            }
        });
        iniupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImageCode=2;
                showFileChooser(2);
            }
        });
        stampupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImageCode=3;
                showFileChooser(3);
            }
        });
        changePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MemberProfileActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

    }

    private void showFileChooser(int reqCode) {
        Intent intent = new Intent();
        //sets the select file to all types of files
        if (reqCode==0 || reqCode==4 || reqCode==5)
            intent.setType("image/*");
        else
            intent.setType("image/png");
        //allows to select data and return it
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //starts new activity to select file and return data
        startActivityForResult(Intent.createChooser(intent,"Choose File..."),reqCode);
    }

    private void showCamera(int reqCode) {
        PickImage pick=new PickImage(activity);
        pick.takePicture(reqCode);//.FromCameraToImage(reqCode);
    }

    private void uploadFile(String filePath) {
        progress.show();
        progress.setFilename(focusFileUpload);
        progress.setTitle("Uploading...");

        String newFilePath=filePath;
        // resize file image
        if (selectImageCode==0 || selectImageCode==4 || selectImageCode==5) {
            String fname=getFileName(filePath);
            String outpurPath = filePath.replace(fname, "");
            File fsourceImg = new File(filePath);
            try {
                File resizedImage = new Resizer(activity)
                        .setTargetLength(1080)
                        .setQuality(80)
                        .setOutputFormat("JPEG")
                        .setOutputFilename("resized_image")
                        //.setOutputDirPath(outpurPath)
                        .setSourceImage(fsourceImg)
                        .getResizedFile();

                newFilePath=resizedImage.getAbsolutePath();
            } catch (Exception x) {
            }
        }

        upload=new UploadFile();
        upload.execute(MainApplication.getUrlApplWeb()+"/updownfile/upload?idx=0&fileType=1",newFilePath);
        upload.setOnDataPostedListener(new UploadFile.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(final T data) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress.dismiss();
                        UploadResult ur=(UploadResult)data;
                        if (ur.idx==-1) {
                            new AlertDialog.Builder(activity)
                                    .setTitle("Error")
                                    .setMessage(ur.message)
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //finish();
                                        }
                                    }).show();
                        }if (ur.idx==-2) {
                            upload.cancel(true);
                            Toast.makeText(activity, "Upload file canceled", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(activity, "Upload file successfully", Toast.LENGTH_SHORT).show();
                            if (selectImageCode==0) {
                                user.ImageProfile = ur.filename;
                                isUpdateFotoProfile=true;
                            }else if (selectImageCode==1)
                                user.ImageSignature=ur.filename;
                            else if (selectImageCode==2)
                                user.ImageInitials=ur.filename;
                            else if (selectImageCode==3)
                                user.ImageStamp=ur.filename;
                            else if (selectImageCode==4)// || selectImageCode==6)
                                user.ImageKtp1=ur.filename;
                            else if (selectImageCode==5)// || selectImageCode==7)
                                user.ImageKtp2=ur.filename;

                            updateImage(ur.filename, selectImageCode);
                            session.LoginUser(user);
                        }
                    }
                });
            }

            @Override
            public <T> void onPostedError(final Exception data) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress.dismiss();
                        new AlertDialog.Builder(activity)
                                .setTitle("Error")
                                .setMessage(data.getMessage())
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //finish();
                                    }
                                }).show();
                    }
                });
            }

            @Override
            public <T> void onProgress(final long totalSize, final long processSize) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress.setProcess(
                                NumberFormat.getInstance().format(processSize)+" to "+
                                        NumberFormat.getInstance().format(totalSize));
                    }
                });
            }
        });
    }

    private void updateImage(String filename, int imageType){
        MemberService msvr=new MemberService(activity);
        msvr.setOnDataPostedListener(new MemberService.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data, boolean isLoadedAllItems) {
                if (data!=null){
                    if (selectImageCode==0) {
                        bindImageRounded(picture, user.ImageProfile);
                    }else if (selectImageCode==1)
                        bindImage(signature, user.ImageSignature);
                    else if (selectImageCode==2)
                        bindImage(initial, user.ImageInitials);
                    else if (selectImageCode==3)
                        bindImage(stamp, user.ImageStamp);
                    else if (selectImageCode==4)// || selectImageCode==6)
                        bindImage(ktp, user.ImageKtp1);
                    else if (selectImageCode==5)// || selectImageCode==7)
                        bindImage(ktp2, user.ImageKtp2);
                }
            }

            @Override
            public <T> void onDataError() {
            }
        });
        msvr.updateImage(user.Id, filename, imageType);
    }

    private void closePage()
    {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("UpdateFotoProfile", isUpdateFotoProfile);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    private void updateKtpNo(String ktpNo){
        MemberService msvr=new MemberService(activity);
        user.KtpNo=ktpNo;
        session.LoginUser(user);
        msvr.setOnDataPostedListener(new MemberService.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data, boolean isLoadedAllItems) {
                closePage();
            }

            @Override
            public <T> void onDataError() {
                Toast.makeText(activity,"Save kto number problem",Toast.LENGTH_SHORT).show();
                closePage();
            }
        });
        msvr.updateKtpNo(user.Id, ktpNo);
    }

    private void bindObject()
    {
        number.setText(user.Number);
        name.setText(user.Name);
        phone.setText(user.Phone);
        email.setText(user.Email);
        ktpNo.setText(user.KtpNo);

        bindImageRounded(picture, user.ImageProfile);
        bindImage(signature, user.ImageSignature);
        bindImage(initial, user.ImageInitials);
        bindImage(stamp, user.ImageStamp);
        bindImage(ktp, user.ImageKtp1);
        bindImage(ktp2, user.ImageKtp2);
    }

    private void bindImage(ImageView img,  String filename){
        String path = MainApplication.getUrlApplWeb() + "/Images/member/" + filename;
        Picasso.with(activity)
                .load(path)
                /*.placeholder(R.drawable.pic_male)
                .error(R.drawable.pic_male)*/
                .into(img);
    }
    private void bindImageRounded(final ImageView img,  String filename){
        String path = MainApplication.getUrlApplWeb() + "/Images/member/" + filename;
        Picasso.with(activity)
                .load(path)
                /*.placeholder(R.drawable.pic_male)
                .error(R.drawable.pic_male)*/
                .into(img, new Callback() {
                    @Override
                    public void onSuccess() {
                        PublicFunction.setPhotoProfile(img);
                    }

                    @Override
                    public void onError() {

                    }
                });
    }
}
