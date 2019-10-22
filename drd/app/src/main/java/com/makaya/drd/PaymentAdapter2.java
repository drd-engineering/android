package com.makaya.drd;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.makaya.drd.domain.CompanyBank;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by xbudi on 03/01/2017.
 */

public class PaymentAdapter2 extends RecyclerView.Adapter<PaymentAdapter2.DataViewHolder>{
    // MY EVENT HANDLER
    private OnSelectedListener onSelectedListener;
    public interface OnSelectedListener {
        public void onSelected(CompanyBank method);
    }

    public void setOnSelectedListener(OnSelectedListener listener) {
        onSelectedListener = listener;
    }
    // /MY EVENT HANDLER

    Context context;
    ArrayList<CompanyBank> companyBanks;
    View view;

    public class DataViewHolder extends RecyclerView.ViewHolder {
        TextView payName;
        TextView payDescr;
        ImageView payLogo;
        View separator;

        public DataViewHolder(View itemView) {
            super(itemView);

            payName = (TextView)itemView.findViewById(R.id.payName);
            payDescr = (TextView)itemView.findViewById(R.id.payDescr);
            payLogo = (ImageView)itemView.findViewById(R.id.payLogo);
            separator = (View)itemView.findViewById(R.id.separator);

            context=itemView.getContext();
            view=itemView;
        }
    }

    PaymentAdapter2(ArrayList<CompanyBank> paymethods){
        this.companyBanks = paymethods;
    }

    @Override
    public int getItemCount() {
        return companyBanks.size();
    }

    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.payment_content_item2, viewGroup, false);

        v.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (onSelectedListener!=null)
                    onSelectedListener.onSelected((CompanyBank)v.findViewById(R.id.payLogo).getTag());
            }
        });

        DataViewHolder pvh = new DataViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(DataViewHolder dataViewHolder, int i) {

        CompanyBank cb=companyBanks.get(i);

        dataViewHolder.payLogo.setTag(cb);
        dataViewHolder.payName.setText(cb.Bank.Name);

        String path=MainApplication.getUrlApplWeb()+"/Images/bank/"+cb.Bank.Logo;
        Picasso.with(context)
                .load(path)
                .placeholder(R.drawable.no_picture)
                .error(R.drawable.no_picture)
                .into(dataViewHolder.payLogo);

        /*if (i==getItemCount()-1)
            dataViewHolder.separator.setVisibility(View.GONE);
        else
            dataViewHolder.separator.setVisibility(View.VISIBLE);*/
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
