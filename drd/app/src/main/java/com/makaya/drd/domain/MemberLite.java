package com.makaya.drd.domain;

import java.io.Serializable;

public class MemberLite implements Serializable {
    public long Id; // Id (Primary key)
    public String Number; // Number (length: 20)
    public String Name; // Name (length: 50)
    public String Phone; // Phone (length: 20)
    public String Email; // Email (length: 50)
    public String ImageProfile; // ImageProfile (length: 50)
    public String ImageQrCode; // ImageQrCode (length: 50)

    public String Profession;
    public int MemberType;
    public String UserGroup;
    public String CompanyName;
}
