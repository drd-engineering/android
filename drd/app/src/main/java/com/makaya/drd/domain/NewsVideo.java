package com.makaya.drd.domain;

import java.util.Date;

/**
 * Created by xbudi on 01/10/2017.
 */

public class NewsVideo {
    public long Id; // Id (Primary key)
    public String Code; // Code (length: 20)
    public String Title; // Title (length: 100)
    public String Descr; // Descr (length: 100)
    public String ChannelId; // ChannelId (length: 20)
    public String ChannelTitle; // ChannelTitle (length: 50)
    public long CategoryId; // CategoryId
    public Date DatePublished; // DatePublished
    public Date DateCreated; // DateCreated

    public int type;

    public NewsVideo()
    {
        type=0;
    }
}
