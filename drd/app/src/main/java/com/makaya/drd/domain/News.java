package com.makaya.drd.domain;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by xbudi on 10/09/2017.
 */

public class News {
    public long Id; // Id (Primary key)
    public String Title; // Title (length: 50)
    public String Descr; // Descr
    public int NewsType;
    public String UserId; // UserId (length: 20)
    public Date DateCreated; // DateCreated
    public Date DateUpdated; // DateUpdated

    // Reverse navigation
    public ArrayList<NewsDetail> NewsDetails; // NewsDetail.FK_KajianIslamDetail_KajianIslam

    public News()
    {
        NewsDetails = new ArrayList<>();
    }
}
