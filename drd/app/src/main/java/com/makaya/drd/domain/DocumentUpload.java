package com.makaya.drd.domain;

import java.util.Date;

public class DocumentUpload {
    public long Id; // Id (Primary key)
    public String FileName; // FileName (length: 100)
    public String FileNameOri; // FileNameOri (length: 100)
    public String ExtFile; // ExtFile (length: 20)
    public int FileFlag; // FileFlag
    public int FileSize; // FileSize
    public long CreatorId; // CreatorId
    public Date DateCreated; // DateCreated
}
