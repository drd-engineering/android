package com.makaya.drd;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.makaya.drd.domain.Generic;

import java.util.ArrayList;

/**
 * Created by xbudi on 28/09/2016.
 */
public class PopupGenericList {
    Dialog dialog;
    Activity activity;

    // MY EVENT HANDLER
    private OnSelectedListener onSelectedListener;
    public interface OnSelectedListener {
        public void onSelected(int idx);
    }

    public void setOnSelectedListener(OnSelectedListener listener) {
        onSelectedListener = listener;
    }
    // /MY EVENT HANDLER

    public PopupGenericList(Activity activity, int templateId)
    {
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(templateId);
    }

    public void Show() {
        ProgressBar pb = (ProgressBar) dialog.findViewById(R.id.progressBar);
        ListView lv = (ListView) dialog.findViewById(R.id.dataList);
        lv.setVisibility(View.GONE);
        pb.setVisibility(View.VISIBLE);
        dialog.show();
    }

    public void BindData(ArrayList<Generic> items) {
        ProgressBar pb = (ProgressBar) dialog.findViewById(R.id.progressBar);
        ListView lv = (ListView) dialog.findViewById(R.id.dataList);
        if (items.size() > 0)
            bindDataList(dialog, items);
        pb.setVisibility(View.GONE);
        lv.setVisibility(View.VISIBLE);
    }

    private <T> void bindDataList(final Dialog d, final ArrayList<Generic> datas)
    {
        ItemAdapter itemAdapter =
                new ItemAdapter(d.getContext(), android.R.layout.simple_list_item_1, datas);

        ListView listView = (ListView) d.findViewById(R.id.dataList);
        listView.setAdapter(itemAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                d.dismiss();
                if (onSelectedListener!=null)
                    onSelectedListener.onSelected(position);
            }
        });

    }

    public class ItemAdapter extends ArrayAdapter<Generic> {

        Context context;
        int layoutResourceId;
        ArrayList<Generic> datas;

        public ItemAdapter(Context context, int layoutResourceId, ArrayList<Generic> datas) {
            super(context, layoutResourceId, datas);

            this.context = context;
            this.layoutResourceId = layoutResourceId;
            this.datas = datas;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;

            if (row == null) {
                row = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            }
            TextView text = (TextView) row;

            Generic item = new Generic();
            item = (Generic) datas.get(position);
            text.setText(item.Descr);

            return row;
        }
    }
}
