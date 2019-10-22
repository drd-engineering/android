package com.makaya.drd.domain;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by xbudi on 21/10/2016.
 */

public class Dashboard implements Serializable {
    public int Rotation;
    public int Inbox;
    public int Altered;
    public int Revised;
    public int InProgress;
    public int Pending;
    public int Signed;
    public int Declined;
    public int Completed;

    public int InviteAccepted;
    public int Invitation;

    public int UnreadChat;
    public double DepositBalance;
}
