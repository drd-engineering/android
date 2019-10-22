package com.makaya.drd.domain;

import java.util.ArrayList;
import java.util.Date;

public class RotationLite {
    public String Key;
    public long Id; // Id (Primary key)
    public String Subject; // Subject (length: 2)
    public long WorkflowId; // WorkflowId
    public String Status; // Status (length: 2)
    public long MemberId; // MemberId
    public Date DateCreated; // DateCreated
    public Date DateUpdated; // DateUpdated
    public Date DateStatus; // DateUpdated
    public Date DateStarted; // DateStarted

    public long RotationNodeId;
    public String ActivityName;
    public String WorkflowName;
    public String StatusDescr;

    public ArrayList<RotationNode> RotationNodes;
}
