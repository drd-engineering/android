package com.makaya.drd.domain;

import java.util.Date;

/**
 * Created by xbudi on 01/10/2017.
 */

public class PodCastLite {
    public long Id; // Id (Primary key)
    public String Title; // Title (length: 100)
    public int Duration; // Duration
    public String Image; // Image
    public Date DateCreated; // DateCreated

    public int type;

    public PodCastLite()
    {
        type=0;
    }
}
