package com.makaya.drd.domain;

public class RotationNodeUpDoc {
    public long Id; // Id (Primary key)
    public long RotationNodeId; // RotationNodeId
    public long DocumentUploadId; // DocumentUploadId
    /*public String FileName; // FileName (length: 100)
    public String FileNameOri; // FileNameOri (length: 100)
    public String ExtFile; // ExtFile (length: 20)
    public int FileSize; // FileSize
    public int FileFlag; // FileFlag*/

    // Foreign keys
    public DocumentUpload DocumentUpload; // FK_RotationNodeUpDoc_DocumentUpload
    public RotationNode RotationNode; // FK_RotationNodeUpDoc_RotationNode
}
