package com.makaya.drd;

import android.app.Activity;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.makaya.drd.domain.MemberDepositTrx;
import com.makaya.drd.library.PublicFunction;

import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Created by xbudi on 15/11/2016.
 */

public class DepositTrxListAdapter extends
        RecyclerView.Adapter<DepositTrxListAdapter.TheViewHolder>{

    public static class TheViewHolder extends RecyclerView.ViewHolder {
        TextView trx;
        TextView type;
        TextView detail;
        TextView amount;

        View itemView;

        TheViewHolder(View itemView) {
            super(itemView);
            trx = itemView.findViewById(R.id.trx);
            type = itemView.findViewById(R.id.type);
            detail = itemView.findViewById(R.id.detail);
            amount = itemView.findViewById(R.id.amount);

            this.itemView=itemView;
        }
    }

    Activity activity;
    ArrayList<MemberDepositTrx> trxs;

    public DepositTrxListAdapter(Activity activity, ArrayList<MemberDepositTrx> trxs){
        this.trxs = trxs;
        this.activity=activity;
    }

    public void setData(ArrayList<MemberDepositTrx> trxs) {
        this.trxs = trxs;

    }

    @Override
    public int getItemCount() {
        return trxs.size();
    }

    @Override
    public TheViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.deposit_trx_list_item, viewGroup, false);

        v.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });
        TheViewHolder pvh = new TheViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(TheViewHolder theViewHolder, int i) {

        MemberDepositTrx tx=trxs.get(i);

        theViewHolder.type.setTag(tx);
        theViewHolder.trx.setText(tx.TrxNo+" | "+ PublicFunction.dateToString("dd/MM/yyyy", tx.TrxDate));
        theViewHolder.detail.setText(tx.Descr);
        theViewHolder.type.setText((tx.DbCr==0?"DB":"CR"));
        int color=Color.RED;
        if (tx.DbCr==0)
            color=Color.parseColor("#2196f3");

        theViewHolder.type.setBackgroundColor(color);
        theViewHolder.amount.setText("DRD Point " + NumberFormat.getInstance().format(tx.Amount));
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
