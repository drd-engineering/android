package com.makaya.drd.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by xbudi on 17/11/2016.
 */

public class CompanyBank implements Serializable {
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

    public String KeyId;
    // Foreign keys
    public Bank Bank;
    public PaymentMethod PaymentMethod;

/*
    public String Code;
    public int UsingType; // UsingType
    public int ConfirmType; // ConfirmType
*/

    public CompanyBank()
    {
        IsActive = true;
        Bank = new Bank();
        PaymentMethod = new PaymentMethod();
    }
}
