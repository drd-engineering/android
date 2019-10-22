package com.makaya.drd.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by xbudi on 31/10/2016.
 */

public class Member implements Serializable {
    /*public int Id; // ID (Primary key)
    public String RegNumber; // RegNumber (length: 20)
    public Date RegDate; // RegDate
    public String Name; // Name (length: 50)
    public String Address; // Address (length: 1000)
    public int CountryId; // CountryID
    public int ProvinceId; // ProvinceID
    public int DistrictId; // DistrictID
    public int SubDistrictId; // SubDistrictID
    public int VillageId; // VillageID
    public String PosCode; // PosCode (length: 5)
    public String Email; // Email (length: 50)
    public String Phone; // Phone (length: 20)
    public String Phone2; // Phone2 (length: 20)
    public String Phone3; // Phone3 (length: 20)
    public String LicenseType; // LicenseType (length: 10)
    public Date LicensePeriod; // LicensePeriod
    public String LicenseNo; // LicenseNo (length: 50)
    public String BirthPlace; // BirthPlace (length: 50)
    public Date BirthDate; // BirthDate
    public int BranchOfficeId; // BranchOfficeID
    public int MemberTypeId; // MemberTypeID
    public int ApprovalStatus; // ApprovalStatus
    public int ActiveStatus; // ActiveStatus
    public int ProcessStatus; // ProcessStatus
    public String ZonaKir; // ZonaKIR (length: 1)
    public String AppZone; // AppZone (length: 10)
    public long ActivationKeyId; // ActivationKeyId
    public Date DateCreated; // DateCreated
    public Date DateUpdated; // DateUpdated
    public String Password;*/

    public long Id; // Id (Primary key)
    public String Number; // Number (length: 20)
    public int MemberTitleId; // Name (length: 50)
    public String Name; // Name (length: 50)
    public String Phone; // Phone (length: 20)
    public String Email; // Email (length: 50)
    public int MemberType; // MemberType
    public String ImageProfile; // ImageProfile (length: 50)
    public String ImageQrCode; // ImageQrCode (length: 50)
    public Date LastLogin; // LastLogin
    public Date LastLogout; // LastLogout
    public long ActivationKeyId; // ActivationKeyId
    public String Password; // Password (length: 50)
    public long CompanyId; // CompanyId
    public String UserGroup; // UserGroup (length: 20)
    public String ImageSignature; // ImageSignature (length: 100)
    public String ImageInitials; // ImageInitials (length: 100)
    public String ImageStamp; // ImageStamp (length: 100)
    public String ImageKtp1; // ImageKtp1 (length: 100)
    public String ImageKtp2; // ImageKtp2 (length: 100)
    public boolean IsActive; // IsActive
    public String UserId; // UserId (length: 20)
    public Date DateCreated; // DateCreated
    public Date DateUpdated; // DateUpdated

    //// Reverse navigation
    //public virtual System.Collections.Generic.ICollection<DtoDocumentMember> DocumentMembers; // DocumentMember.FK_DocumentMember_Member
    //public virtual System.Collections.Generic.ICollection<DtoRotation> Rotations; // Rotation.FK_Rotation_Member
    //public virtual System.Collections.Generic.ICollection<DtoRotationNode> RotationNodes; // RotationNode.FK_RotationNode_Member
    //public virtual System.Collections.Generic.ICollection<DtoWorkflowNode> WorkflowNodes; // WorkflowNode.FK_WorkflowNode_Member

    // Foreign keys
    public Company Company; // FK_Member_Company
    public MemberTitle MemberTitle;

    //public DtoMemberMaster Master;
    public long LoginId;
}
