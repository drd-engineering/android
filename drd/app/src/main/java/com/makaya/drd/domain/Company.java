package com.makaya.drd.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by xbudi on 27/07/2017.
 */

public class Company implements Serializable {
    public long Id; // Id (Primary key)
    public String Code; // Code (length: 10)
    public String Name; // Name (length: 50)
    public String Contact; // Contact (length: 50)
    public String Phone; // Phone (length: 20)
    public String Email; // Email (length: 100)
    public String Descr; // Descr
    public String Address; // Address (length: 1000)
    public String PointLocation; // PointLocation (length: 1000)
    public double Latitude; // Latitude
    public double Longitude; // Longitude
    public String CountryCode; // CountryCode (length: 10)
    public String CountryName; // CountryName (length: 50)
    public String AdminArea; // AdminArea (length: 50)
    public String SubAdminArea; // SubAdminArea (length: 50)
    public String Locality; // Locality (length: 50)
    public String SubLocality; // SubLocality (length: 50)
    public String Thoroughfare; // Thoroughfare (length: 50)
    public String SubThoroughfare; // SubThoroughfare (length: 10)
    public String PostalCode; // PostalCode (length: 5)
    public String Image1; // Image1 (length: 100)
    public String Image2; // Image2 (length: 100)
    public String ImageCard; // ImageCard (length: 100)
    public String BackColorBar; // BackColorBar (length: 20)
    public String BackColorPage; // BackColorPage (length: 20)
    public int CompanyType; // CompanyType
    public int SubscriptionType; // SubscriptionType
    public String ImageQrCode; // ImageQrCode (length: 50)
}
