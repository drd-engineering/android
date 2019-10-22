package com.makaya.drd.domain;

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

    public GenericData(){
    }

    public GenericData(long Id, String Descr){
        this.Id=Id;
        this.Descr=Descr;
    }

    public GenericData(long Id, String Descr, int ParentId){
        this.Id=Id;
        this.Descr=Descr;
        this.ParentId=ParentId;
    }

    public GenericData(long Id, String Descr, int ParentId, String Tag){
        this.Id=Id;
        this.Descr=Descr;
        this.ParentId=ParentId;
        this.Tag=Tag;
    }

}
