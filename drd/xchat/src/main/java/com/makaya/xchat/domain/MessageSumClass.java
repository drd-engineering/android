package com.makaya.xchat.domain;

import java.util.Date;

/**
 * Created by xbudi on 25/10/2016.
 */

public class MessageSumClass {
    public long Id;
    public String SenderFoto;
    public long SenderId;
    public String SenderName;
    public String SenderProfession;
    public int SenderType;

    public long ReceiverId;
    public String ReceiverName;

    public int Unread;
    public Date TheDate;
}
