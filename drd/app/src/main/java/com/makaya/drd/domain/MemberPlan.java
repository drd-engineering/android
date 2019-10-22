package com.makaya.drd.domain;

import java.util.Date;

public class MemberPlan {
    public long Id; // Id (Primary key)
    public long MemberId; // MemberId
    public int SubscriptTypeId; // SubscriptTypeId
    public double Price; // Price
    public String PriceUnitCode; // PriceUnitCode (length: 10)
    public String PriceUnitDescr; // PriceUnitDescr (length: 50)
    public int RotationCount; // RotationCount
    public double RotationPrice; // RotationPrice
    public int RotationCountAdd; // RotationCountAdd
    public int RotationCountUsed; // RotationCountUsed
    public int FlowActivityCount; // FlowActivityCount
    public double FlowActivityPrice; // FlowActivityPrice
    public int FlowActivityCountAdd; // FlowActivityCountAdd
    public int FlowActivityCountUsed; // FlowActivityCountUsed
    public long StorageSize; // StorageSize
    public double StoragePrice; // StoragePrice
    public long StorageSizeAdd; // StorageSizeAdd
    public long StorageSizeUsed; // StorageSizeUsed
    public long DrDriveSize; // DrDriveSize
    public double DrDrivePrice; // DrDrivePrice
    public long DrDriveSizeAdd; // DrDriveSizeAdd
    public long DrDriveSizeUsed; // DrDriveSizeUsed
    public int ExpiryDocDay; // ExpiryDocDay
    public int PackageExpiryDay; // PackageExpiryDay
    public int DrDriveExpiryDay; // DrDriveExpiryDay
    public Date ValidPackage; // ValidPackage
    public Date ValidDrDrive; // ValidDrDrive
    public boolean IsDefault; // IsDefault
    public String UserId; // UserId (length: 50)
    public Date DateCreated; // DateCreated
    public Date DateUpdated; // DateUpdated

    public String StrStorageSize; // StorageSize
    public String StrStorageSizeAdd; // StorageSizeAdd
    public String StrStorageSizeUsed; // StorageSizeUsed
    public String StrStorageSizeBal; // StorageSizeUsed

    public String StrDrDriveSize; // DrDriveSize
    public String StrDrDriveSizeAdd; // DrDriveSizeAdd
    public String StrDrDriveSizeUsed; // DrDriveSizeUsed
    public String StrDrDriveSizeBal; // DrDriveSizeUsed

    // Foreign keys
    public Member Member; // FK_MemberPlan_Member
    public SubscriptType SubscriptType; // FK_MemberPlan_SubscriptType

    public MemberPlan()
    {
        Price = 0;
        RotationCount = 0;
        RotationPrice = 0;
        RotationCountAdd = 0;
        RotationCountUsed = 0;
        FlowActivityCount = 0;
        FlowActivityPrice = 0;
        FlowActivityCountAdd = 0;
        FlowActivityCountUsed = 0;
        StoragePrice = 0;
        StorageSizeAdd = 0;
        StorageSizeUsed = 0;
        DrDriveSize = 0;
        DrDrivePrice = 0;
        DrDriveSizeAdd = 0;
        DrDriveSizeUsed = 0;
        ExpiryDocDay = 0;
        PackageExpiryDay = 0;
        DrDriveExpiryDay = 0;
        IsDefault = true;
    }
}
