package com.makaya.drd.domain;

import java.util.Date;

public class MemberInvited {
    public long Id; // Id (Primary key)
    public long MemberId; // MemberId
    public long InvitedId; // InvitedId
    public String Status; // Status (length: 2)
    public Date DateExpiry; // DateExpiry
    public Date DateCreated; // DateCreated
    public Date DateUpdated; // DateUpdated

    public String StatusDescr;
    public MemberLite Invited;
    public MemberLite Member;
}
