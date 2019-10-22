package com.makaya.drd.domain;

import java.util.Date;

public class MemberDepositTrx {
    public long Id; // Id (Primary key)
    public String TrxNo; // TrxNo (length: 20)
    public Date TrxDate; // TrxDate
    public String TrxType; // TrxType (length: 10)
    public long TrxId; // TrxId
    public String Descr; // Descr (length: 500)
    public long MemberId; // MemberId
    public double Amount; // Amount
    public int DbCr; // DbCr
    public Date DateCreated; // TrxDate
}
