package com.makaya.drd.domain;

import java.util.ArrayList;

public class WorkflowNode {
    public long Id; // Id (Primary key)
    public long WorkflowId; // WorkflowId
    public long MemberId; // MemberId
    public int SymbolId; // SymbolId
    public String Caption; // Caption (length: 100)
    public String Info; // Info (length: 1000)
    public String Operator; // Operator (length: 10)
    public String Value; // Value (length: 20)
    public String PosLeft; // PosLeft (length: 10)
    public String PosTop; // PosTop (length: 10)
    public String Width; // Width (length: 10)
    public String Height; // Height (length: 10)
    public String TextColor; // TextColor (length: 20)
    public String BackColor; // BackColor (length: 20)
    public int Flag; // Flag

    // Reverse navigation
    public ArrayList<RotationNode> Rotations; // Rotation.FK_Rotation_WorkflowNode
    public ArrayList<WorkflowNodeLink> WorkflowNodeLinks_WorkflowNodeId; // WorkflowNodeLink.FK_WorkflowNodeLink_WorkflowNode
    public ArrayList<WorkflowNodeLink> WorkflowNodeLinks_WorkflowNodeToId; // WorkflowNodeLink.FK_WorkflowNodeLink_WorkflowNode1

    // Foreign keys
    public Member Member; // FK_WorkflowNode_Member
    public Symbol Symbol; // FK_WorkflowNode_Symbol
    public Workflow Workflow; // FK_WorkflowNode_Workflow

}
