package com.makaya.drdamin;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;


import com.makaya.drdamin.domain.SortData;

import java.util.ArrayList;

/**
 * Created by xbudi on 22/11/2016.
 */

public class PopupSortDialog extends Dialog {

    // MY EVENT HANDLER
    private OnSelectedListener onSelectedListener;
    public interface OnSelectedListener {
        public void onSelected(SortData data);
    }

    public void setOnSelectedListener(OnSelectedListener listener) {
        onSelectedListener = listener;
    }
    // /MY EVENT HANDLER

    Dialog dialog;
    ArrayList<SortData> datas;
    SortData current;
    Activity activity;

    public PopupSortDialog(Activity activity, ArrayList<SortData> datas, SortData current)
    {
        super(activity);
        this.datas=datas;
        this.current=current;
        this.activity=activity;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_sort);
        dialog=this;
        bindRecyclerView();
    }

    private void bindRecyclerView()
    {
        RecyclerView rv = (RecyclerView)findViewById(R.id.rv);
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(activity);
        rv.setLayoutManager(llm);

        PopupSortAdapter adapter = new PopupSortAdapter(datas, current, dialog);
        rv.setAdapter(adapter);
        adapter.setOnSelectedListener(new PopupSortAdapter.OnSelectedListener() {
            @Override
            public void onSelected(SortData data) {
                if (onSelectedListener!=null)
                    onSelectedListener.onSelected(data);

                dialog.dismiss();
            }
        });

    }
}
