package com.makaya.drdamin.domain;

import com.google.gson.annotations.Expose;

/**
 * Created by xbudi on 07/10/2016.
 */

public class GenericData {
    @Expose
    public long Id;
    @Expose
    public String Descr;
    @Expose
    public int ParentId;
    @Expose
    public String Tag;

    GenericData(){
    }

    GenericData(long Id, String Descr){
        this.Id=Id;
        this.Descr=Descr;
    }

    GenericData(long Id, String Descr, int ParentId){
        this.Id=Id;
        this.Descr=Descr;
        this.ParentId=ParentId;
    }

    GenericData(long Id, String Descr, int ParentId, String Tag){
        this.Id=Id;
        this.Descr=Descr;
        this.ParentId=ParentId;
        this.Tag=Tag;
    }

}
