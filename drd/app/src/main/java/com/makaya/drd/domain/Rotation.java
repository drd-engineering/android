package com.makaya.drd.domain;

import java.util.ArrayList;
import java.util.Date;

public class Rotation {
    public long Id; // Id (Primary key)
    public String Subject; // Subject (length: 100)
    public long WorkflowId; // WorkflowId
    public String Status; // Status (length: 2)
    public String Remark; // Remark
    public long MemberId; // MemberId
    public Date DateCreated; // DateCreated
    public Date DateUpdated; // DateUpdated
    public Date DateStatus; // DateUpdated

    public String StatusDescr;

    public long RotationNodeId;
    public long DefWorkflowNodeId;
    public int FlagAction;
    public String DecissionInfo;
    //public RotationNode RotationNode;
    // Document summaries
    public ArrayList<RotationNodeDoc> SumRotationNodeDocs; // RotationNodeDoc.FK_RotationNodeDoc_RotationNode
    public ArrayList<RotationNodeUpDoc> SumRotationNodeUpDocs; // RotationNodeUpDoc.FK_RotationNodeUpDoc_RotationNode

    // Reverse navigation
    public ArrayList<RotationMember> RotationMembers; // RotationMember.FK_RotationMember_Rotation
    public ArrayList<RotationNode> RotationNodes; // RotationNode.FK_RotationNode_Rotation

    // Foreign keys
    public Member Member; // FK_Rotation_Member
    public Workflow Workflow; // FK_Rotation_Workflow

}
