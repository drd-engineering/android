package com.makaya.drd.domain;

import android.graphics.Bitmap;

public class UploadParam {
    public int idx;
    public int fileType;
    public Bitmap bmp;

    public UploadParam(int idx, int fileType, Bitmap bmp){
        this.idx=idx;
        this.fileType=fileType;
        this.bmp=bmp;
    }
}
