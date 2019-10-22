package com.makaya.xchat.domain;

import java.util.Date;

/**
 * Created by xbudi on 25/10/2016.
 */

public class MessageClass {
    public long Id;
    public long FromId;
    public long ToId;
    public String TextMessage;
    public int MessageType;
    public Date DateCreated;
    public Date DateOpened;
    public Date DateReplied;
}
