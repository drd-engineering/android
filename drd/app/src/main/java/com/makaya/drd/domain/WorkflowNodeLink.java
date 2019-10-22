package com.makaya.drd.domain;

public class WorkflowNodeLink {
    public long Id; // Id (Primary key)
    public long WorkflowNodeId; // WorkflowNodeId
    public long WorkflowNodeToId; // WorkflowNodeToId
    public String Caption; // Caption (length: 100)
    public String Operator; // Operator (length: 10)
    public String Value; // Value (length: 20)
    public int SymbolId; // SymbolId

    // Foreign keys
    public Symbol Symbol; // FK_WorkflowNodeLink_Symbol
    public WorkflowNode WorkflowNode_WorkflowNodeId; // FK_WorkflowNodeLink_WorkflowNode
    public WorkflowNode WorkflowNode_WorkflowNodeToId; // FK_WorkflowNodeLink_WorkflowNode1
}
