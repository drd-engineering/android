package com.makaya.drd.domain;

import java.util.ArrayList;
import java.util.Date;

public class Workflow {
    public long Id; // Id (Primary key)
    public String Name; // Name (length: 100)
    public String Descr; // Descr
    public long ProjectId; // ProjectId
    public boolean IsActive; // IsActive
    public String UserId; // UserId (length: 50)
    public Date DateCreated; // DateCreated
    public Date DateUpdated; // DateUpdated

    // Reverse navigation
    public ArrayList<WorkflowNode> WorkflowNodes; // WorkflowNode.FK_WorkflowNode_Workflow

    // Foreign keys
    public Project Project; // FK_Workflow_Project

    public Workflow()
    {
        WorkflowNodes = new ArrayList<>();
    }
}
