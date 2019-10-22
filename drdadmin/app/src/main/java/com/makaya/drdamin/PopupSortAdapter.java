package com.makaya.drdamin;

import android.app.Dialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;


import com.makaya.drdamin.domain.SortData;

import java.util.ArrayList;

/**
 * Created by xbudi on 22/11/2016.
 */

public class PopupSortAdapter extends
        RecyclerView.Adapter<PopupSortAdapter.SortViewHolder>{

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
    //Activity activity;
    ArrayList<SortData> datas;
    SortData current;
    RadioButton lastCheckedRB = null;
    RadioButton rb;

    public static class SortViewHolder extends RecyclerView.ViewHolder {
        RadioButton rb;
        View itemView;

        SortViewHolder(View itemView) {
            super(itemView);
            rb = (RadioButton)itemView.findViewById(R.id.rb);
            this.itemView=itemView;
        }
    }

    PopupSortAdapter(ArrayList<SortData> datas, SortData current, Dialog dialog){
        this.datas = datas;
        //this.activity=activity;
        this.dialog=dialog;
        this.current=current;
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    @Override
    public SortViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.popup_sort_item, viewGroup, false);

        v.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
            }
        });
        SortViewHolder pvh = new SortViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(SortViewHolder sortViewHolder, int i) {

        sortViewHolder.rb.setTag(datas.get(i));
        sortViewHolder.rb.setText(datas.get(i).Title);
        sortViewHolder.rb.setChecked(datas.get(i).equals(current));
        sortViewHolder.rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                RadioButton checked_rb = (RadioButton) buttonView;
                if(lastCheckedRB != null)
                    lastCheckedRB.setChecked(false);

                lastCheckedRB = checked_rb;

                if (onSelectedListener!=null)
                    onSelectedListener.onSelected((SortData) lastCheckedRB.getTag());
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
