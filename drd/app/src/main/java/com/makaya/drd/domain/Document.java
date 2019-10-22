package com.makaya.drd.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Document {
    public long Id; // Id (Primary key)
    public String Title; // Title (length: 500)
    public String Descr; // Descr
    public String FileName; // FileName (length: 100)
    public String FileNameOri; // FileNameOri (length: 100)
    public String ExtFile; // ExtFile (length: 20)
    public int FileFlag; // FileFlag
    public int FileSize; // FileSize
    public long CompanyId; // CompanyId
    public String UserId; // UserId (length: 20)
    public Date DateCreated; // DateCreated
    public Date DateUpdated; // DateUpdated

    // Reverse navigation
    public ArrayList<DocumentAnnotate> DocumentAnnotates;
    public ArrayList<DocumentMember> DocumentMembers; // DocumentMember.FK_DocumentMember_Document
    public ArrayList<RotationNodeDoc> RotationNodeDocs; // RotationNodeDoc.FK_RotationNodeDoc_Document

    public DocumentMember DocumentMember;
}
