package com.makaya.drd.domain;

/**
 * Created by xbudi on 10/09/2017.
 */

public class NewsDetail {
    public long Id; // Id (Primary key)
    public long KajianIslamId; // KajianIslamId
    public String Image;// { get; set; } // Image (length: 50)
    public String Descr; // Descr

    // Foreign keys
    //public News News; // FK_KajianIslamDetail_KajianIslam
}
