package com.makaya.drd.domain;

import java.util.Date;

public class SubscriptType {
    public int Id; // Id (Primary key)
    public String TypeCode; // TypeCode (length: 10)
    public String ClassName; // ClassName (length: 100)
    public String Descr; // Descr (length: 1000)
    public double Price; // Price
    public String PriceUnitCode; // PriceUnitCode (length: 10)
    public String PriceUnitDescr; // PriceUnitDescr (length: 50)
    public int RotationCount; // RotationCount
    public double RotationPrice; // RotationPrice
    public int FlowActivityCount; // FlowActivityCount
    public double FlowActivityPrice; // FlowActivityPrice
    public int StorageSize; // StorageSize
    public double StoragePrice; // StoragePrice
    public int ExpiryDocDay; // ExpiryDocDay
    public String UserId; // UserId (length: 50)
    public Date DateCreated; // DateCreated
    public Date DateUpdated; // DateUpdated
}
