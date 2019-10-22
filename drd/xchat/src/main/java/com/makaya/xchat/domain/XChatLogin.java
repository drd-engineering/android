package com.makaya.xchat.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by xbudi on 31/10/2016.
 */

public class XChatLogin implements Serializable {
    public long Id; // Id (Primary key)
    public String Number; // Number (length: 20)
    public String Name; // Name (length: 50)
    public String Phone; // Phone (length: 20)
    public String Email; // Email (length: 50)
    public String ImageProfile; // ImageProfile (length: 50)

}
