package com.makaya.drd.domain;

public class RotationNodeDoc {
    public long Id; // Id (Primary key)
    public long RotationNodeId; // RotationNodeId
    public long DocumentId; // DocumentId
    public int FlagAction; // FlagAction

    // Foreign keys
    public Document Document; // FK_RotationNodeDoc_Document
    public RotationNode RotationNode; // FK_RotationNodeDoc_RotationNode1
}
