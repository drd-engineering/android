package com.makaya.drd;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.makaya.drd.domain.GenericData;

import java.util.ArrayList;

/**
 * Created by xbudi on 28/09/2016.
 */
public class PopupDialogList {
    Dialog dialog;
    View view;
    int fieldId=0;

    // MY EVENT HANDLER
    private OnSelectedListener onSelectedListener;
    public interface OnSelectedListener {
        public void onSelected(Object data);
    }

    public void setOnSelectedListener(OnSelectedListener listener) {
        onSelectedListener = listener;
    }
    // /MY EVENT HANDLER

    PopupDialogList(View view, int fieldId)
    {
        this.view=view;
        this.fieldId=fieldId;

        dialog = new Dialog(view.getContext());
    }

    public void Show(int templateId) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(templateId);

        dialog.show();
    }

    public <T> void Show(int templateId, ArrayList<T> items) {

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(templateId);

        final EditText field = (EditText) view.findViewById(fieldId);
        if (items.size() > 0)
            bindDataList(dialog, field, items);

        dialog.show();
    }

    public <T> void BindData(ArrayList<T> items) {
        ProgressBar pb = (ProgressBar) dialog.findViewById(R.id.progressBar);
        EditText field = (EditText) view.findViewById(fieldId);
        if (items.size() > 0)
            bindDataList(dialog, field, items);
        pb.setVisibility(View.GONE);
    }

    private <T> void bindDataList(final Dialog d, final EditText field, final ArrayList<T> datas)
    {
        PopupListAdapter<T> itemAdapter =
                new PopupListAdapter<T>(d.getContext(), android.R.layout.simple_list_item_1, datas);

        ListView listView = (ListView) d.findViewById(R.id.dataList);
        listView.setAdapter(itemAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                boolean ischanged= field.getTag() == null || !field.getTag().equals(view.getTag());
                field.setText(((TextView) view).getText());
                field.setTag(view.getTag());
                d.dismiss();
                if (onSelectedListener!=null && ischanged)
                    onSelectedListener.onSelected(view.getTag());
            }
        });

    }

    public class PopupListAdapter<T> extends ArrayAdapter<T> {

        Context context;
        int layoutResourceId;
        ArrayList<T> datas = null;

        public PopupListAdapter(Context context, int layoutResourceId, ArrayList<T> datas) {
            super(context, layoutResourceId, datas);

            this.context = context;
            this.layoutResourceId = layoutResourceId;
            this.datas = datas;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;

            if (row == null) {
                row = LayoutInflater.from(getContext()).inflate(layoutResourceId, parent, false);
            }
            TextView text = (TextView) row;

            GenericData item = new GenericData();
            item = (GenericData) datas.get(position);
            text.setText(item.Descr);
            text.setTag(item);

            return row;
        }
    }
}
