package com.makaya.drd;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;


import com.makaya.drd.domain.FilterData;
import com.makaya.drd.domain.GenericData;
import com.makaya.drd.library.PublicFunction;

import java.util.ArrayList;

/**
 * Created by xbudi on 22/11/2016.
 */

public class PopupFilterDialog extends Dialog {

    // MY EVENT HANDLER
    private OnSelectedListener onSelectedListener;
    public interface OnSelectedListener {
        public void onSelected(ArrayList<FilterData> datas);
    }

    public void setOnSelectedListener(OnSelectedListener listener) {
        onSelectedListener = listener;
    }
    // /MY EVENT HANDLER

    Dialog dialog;
    ArrayList<FilterData> datas;
    Activity activity;
    RecyclerView rv;

    public PopupFilterDialog(Activity activity, ArrayList<FilterData> datas)
    {
        super(activity);
        this.datas=datas;
        this.activity=activity;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_filter);
        dialog=this;

        Button btnClose=(Button)dialog.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PublicFunction.hideKeyboard(dialog);
                dialog.dismiss();
            }
        });
        Button btnOk=(Button)dialog.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PublicFunction.hideKeyboard(dialog);
                if (onSelectedListener!=null) {
                    for(int i=0;i< datas.size();i++){
                        View vw=rv.getChildAt(i);
                        FilterData fd=datas.get(i);
                        if (fd.ItemType== MainApplication.enumFilterItemType.TEXTBOX) {
                            EditText value = (EditText) vw.findViewById(R.id.value);
                            EditText valueTo = (EditText) vw.findViewById(R.id.valueTo);
                            fd.Value = value.getText().toString();
                            if (fd.CaptionTo != null)
                                fd.ValueTo = valueTo.getText().toString();
                        }else if (fd.ItemType== MainApplication.enumFilterItemType.CHECKBOX) {
                            CheckBox checkBox=(CheckBox) vw.findViewById(R.id.checkBox);
                            fd.CheckBox=checkBox.isChecked();
                        }else if (fd.ItemType== MainApplication.enumFilterItemType.RADIOBUTTON) {
                            RadioGroup radioGroup=(RadioGroup) vw.findViewById(R.id.radioGroup);
                            for(int x=0;x<radioGroup.getChildCount();x++)
                            {
                                RadioButton radio=(RadioButton)radioGroup.getChildAt(x);
                                if (radio.isChecked())
                                {
                                    fd.ValueId=((GenericData)radio.getTag()).Id;
                                    break;
                                }
                            }
                        }else if (fd.ItemType== MainApplication.enumFilterItemType.DROPDOWNLIST) {
                            Spinner spinner=(Spinner) vw.findViewById(R.id.spinner);
                            fd.ValueId=((GenericData)spinner.getTag()).Id;
                        }
                    }
                    onSelectedListener.onSelected(datas);
                }
                dialog.dismiss();
            }
        });

        bindRecyclerView();
    }

    private void bindRecyclerView()
    {
        rv = (RecyclerView)findViewById(R.id.rv);
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(activity);
        rv.setLayoutManager(llm);

        PopupFilterAdapter adapter = new PopupFilterAdapter(datas, dialog);
        rv.setAdapter(adapter);
        /*adapter.setOnSelectedListener(new PopupFilterAdapter.OnSelectedListener() {
            @Override
            public void onSelected(ArrayList<FilterDataClass> news) {
                if (onSelectedListener!=null)
                    onSelectedListener.onSelected(news);

                dialog.dismiss();
            }
        });*/

    }
}
