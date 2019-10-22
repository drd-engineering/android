package com.makaya.drd.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class DocumentLite implements Serializable{
    public long Id; // Id (Primary key)
    public String Title; // Title (length: 500)
    public String Descr; // Descr
    public String FileName; // FileName (length: 100)
    public String FileNameOri; // FileNameOri (length: 100)
    public String ExtFile; // ExtFile (length: 20)
    public long CompanyId; // CompanyId
    public String UserId; // UserId (length: 20)
    public Date DateCreated; // DateCreated
    public Date DateUpdated; // DateUpdated

    public ArrayList<DocumentMember> DocumentMembers; // DocumentMember.FK_DocumentMember_Document

    public DocumentLite()
    {
        DocumentMembers=new ArrayList<>();
    }
}
