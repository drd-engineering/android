package com.makaya.xchat;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.makaya.xchat.domain.MessageSumDetailClass;
import com.makaya.xchat.library.PublicFunction;

import java.util.ArrayList;

/**
 * Created by xbudi on 25/10/2016.
 */

public class InboxDetailAdapter extends
        RecyclerView.Adapter<InboxDetailAdapter.InboxViewHolder>{

    public static class InboxViewHolder extends RecyclerView.ViewHolder {
        //CardView cv;
        TextView messageLeft;
        TextView messageRight;
        TextView dateLeft;
        TextView dateRight;
        LinearLayout layoutLeft;
        LinearLayout layoutRight;

        View itemView;

        InboxViewHolder(View itemView) {
            super(itemView);
            //cv = (CardView)itemView.findViewById(R.id.cv);
            messageLeft = (TextView)itemView.findViewById(R.id.messageLeft);
            messageRight = (TextView)itemView.findViewById(R.id.messageRight);
            dateLeft = (TextView)itemView.findViewById(R.id.dateLeft);
            dateRight = (TextView)itemView.findViewById(R.id.dateRight);
            layoutLeft = (LinearLayout)itemView.findViewById(R.id.layoutLeft);
            layoutRight = (LinearLayout)itemView.findViewById(R.id.layoutRight);

            this.itemView=itemView;
        }
    }

    ArrayList<MessageSumDetailClass> msgs;

    InboxDetailAdapter(ArrayList<MessageSumDetailClass> msgs){
        this.msgs = msgs;
    }

    public void setData(ArrayList<MessageSumDetailClass> msgs) {
        this.msgs = msgs;
    }

    @Override
    public int getItemCount() {
        return msgs.size();
    }

    @Override
    public InboxViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.inbox_detail_item, viewGroup, false);

        v.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                CardView cv=(CardView)v.findViewById(R.id.cv);
                //Intent intent = new Intent(v.getContext(), RentOrderDetailActivity.class);
                //v.getContext().startActivity (intent);
            }
        });
        InboxViewHolder pvh = new InboxViewHolder(v);
        return pvh;
    }


    @Override
    public void onBindViewHolder(InboxViewHolder vehicleViewHolder, int i) {

        MessageSumDetailClass msg=msgs.get(i);

        String format="dd/MM/yyyy HH:mm";
        if (PublicFunction.dateToString("yyyyMM/dd",msg.DateMessage).
                equals(PublicFunction.dateToString("yyyyMM/dd", PublicFunction.getToday())))
            format="HH:mm";

        if (msg.IsMe){
            vehicleViewHolder.layoutLeft.setVisibility(View.GONE);
            vehicleViewHolder.layoutRight.setVisibility(View.VISIBLE);
            vehicleViewHolder.messageRight.setText(msg.TextMessage);
            vehicleViewHolder.dateRight.setText(PublicFunction.dateToString(format, msg.DateMessage));
        }else{
            vehicleViewHolder.layoutRight.setVisibility(View.GONE);
            vehicleViewHolder.layoutLeft.setVisibility(View.VISIBLE);
            vehicleViewHolder.messageLeft.setText(msg.TextMessage);
            vehicleViewHolder.dateLeft.setText(PublicFunction.dateToString(format, msg.DateMessage));
        }


    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}

