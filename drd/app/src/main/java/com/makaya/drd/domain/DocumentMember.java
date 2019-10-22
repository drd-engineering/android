package com.makaya.drd.domain;

import java.io.Serializable;

public class DocumentMember implements Serializable {
    public long Id; // Id (Primary key)
    public long DocumentId; // DocumentId
    public long MemberId; // MemberId
    public int FlagPermission; // FlagPermission
    public int FlagAction; // FlagAction

    public String MemberNumber;
    public String MemberName;
}
