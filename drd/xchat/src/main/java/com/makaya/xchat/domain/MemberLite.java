package com.makaya.xchat.domain;

import java.io.Serializable;
import java.util.Date;

public class MemberLite implements Serializable {
    public long Id; // Id (Primary key)
    public String Number; // RegNumber (length: 20)
    public String Name; // Name (length: 50)
    public String Phone; // Phone (length: 20)
    public String Email; // Email (length: 20)
    public String ImageProfile; // ImageProfile (length: 50)
    public String Profession;
    public int MemberType;
    public boolean IsActive; // IsActive
    public Date DateCreated; // DateCreated
}
