package com.makaya.drd.domain;

import java.io.Serializable;

/**
 * Created by xbudi on 02/04/2017.
 */

public class Bank implements Serializable {
    public int Id;
    public String Code;
    public String Name;
    public String Logo;
    public int BankType;

    public Bank()
    {
        BankType = 1;
    }
}
