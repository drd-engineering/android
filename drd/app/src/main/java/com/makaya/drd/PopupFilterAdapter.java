package com.makaya.drd;

import android.app.Dialog;
import android.content.Context;
import com.google.android.material.textfield.TextInputLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;


import com.makaya.drd.domain.FilterData;
import com.makaya.drd.domain.GenericData;

import java.util.ArrayList;

/**
 * Created by xbudi on 22/11/2016.
 */

public class PopupFilterAdapter extends
        RecyclerView.Adapter<PopupFilterAdapter.FilterViewHolder>{
/*

    // MY EVENT HANDLER
    private OnSelectedListener onSelectedListener;
    public interface OnSelectedListener {
        public void onSelected(ArrayList<FilterDataClass> news);
    }

    public void setOnSelectedListener(OnSelectedListener listener) {
        onSelectedListener = listener;
    }
    // /MY EVENT HANDLER
*/

    Dialog dialog;
    //Activity activity;
    ArrayList<FilterData> datas;


    public static class FilterViewHolder extends RecyclerView.ViewHolder {
        View itemView;

        //root
        LinearLayout rootLayout;

        //textbox
        EditText value;
        EditText valueTo;
        TextInputLayout layoutValue;
        TextInputLayout layoutValueTo;
        LinearLayout inputLayout;

        //checkbox
        TextView checkboxTitle;
        CheckBox checkBox;
        LinearLayout checkLayout;

        //radio button
        RadioGroup radioGroup;
        TextView radioTitle;
        LinearLayout radioLayout;

        //combobox
        TextView comboTitle;
        Spinner spinner;
        LinearLayout comboLayout;

        FilterViewHolder(View itemView) {
            super(itemView);
            this.itemView=itemView;

            //root
            rootLayout= (LinearLayout)itemView.findViewById(R.id.rootLayout);
            rootLayout.setVisibility(View.GONE);

            //textbox
            value = (EditText)itemView.findViewById(R.id.value);
            valueTo = (EditText)itemView.findViewById(R.id.valueTo);
            layoutValueTo = (TextInputLayout)itemView.findViewById(R.id.columnTo);
            layoutValue = (TextInputLayout)itemView.findViewById(R.id.column);
            inputLayout = (LinearLayout)itemView.findViewById(R.id.inputLayout);
            inputLayout.setVisibility(View.GONE);

            //checkbox
            checkboxTitle= (TextView)itemView.findViewById(R.id.checkboxTitle);
            checkBox= (CheckBox)itemView.findViewById(R.id.checkBox);
            checkLayout= (LinearLayout)itemView.findViewById(R.id.checkLayout);
            checkLayout.setVisibility(View.GONE);

            //radio button
            radioGroup= (RadioGroup) itemView.findViewById(R.id.radioGroup);
            radioTitle= (TextView)itemView.findViewById(R.id.radioTitle);
            radioLayout= (LinearLayout)itemView.findViewById(R.id.radioLayout);
            radioLayout.setVisibility(View.GONE);

            //combobox
            comboTitle= (TextView)itemView.findViewById(R.id.comboTitle);
            spinner= (Spinner)itemView.findViewById(R.id.spinner);
            comboLayout= (LinearLayout)itemView.findViewById(R.id.comboLayout);
            comboLayout.setVisibility(View.GONE);
        }
    }

    PopupFilterAdapter(ArrayList<FilterData> datas, Dialog pdialog){
        this.datas = datas;
        //this.activity=activity;
        this.dialog=pdialog;
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    @Override
    public FilterViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.popup_filter_item, viewGroup, false);

        v.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
            }
        });
        FilterViewHolder pvh = new FilterViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(FilterViewHolder filterViewHolder, int i) {
        FilterData filter=datas.get(i);

        if (filter.ItemType== MainApplication.enumFilterItemType.TEXTBOX) {
            filterViewHolder.inputLayout.setVisibility(View.VISIBLE);

            filterViewHolder.value.setText(filter.Value);
            filterViewHolder.value.setHint("");
            filterViewHolder.layoutValue.setHint(filter.Caption);
            filterViewHolder.value.setInputType(filter.InputType);
            filterViewHolder.value.setFilters(new InputFilter[]{new InputFilter.LengthFilter(filter.MaxLength)});

            if (filter.CaptionTo == null) {
                filterViewHolder.layoutValueTo.setVisibility(View.GONE);
            } else {
                filterViewHolder.valueTo.setText(filter.ValueTo);
                filterViewHolder.valueTo.setHint("");
                filterViewHolder.layoutValueTo.setHint(filter.CaptionTo);
                filterViewHolder.valueTo.setInputType(filter.InputType);
                filterViewHolder.valueTo.setFilters(new InputFilter[]{new InputFilter.LengthFilter(filter.MaxLength)});
            }
        }else if (filter.ItemType== MainApplication.enumFilterItemType.CHECKBOX) {
            filterViewHolder.checkLayout.setVisibility(View.VISIBLE);
            filterViewHolder.checkboxTitle.setText(filter.GroupCaption);
            filterViewHolder.checkBox.setText(filter.Caption);
            filterViewHolder.checkBox.setChecked(filter.CheckBox);
            if (filter.GroupCaption.equals(""))
                filterViewHolder.checkboxTitle.setVisibility(View.GONE);

        }else if (filter.ItemType== MainApplication.enumFilterItemType.RADIOBUTTON) {
            filterViewHolder.radioLayout.setVisibility(View.VISIBLE);
            filterViewHolder.radioTitle.setText(filter.Caption);
            if (filter.Caption.equals(""))
                filterViewHolder.radioTitle.setVisibility(View.GONE);

            int selectedId=0;
            for (GenericData gen : filter.Datas){
                RadioButton radio=new RadioButton(dialog.getContext());
                radio.setText(gen.Descr);
                radio.setTag(gen);
                filterViewHolder.radioGroup.addView(radio);

                if(gen.Id==filter.ValueId)
                    selectedId=radio.getId();
            }
            if (selectedId!=0)
                filterViewHolder.radioGroup.check(selectedId);

        }else if (filter.ItemType== MainApplication.enumFilterItemType.DROPDOWNLIST) {
            filterViewHolder.comboLayout.setVisibility(View.VISIBLE);
            filterViewHolder.comboTitle.setText(filter.Caption);
            if (filter.Caption.equals(""))
                filterViewHolder.comboTitle.setVisibility(View.GONE);

            GenericDataAdapter adapter=new GenericDataAdapter(dialog.getContext(),
                    R.layout.textview_item1,R.layout.spinner_dropdown_item1, filter.Datas, filter.ValueId);
            filterViewHolder.spinner.setAdapter(adapter);
            if (filter.ValueId!=0) {
                int x=0;
                for (GenericData gen : filter.Datas){
                    if (gen.Id==filter.ValueId) {
                        filterViewHolder.spinner.setTag(gen);
                        break;
                    }
                    x++;
                }
                filterViewHolder.spinner.setSelection(x);
            }
            filterViewHolder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    TextView text=(TextView)view.findViewById(R.id.text1);
                    parent.setTag(text.getTag());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }
        filterViewHolder.rootLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class GenericDataAdapter extends ArrayAdapter<GenericData>{

        ArrayList<GenericData> datas;
        Context context;
        int layoutResourceId;
        int ddlayoutResourceId;
        long valueId;

        public GenericDataAdapter(Context context, int layoutResourceId, int ddlayoutResourceId,
                                  ArrayList<GenericData> datas, long valueId)
        {
            super(context, layoutResourceId, ddlayoutResourceId, datas);

            this.context=context;
            this.layoutResourceId=layoutResourceId;
            this.ddlayoutResourceId=ddlayoutResourceId;
            this.datas=datas;
            this.valueId=valueId;
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null)
                convertView = LayoutInflater.from(getContext()).inflate(layoutResourceId, null);

            GenericData item=datas.get(position);

            TextView text=(TextView)convertView.findViewById(R.id.text1);
            text.setText(item.Descr);
            text.setTag(item);

            return convertView;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            if(convertView == null)
                convertView = LayoutInflater.from(getContext()).inflate(ddlayoutResourceId, null);

            TextView text=(TextView)convertView.findViewById(R.id.text1);

            GenericData item=datas.get(position);
            text.setText(item.Descr);
            text.setTag(item);

            return convertView;
        }
    }

}
