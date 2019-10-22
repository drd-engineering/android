package com.makaya.drd.domain;

import java.util.Date;

public class RotationNodeRemark {
    public long Id; // Id (Primary key)
    public long RotationNodeId; // RotationNodeId
    public String Remark; // Remark
    public Date DateStamp; // DateStamp

    // Foreign keys
    public RotationNode RotationNode; // FK_RotationNodeRemark_RotationNode
}
