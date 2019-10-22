package com.makaya.drdamin.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by xbudi on 28/09/2016.
 */

public class Member implements Serializable {

    public long Id;
    public String Name;
    public int Gender;
    public Date BirthDate;
    public String Phone;
    public String EMail;
    public Double Discount;
    public int AllowSelfDrive;
    public int MemberStatus;
    public String Password;
    public long VcCustomerId;
    public Date DateCreated;
    public Date DateUpdated;

    public Member()
    {
        Id=0;
        Name="";
        Gender=0;
        BirthDate=null;
        Phone="";
        EMail="";
        Discount=0.;
        AllowSelfDrive=0;
        MemberStatus=1;
        Password="";
        DateCreated=null;
        DateUpdated=null;
    }

}
