package com.makaya.drd.domain;

import java.util.Date;

/**
 * Created by xbudi on 11/09/2017.
 */

public class NewsLite {
    public long Id; // Id (Primary key)
    public String Title; // Title (length: 50)
    public String Descr; // Descr
    public String Image; // Image
    public Date DateCreated; // DateCreated
    public Date DateUpdated; // DateUpdated
    public int type;

    public NewsLite()
    {
        type=0;
    }
}
