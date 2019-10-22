package com.makaya.drdamin.domain;

/**
 * Created by xbudi on 03/03/2017.
 */

public class DataList {
    public long Id;
    public String Code;
    public String Text;

    public DataList(long Id, String Code, String Text)
    {
        this.Code=Code;
        this.Id=Id;
        this.Text=Text;
    }
    public DataList()
    {
    }
}
