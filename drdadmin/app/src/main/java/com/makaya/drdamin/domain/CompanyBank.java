package com.makaya.drdamin.domain;

import java.util.Date;

/**
 * Created by xbudi on 18/11/2016.
 */

public class CompanyBank {
    public int Id;
    public int BankId;
    public String Branch;
    public String AccountNo;
    public String AccountName;
    public int PaymentMethodId;
    public boolean IsActive;
    public String UserId;
    public Date DateCreated;
    public Date DateUpdated;

    // Foreign keys
    public Bank Bank;
    public PaymentMethod PaymentMethod;

    public CompanyBank()
    {
        IsActive = true;
        Bank = new Bank();
        PaymentMethod = new PaymentMethod();
    }
}
