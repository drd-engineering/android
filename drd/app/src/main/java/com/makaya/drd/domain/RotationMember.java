package com.makaya.drd.domain;

public class RotationMember {
    public long Id; // Id (Primary key)
    public long RotationId; // RotationId
    public long WorkflowNodeId; // WorkflowNodeId
    public long MemberId; // MemberId
    public int FlagPermission; // FlagPermission

    public String ActivityName;
    public String MemberPicture;
    public String MemberNumber;
    public String MemberName;
    public String MemberEmail;

    // Foreign keys
    public Member Member; // FK_RotationMember_Member
    public Rotation Rotation; // FK_RotationMember_Rotation
    public WorkflowNode WorkflowNode; // FK_RotationMember_WorkflowNode
}
