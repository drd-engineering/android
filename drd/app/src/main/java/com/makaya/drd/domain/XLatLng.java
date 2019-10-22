package com.makaya.drd.domain;

import java.io.Serializable;

/**
 * Created by xbudi on 26/02/2017.
 */

public class XLatLng implements Serializable {
    public double Latitude;
    public double Longitude;

    public XLatLng(double lat, double lng)
    {
        Latitude=lat;
        Longitude=lng;
    }
    public XLatLng()
    {
    }
}
