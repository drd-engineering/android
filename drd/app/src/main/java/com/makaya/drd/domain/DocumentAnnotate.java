package com.makaya.drd.domain;

import java.util.Date;

public class DocumentAnnotate {
    public long Id; // Id (Primary key)
    public long DocumentId; // DocumentId
    public int Page; // Page
    public int AnnotateTypeId; // AnnotateTypeId
    public double LeftPos; // LeftPos
    public double TopPos; // TopPos
    public double WidthPos; // WidthPos
    public double HeightPos; // HeightPos
    public String Color; // Color (length: 50)
    public String BackColor; // BackColor (length: 50)
    public String Data; // Data
    public String Data2; // Data2
    public int Rotation; // Rotation
    public double ScaleX; // ScaleX
    public double ScaleY; // ScaleY
    public double TransX; // TransX
    public double TransY; // TransY
    public double StrokeWidth; // StrokeWidth
    public double Opacity; // Opacity
    public long CreatorId; // CreatorId
    public long AnnotateId; // AnnotateId
    public int Flag; // Flag
    public String FlagCode; // FlagCode (length: 20)
    public String UserId; // UserId (length: 50)
    public Date DateCreated; // DateCreated
    public Date DateUpdated; // DateUpdated

    public Annotate Annotate;

    // Foreign keys
    public AnnotateType AnnotateType; // FK_DocumentAnnotate_AnnotateType
    public Document Document; // FK_DocumentAnnotate_Document

    public DocumentAnnotate()
    {
        Rotation = 0;
        ScaleX = 1;
        ScaleY = 1;
        TransX = 0;
        TransY = 0;
        StrokeWidth = 4;
        Opacity = 1;
        Flag = 0;
        Annotate = new Annotate();
    }
}
