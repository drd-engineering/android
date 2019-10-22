package com.makaya.drdamin.domain;

import java.util.Date;

/**
 * Created by xbudi on 06/03/2017.
 */

public class UserAdmin {
    public int Id; // Id (Primary key)
    public String Email; // Email (length: 50)
    public String Name; // Name (length: 50)
    public String Phone; // Phone (length: 20)
    public int AdminType; // AdminType
    public Date LastLogin; // LastLogin
    public Date LastLogout; // LastLogout
    public String AppZoneAccess; // AppZoneAccess (length: 50)
    public String Password; // Password (length: 20)
    public boolean IsActive; // IsActive
    public String UserId; // UserId (length: 20)
    public Date DateCreated; // DateCreated
    public Date DateUpdated; // DateUpdated

}
