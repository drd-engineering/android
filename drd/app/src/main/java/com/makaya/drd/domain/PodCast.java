package com.makaya.drd.domain;

import java.util.Date;

/**
 * Created by xbudi on 01/10/2017.
 */

public class PodCast {
    public long Id; // Id (Primary key)
    public String Title; // Title (length: 100)
    public String Descr; // Descr (length: 100)
    public int Duration; // Duration 
    public String Image; // Image (length: 100)
    public String AudioFileName; // AudioFileName (length: 100)
    public String AppZone; // AppZone (length: 10)
    public boolean IsAppZonePublish; // IsAppZonePublish
    public boolean IsActive; // IsActive
    public String UserId; // UserId (length: 20)
    public Date DateCreated; // DateCreated
    public Date DateUpdated; // DateUpdated

}
