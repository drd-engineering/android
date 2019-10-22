package com.makaya.drdamin.domain;

import java.util.Date;

/**
 * Created by xbudi on 11/10/2016.
 */

public class BillingOrder {
    public long Id;
    public String BillingNo;
    public Date BillingDate;
    public long MemberId;
    public double Amount;
    public double Discount;
    public double Paid;
    public String BillingStatus;
    public int CancelationReasonId;
    public Date CancelationDate;
    public Date DateCreated;
    public Date DateUpdated;
}
