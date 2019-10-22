package com.makaya.drdamin.domain;

import java.util.Date;

/**
 * Created by xbudi on 17/11/2016.
 */

public class PaymentOrder {
    public long Id;
    public String PaymentNo;
    public Date PaymentDate;
    public long BillingOrderId;
    public double Amount;
    public int CompanyBankId;
    public long MemberAccountId;
    public String PaymentStatus;
    public Date DateCreated ;
    public Date DateUpdated ;

    public String PaymentStatusDescr;

    public com.makaya.drdamin.domain.CompanyBank CompanyBank;
    public com.makaya.drdamin.domain.BillingOrder BillingOrder;
    public com.makaya.drdamin.domain.MemberAccount MemberAccount;

    public com.makaya.drdamin.domain.Member Member;

}
