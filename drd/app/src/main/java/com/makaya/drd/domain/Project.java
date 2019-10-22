package com.makaya.drd.domain;

import java.util.ArrayList;
import java.util.Date;

public class Project {
    public long Id; // Id (Primary key)
    public String Name; // Name (length: 100)
    public String Descr; // Descr
    public long CompanyId; // CompanyId
    public boolean IsActive; // IsActive
    public String UserId; // UserId (length: 50)
    public Date DateCreated; // DateCreated
    public Date DateUpdated; // DateUpdated

    // Reverse navigation
    public ArrayList<Workflow> Workflows; // Workflow.FK_Workflow_Project

    // Foreign keys
    public Company Company; // FK_Project_Company
}
