package com.makaya.drd;

import android.app.Activity;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.makaya.drd.domain.Bank;
import com.makaya.drd.domain.MemberAccount;

import java.util.ArrayList;

/**
 * Created by xbudi on 15/11/2016.
 */

public class MemberAccountListAdapter extends
        RecyclerView.Adapter<MemberAccountListAdapter.TheViewHolder>{

    public static class TheViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView bank;
        TextView accountNo;
        TextView accountName;
        TextView branch;

        View itemView;

        TheViewHolder(View itemView) {
            super(itemView);
            title = (TextView)itemView.findViewById(R.id.title);
            bank = (TextView)itemView.findViewById(R.id.bank);
            accountNo = (TextView)itemView.findViewById(R.id.accountNo);
            accountName = (TextView)itemView.findViewById(R.id.accountName);
            branch = (TextView)itemView.findViewById(R.id.branch);

            this.itemView=itemView;
        }
    }

    Activity activity;
    ArrayList<MemberAccount> accounts;

    MemberAccountListAdapter(Activity activity, ArrayList<MemberAccount> accounts){
        this.accounts = accounts;
        this.activity=activity;
    }

    @Override
    public int getItemCount() {
        return accounts.size();
    }

    @Override
    public TheViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.member_account_list_item, viewGroup, false);

        v.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                TextView title=(TextView)v.findViewById(R.id.title);
                Intent intent = new Intent(v.getContext(), MemberAccountActivity.class);
                intent.putExtra("Id",((MemberAccount)title.getTag()).Id);
                activity.startActivityForResult (intent,1);
            }
        });
        TheViewHolder pvh = new TheViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(TheViewHolder theViewHolder, int i) {

        MemberAccount account=accounts.get(i);
        Bank banktag=new Bank();
        banktag.Id=account.BankId;
        banktag.Name=account.Bank.Name;

        theViewHolder.title.setText(account.Title);
        theViewHolder.title.setTag(account);
        theViewHolder.accountNo.setText("Acc No: "+account.AccountNo);
        theViewHolder.accountName.setText("Name: "+account.AccountName);
        theViewHolder.bank.setText("Bank: "+account.Bank.Name);
        theViewHolder.bank.setTag(banktag);

        theViewHolder.branch.setText("Branch: "+account.Branch);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
