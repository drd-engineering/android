package com.makaya.drdamin.domain;

import java.io.Serializable;

/**
 * Created by xbudi on 06/01/2017.
 */

public class PaymentMethod implements Serializable {
    public int Id;
    public String Code;
    public String Name;
    public String Logo;
    public String Descr;
    public int UsingType;
    public int ConfirmType;
}
