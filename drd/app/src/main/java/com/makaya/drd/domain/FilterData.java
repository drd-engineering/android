package com.makaya.drd.domain;

import com.makaya.drd.MainApplication;

import java.util.ArrayList;

/**
 * Created by xbudi on 18/11/2016.
 */

public class FilterData {
    public String GroupCaption;
    public String Caption;
    public String ColumnName;
    public String Value;
    public String CaptionTo;
    public String ColumnNameTo;
    public String ValueTo;
    public boolean CheckBox;
    public ArrayList<GenericData> Datas;
    public int InputType;
    public Class<?> ItemDataType;
    public int MaxLength;
    public long ValueId;

    public MainApplication.enumFilterItemType ItemType;

    public FilterData(String caption, String columnName, String value,
                      int InputType, int MaxLength)
    {
        this.Caption=caption;
        this.ColumnName=columnName;
        this.Value=value;
        this.CaptionTo=null;

        this.InputType=InputType;
        this.MaxLength=MaxLength;
        ItemType= MainApplication.enumFilterItemType.TEXTBOX;
    }
    public FilterData(String caption, String columnName,
                      int InputType, int MaxLength)
    {
        this.Caption=caption;
        this.ColumnName=columnName;
        this.Value="";
        this.CaptionTo=null;

        this.InputType=InputType;
        this.MaxLength=MaxLength;
        ItemType= MainApplication.enumFilterItemType.TEXTBOX;
    }

    public FilterData(String caption, String columnName, String value,
                      String captionTo, String columnNameTo, String valueTo,
                      int InputType, int MaxLength)
    {
        this.Caption=caption;
        this.ColumnName=columnName;
        this.Value=value;
        this.CaptionTo=captionTo;
        this.ColumnNameTo=columnNameTo;
        this.ValueTo=valueTo;

        this.InputType=InputType;
        this.MaxLength=MaxLength;
        ItemType= MainApplication.enumFilterItemType.TEXTBOX;
    }
    public FilterData(String caption, String columnName,
                      String captionTo, String columnNameTo,
                      int InputType, int MaxLength)
    {
        this.Caption=caption;
        this.ColumnName=columnName;
        this.Value="";
        this.CaptionTo=captionTo;
        this.ColumnNameTo=columnNameTo;
        this.ValueTo="";

        this.InputType=InputType;
        this.MaxLength=MaxLength;
        ItemType= MainApplication.enumFilterItemType.TEXTBOX;
    }

    // checkbox
    public FilterData(String groupCaption, String caption, String columnName, int value, boolean isChecked)
    {
        this.GroupCaption=groupCaption;
        this.Caption=caption;
        this.ColumnName=columnName;
        this.CheckBox=isChecked;
        this.ItemDataType=Integer.class;
        ValueId=value;
        ItemType= MainApplication.enumFilterItemType.CHECKBOX;
    }

    public FilterData(String groupCaption, String caption, String columnName, boolean isChecked)
    {
        this.GroupCaption=groupCaption;
        this.Caption=caption;
        this.ColumnName=columnName;
        this.CheckBox=isChecked;
        this.ItemDataType=Boolean.class;

        ItemType= MainApplication.enumFilterItemType.CHECKBOX;
    }

    // radiobutton/combobox
    public <T> FilterData(String caption, String columnName, int value, ArrayList<GenericData> datas,
                          MainApplication.enumFilterItemType itemType)
    {

        this.Caption=caption;
        this.ColumnName=columnName;
        this.ValueId=value;
        this.Datas=datas;
        this.ItemType= itemType;
    }
}
