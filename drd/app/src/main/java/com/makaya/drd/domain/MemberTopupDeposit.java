package com.makaya.drd.domain;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by xbudi on 09/08/2017.
 */

public class MemberTopupDeposit {
    public long Id; // Id (Primary key)
    public String TopupNo; // TopupNo (length: 20)
    public Date TopupDate; // TopupDate
    public long MemberId; // MemberId
    public double Amount; // Amount
    public String PaymentStatus; // PaymentStatus (length: 2)
    public Date DateCreated; // DateCreated
    public Date DateUpdated; // DateUpdated


    public String PaymentStatusDescr;
    public String KeyId;

    // Reverse navigation
    public ArrayList<MemberTopupPayment> MemberTopupPayments; // MemberTopupPayment.FK_MemberTopupPayment_MemberTopupDeposit

    // Foreign keys
    public Member Member; // FK_MemberTopupDeposit_Member

    public MemberTopupDeposit()
    {
        MemberTopupPayments = new ArrayList<>();
    }
}
