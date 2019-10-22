package com.makaya.drd.domain;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by xbudi on 06/01/2017.
 */

public class PaymentMethod implements Serializable {

    public int Id; // Id (Primary key)
    public String Code; // Code (length: 5)
    public String Name; // Name (length: 50)
    public String Logo; // Logo (length: 50)
    public String Descr; // Descr (length: 1000)
    public int UsingType; // UsingType
    public int ConfirmType; // ConfirmType

    public ArrayList<CompanyBank> CompanyBanks;
}
