package com.makaya.drdamin;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.makaya.drdamin.domain.DataList;

import java.util.ArrayList;

/**
 * Created by xbudi on 03/03/2017.
 */

public class PopupListDialog extends Dialog {

    // MY EVENT HANDLER
    private OnSelectedListener onSelectedListener;
    public interface OnSelectedListener {
        public void onSelected(DataList data);
    }

    public void setOnSelectedListener(OnSelectedListener listener) {
        onSelectedListener = listener;
    }
    // /MY EVENT HANDLER

    Dialog dialog;
    ArrayList<DataList> datas;
    Activity activity;
    View view;
    boolean isCode;

    public PopupListDialog(Activity activity, View view, ArrayList<DataList> datas)
    {
        super(activity);
        this.datas=datas;
        this.activity=activity;
        this.view=view;
        this.isCode=false;
    }

    public PopupListDialog(Activity activity, View view, ArrayList<DataList> datas, boolean isCode)
    {
        super(activity);
        this.datas=datas;
        this.activity=activity;
        this.view=view;
        this.isCode=isCode;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.list_data);
        dialog=this;
        bindRecyclerView();
    }

    private void bindRecyclerView()
    {
        RecyclerView rv = (RecyclerView)findViewById(R.id.rv);
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(activity);
        rv.setLayoutManager(llm);

        PopupListAdapter adapter = new PopupListAdapter(datas);
        rv.setAdapter(adapter);

        adapter.setOnSelectedListener(new PopupListAdapter.OnSelectedListener() {
            @Override
            public void onSelected(DataList data) {
                if (view!=null) {
                    view.setTag(data);
                    if (isCode)
                        ((EditText) view).setText(data.Code);
                    else
                        ((EditText) view).setText(data.Text);
                }
                if (onSelectedListener!=null)
                    onSelectedListener.onSelected(data);

                dialog.dismiss();
            }
        });

    }



}
