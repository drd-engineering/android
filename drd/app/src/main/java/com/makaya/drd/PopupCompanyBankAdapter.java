package com.makaya.drd;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.makaya.drd.domain.CompanyBank;

import java.util.ArrayList;

/**
 * Created by xbudi on 03/01/2017.
 */

public class PopupCompanyBankAdapter extends RecyclerView.Adapter<PopupCompanyBankAdapter.DataViewHolder>{
    // MY EVENT HANDLER
    private OnSelectedListener onSelectedListener;
    public interface OnSelectedListener {
        public void onSelected(CompanyBank cbc);
    }

    public void setOnSelectedListener(OnSelectedListener listener) {
        onSelectedListener = listener;
    }
    // /MY EVENT HANDLER

    Context context;
    ArrayList<CompanyBank> compbanks;
    View view;

    public class DataViewHolder extends RecyclerView.ViewHolder {
        TextView bankName;
        TextView branchOffice;
        TextView accountNo;
        TextView accountName;
        ImageView logobank;

        public DataViewHolder(View itemView) {
            super(itemView);

            bankName = (TextView)itemView.findViewById(R.id.bankName);
            branchOffice = (TextView)itemView.findViewById(R.id.branchOffice);
            accountNo = (TextView)itemView.findViewById(R.id.accountNo);
            accountName = (TextView)itemView.findViewById(R.id.accountName);
            logobank = (ImageView)itemView.findViewById(R.id.logobank);

            context=itemView.getContext();
            view=itemView;
        }
    }

    PopupCompanyBankAdapter(ArrayList<CompanyBank> compbanks){
        this.compbanks = compbanks;
    }

    @Override
    public int getItemCount() {
        return compbanks.size();
    }

    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.popup_company_bank_item, viewGroup, false);

        v.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (onSelectedListener!=null) {
                    ImageView logobank = (ImageView)v.findViewById(R.id.logobank);
                    onSelectedListener.onSelected((CompanyBank)logobank.getTag());
                }
            }
        });

        DataViewHolder pvh = new DataViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(DataViewHolder dataViewHolder, int i) {

        CompanyBank cb=compbanks.get(i);

        dataViewHolder.logobank.setTag(cb);
        dataViewHolder.bankName.setText(cb.Bank.Name);
        dataViewHolder.branchOffice.setText(cb.Branch);
        dataViewHolder.accountNo.setText(cb.AccountNo);
        dataViewHolder.accountName.setText(cb.AccountName);

        if (cb.AccountNo.equals(""))
            dataViewHolder.accountNo.setVisibility(View.GONE);
        else
            dataViewHolder.accountNo.setVisibility(View.VISIBLE);

        if (cb.AccountName.equals(""))
            dataViewHolder.accountName.setVisibility(View.GONE);
        else
            dataViewHolder.accountName.setVisibility(View.VISIBLE);

        String path=MainApplication.getUrlApplWeb()+"/Images/bank/"+cb.Bank.Logo;
        Picasso.with(context)
                .load(path)
                .placeholder(R.drawable.no_picture)
                .error(R.drawable.no_picture)
                .into(dataViewHolder.logobank);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
