package com.makaya.drd.domain;

import java.util.Date;

/**
 * Created by xbudi on 09/08/2017.
 */

public class MemberTopupPayment {
    public long Id; // Id (Primary key)
    public String PaymentNo; // PaymentNo (length: 20)
    public Date PaymentDate; // PaymentDate
    public long TopupDepositId; // TopupDepositId
    public double Amount; // Amount
    public int CompanyBankId; // CompanyBankId
    public long MemberAccountId; // MemberAccountId
    public String PaymentStatus; // PaymentStatus (length: 2)
    public Date DateCreated; // DateCreated
    public Date DateUpdated; // DateUpdated

    public String PaymentStatusDescr;
    public String VoucherNo;

    // Foreign keys
    public CompanyBank CompanyBank; // FK_MemberTopupPayment_CompanyBank
    public MemberAccount MemberAccount; // FK_MemberTopupPayment_MemberAccount
    public MemberTopupDeposit MemberTopupDeposit; // FK_MemberTopupPayment_MemberTopupDeposit

    public MemberTopupPayment()
    {
        VoucherNo="";
    }
}
