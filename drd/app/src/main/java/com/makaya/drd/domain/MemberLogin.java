package com.makaya.drd.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by xbudi on 31/10/2016.
 */

public class MemberLogin implements Serializable {
    public long Id; // Id (Primary key)
    public String Number; // Number (length: 20)
    public String Name; // Name (length: 50)
    public String Phone; // Phone (length: 20)
    public String Email; // Email (length: 50)
    public int MemberType; // MemberType
    public String KtpNo; // KtpNo (length: 50)
    public String ImageProfile; // ImageProfile (length: 50)
    public String ImageQrCode; // ImageQrCode (length: 50)
    public Date LastLogin; // LastLogin
    public Date LastLogout; // LastLogout
    public long ActivationKeyId; // ActivationKeyId
    public long CompanyId; // CompanyId
    public String UserGroup; // UserGroup (length: 20)
    public String ImageSignature; // ImageSignature (length: 100)
    public String ImageInitials; // ImageInitials (length: 100)
    public String ImageStamp; // ImageStamp (length: 100)
    public String ImageKtp1; // ImageKtp1 (length: 100)
    public String ImageKtp2; // ImageKtp2 (length: 100)
    public Company Company;

    public MemberLogin(){
        Company =new Company();
        Company.BackColorBar="#757575";
        Company.BackColorPage="#bdbdbd";
    }
}
