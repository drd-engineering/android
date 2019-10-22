package com.makaya.drd;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.makaya.drd.domain.DataList;

import java.util.ArrayList;


/**
 * Created by xbudi on 28/09/2016.
 */
public class PopupListAdapter  extends
        RecyclerView.Adapter<PopupListAdapter.ListViewHolder>{

    // MY EVENT HANDLER
    private OnSelectedListener onSelectedListener;
    public interface OnSelectedListener {
        public void onSelected(DataList data);
    }

    public void setOnSelectedListener(OnSelectedListener listener) {
        onSelectedListener = listener;
    }
    // /MY EVENT HANDLER

    public class ListViewHolder extends RecyclerView.ViewHolder {
        TextView text;

        View itemView;

        ListViewHolder(View itemView) {
            super(itemView);
            text = (TextView)itemView.findViewById(R.id.text);

            this.itemView=itemView;
        }
    }

    ArrayList<DataList> datas;

    PopupListAdapter(ArrayList<DataList> datas){
        this.datas = datas;
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_data_item, viewGroup, false);

        v.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (onSelectedListener!=null) {
                    TextView text=(TextView)v.findViewById(R.id.text);
                    onSelectedListener.onSelected((DataList)text.getTag());
                }
            }
        });
        ListViewHolder pvh = new ListViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(ListViewHolder viewHolder, int i) {
        DataList data=datas.get(i);
        viewHolder.text.setText(data.Text);
        viewHolder.text.setTag(data);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
