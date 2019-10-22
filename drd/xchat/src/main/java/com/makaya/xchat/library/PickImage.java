package com.makaya.xchat.library;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xbudi on 03/10/2016.
 */
public class PickImage {

    Activity activity;

    public PickImage(Activity activity)
    {
        this.activity=activity;
    }
    public PickImage()
    {
    }
    public void pickImageOther() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

        activity.startActivityForResult(chooserIntent, 1);
    }

    public void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("outputX", 128);
        intent.putExtra("outputY", 128);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("return-data", true);
        activity.startActivityForResult(intent, 1);
    }
    public void FromCamera() {

        Log.i("camera", "startCameraActivity()");
        File file = new File("image/*");
        Uri outputFileUri = Uri.fromFile(file);
        Intent intent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        activity.startActivityForResult(intent, 1);

    }

    public void FromCard() {
        Intent i = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(i, 2);
    }

    public void storeImage(Bitmap image) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            //Log.d(TAG,
            //        "Error creating media file, check storage permissions: ");// e.getMessage());
            Toast.makeText(activity,"Error creating media file, check storage permissions. ",Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            //image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            //Log.d(TAG, "File not found: " + e.getMessage());
            Toast.makeText(activity,"File not found: " + e.getMessage(),Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            //Log.d(TAG, "Error accessing file: " + e.getMessage());
            Toast.makeText(activity,"Error accessing file: " + e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    /** Create a File for saving an image or video */
    private  File getOutputMediaFile(){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getDataDirectory()//.getExternalStorageDirectory()
                + "/Android/data/"
                + activity.getApplicationContext().getPackageName()
                + "/Files");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName="MI_"+ timeStamp +".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }

    public String saveToInternalStorage(Bitmap bitmapImage,String path, String fileName){

        ContextWrapper cw = new ContextWrapper(activity.getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir(path, Context.MODE_PRIVATE);//cw.getDir(path, Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory, fileName);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            }catch(IOException x){}
        }
        return directory.getAbsolutePath();
    }

    public void dynamicPickImage(int requestCode) {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        /*Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image*//*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {getIntent, pickIntent});*/

        activity.startActivityForResult(/*chooserIntent*/getIntent, requestCode);

       /* //Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        Intent intent = new Intent(Intent.ACTION_PICK,
                //Intent.ACTION_GET_CONTENT,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image*//*");
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        *//*intent.putExtra("outputX", 128);
        intent.putExtra("outputY", 0);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 0);*//*
        intent.putExtra("return-data", true);
        activity.startActivityForResult(intent, requestCode);*/
    }


    public Bitmap loadImageFromStorage(String path, String fileName)
    {

        try {
            ContextWrapper cw = new ContextWrapper(activity.getApplicationContext());
            // path to /data/data/yourapp/app_data/imageDir
            File directory = cw.getDir(path, Context.MODE_PRIVATE);
            // Create imageDir
            File mypath=new File(directory, fileName);

            return BitmapFactory.decodeStream(new FileInputStream(mypath));
            //Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            //ImageView img=(ImageView)findViewById(R.id.imgPicker);
            //img.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public boolean saveImageToInternalStorage(Bitmap image, String fileName) {

        try {
            // Use the compress method on the Bitmap object to write image to
            // the OutputStream
            FileOutputStream fos = activity.openFileOutput(fileName, Context.MODE_PRIVATE);

            // Writing the bitmap to the output stream
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();

            return true;
        } catch (Exception e) {
            Log.e("saveToInternalStorage()", e.getMessage());
            return false;
        }
    }


    /*-------------------------------------------------
    SAVE AND LOAD (work)
    ---------------------------------------------------*/


    public String saveImage(Bitmap bitmap, String fileName) {
        OutputStream output;
        String recentImageInCache;
        File filepath = Environment.getExternalStorageDirectory();

        // Create a new folder in SD Card
        File dir = new File(filepath.getAbsolutePath() + "/"+ activity.getApplicationContext().getPackageName()+"/profile");
        dir.mkdirs();

        // Create a name for the saved image
        File file = new File(dir, fileName);
        try {

            output = new FileOutputStream(file);

            // Compress into png format image from 0% - 100%
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
            output.flush();
            output.close();
            return file.toString();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public Bitmap loadImage(String fileName)
    {
        File filepath = Environment.getExternalStorageDirectory();
        File image_file = new File(filepath.getAbsolutePath() + "/"+ activity.getApplicationContext().getPackageName()+"/profile/"+fileName);

        if (image_file.exists()) {
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeFile(image_file.getAbsolutePath());
                return bitmap;
            } catch (Exception e) {
                Log.e("Error", "Bitmap Exception" + e.getMessage());
            }
        }
        return null;
    }

    public Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
        int targetWidth = 50;
        int targetHeight = 50;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                targetHeight,Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2,
                ((float) targetHeight - 1) / 2,
                (Math.min(((float) targetWidth),
                        ((float) targetHeight)) / 2),
                Path.Direction.CCW);

        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap,
                new Rect(0, 0, sourceBitmap.getWidth(),
                        sourceBitmap.getHeight()),
                new Rect(0, 0, targetWidth, targetHeight), null);
        return targetBitmap;
    }

    public Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

}
