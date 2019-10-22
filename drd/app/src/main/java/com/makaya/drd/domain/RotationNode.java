package com.makaya.drd.domain;

import java.util.ArrayList;
import java.util.Date;

public class RotationNode {
    public long Id; // Id (Primary key)
    public long RotationId; // RotationId
    public long WorkflowNodeId; // WorkflowNodeId
    public long SenderRotationNodeId; // SenderRotationNodeId
    public long MemberId; // MemberId
    public String Value; // Value (length: 20)
    public String Status; // String
    public Date DateRead; // DateRead
    public String UserId; // UserId (length: 20)
    public Date DateCreated; // DateCreated
    public Date DateUpdated; // DateUpdated

    public StatusCode StatusCode;
    public String Note;

    // Reverse navigation
    public ArrayList<RotationNode> RotationNodes; // RotationNode.FK_RotationNode_RotationNode
    public ArrayList<RotationNodeDoc> RotationNodeDocs; // RotationNodeDoc.FK_RotationNodeDoc_RotationNode
    public ArrayList<RotationNodeRemark> RotationNodeRemarks; // RotationNodeRemark.FK_RotationNodeRemark_RotationNode
    public ArrayList<RotationNodeUpDoc> RotationNodeUpDocs; // RotationNodeUpDoc.FK_RotationNodeUpDoc_RotationNode


    // Foreign keys
    public Member Member; // FK_RotationNode_Member
    public Rotation Rotation; // FK_RotationNode_Rotation
    public WorkflowNode WorkflowNode; // FK_RotationNode_WorkflowNode

    public RotationNode()
    {
        RotationNodes = new ArrayList<>();
        RotationNodeDocs = new ArrayList<>();
        RotationNodeRemarks = new ArrayList<>();
        RotationNodeUpDocs = new ArrayList<>();
    }
}
