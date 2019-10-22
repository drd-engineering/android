package com.makaya.drd;

import android.app.Activity;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.makaya.drd.domain.MemberTopupDeposit;
import com.makaya.drd.library.PublicFunction;

import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Created by xbudi on 11/11/2016.
 */

public class MemberTopupDepositListAdapter extends
        RecyclerView.Adapter<MemberTopupDepositListAdapter.TheViewHolder>{

    Activity activity;

    public static class TheViewHolder extends RecyclerView.ViewHolder {
        //LinearLayout cv;
        TextView topup;
        TextView amount;
        TextView detail;
        TextView account;
        TextView status;
        TextView payMethod;
        Button btnPayment;
        LinearLayout layoutAccount;
        LinearLayout layoutMethod;

        View itemView;

        TheViewHolder(View itemView) {
            super(itemView);
            //cv = (LinearLayout)itemView.findViewById(R.id.cv);
            topup = (TextView)itemView.findViewById(R.id.topup);
            amount = (TextView)itemView.findViewById(R.id.amount);
            detail = (TextView)itemView.findViewById(R.id.detail);
            account = (TextView)itemView.findViewById(R.id.account);
            status = (TextView)itemView.findViewById(R.id.status);
            payMethod = (TextView)itemView.findViewById(R.id.payMethod);
            layoutAccount = (LinearLayout)itemView.findViewById(R.id.layoutAccount);
            layoutMethod = (LinearLayout)itemView.findViewById(R.id.layoutMethod);
            btnPayment = (Button)itemView.findViewById(R.id.btnPayment);
            this.itemView=itemView;
        }
    }

    ArrayList<MemberTopupDeposit> topups;

    MemberTopupDepositListAdapter(ArrayList<MemberTopupDeposit> topups, Activity activity){
        this.topups = topups;
        this.activity=activity;
    }

    public void setData(ArrayList<MemberTopupDeposit> topups) {
        this.topups = topups;

    }
    @Override
    public int getItemCount() {
        return topups.size();
    }

    @Override
    public TheViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.member_topup_deposit_list_item, viewGroup, false);

        v.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                TextView status=(TextView)v.findViewById(R.id.status);
                MemberTopupDeposit topup=(MemberTopupDeposit)status.getTag();
                if (topup.PaymentStatus.equals("00")){
                    Intent intent = new Intent(activity, PaymentActivity.class);
                    intent.putExtra("TrxKeyId",topup.KeyId);
                    intent.putExtra("TrxId",(long)topup.Id);
                    intent.putExtra("TrxNo",topup.TopupNo);
                    intent.putExtra("TrxDate", PublicFunction.dateToString("dd/MM/yyyy",topup.TopupDate));
                    intent.putExtra("TrxAmount",topup.Amount);
                    intent.putExtra("TrxType","MTU");
                    intent.putExtra("TrxDescr","Member Topup Deposit");
                    intent.putExtra("TrxPayMethod",activity.getString(R.string.api_topup_paymethodtype));

                    activity.startActivityForResult (intent, 1);
                }else
                {
                    Toast.makeText(activity,"The status already "+topup.PaymentStatusDescr,Toast.LENGTH_SHORT).show();
                }
            }
        });
        TheViewHolder pvh = new TheViewHolder(v);
        return pvh;
    }


    @Override
    public void onBindViewHolder(TheViewHolder theViewHolder, int i) {

        MemberTopupDeposit topup=topups.get(i);
        theViewHolder.status.setTag(topup);
        theViewHolder.btnPayment.setTag(topup);
        theViewHolder.topup.setText(topup.TopupNo+" | "+PublicFunction.dateToString("dd/MM/yyyy", topup.TopupDate));
        theViewHolder.status.setText(topup.PaymentStatusDescr);
        theViewHolder.status.setBackgroundColor(MainApplication.generateColor(topup.PaymentStatus));
        theViewHolder.amount.setText("DRD Point " + NumberFormat.getInstance().format(topup.Amount));

        if ( topup.MemberTopupPayments.size()>0 && topup.MemberTopupPayments.get(0).MemberAccountId!=0) {
            theViewHolder.layoutAccount.setVisibility(View.VISIBLE);
            theViewHolder.account.setText(topup.MemberTopupPayments.get(0).MemberAccount.Bank.Name+" | "+
                    topup.MemberTopupPayments.get(0).MemberAccount.AccountNo + "\n" + topup.MemberTopupPayments.get(0).MemberAccount.AccountName);
        }else
            theViewHolder.layoutAccount.setVisibility(View.GONE);

        if ( topup.MemberTopupPayments.size()>0 && topup.MemberTopupPayments.get(0).CompanyBankId!=0) {
            theViewHolder.layoutMethod.setVisibility(View.VISIBLE);
            if (!topup.MemberTopupPayments.get(0).CompanyBank.AccountNo.equals(""))
                theViewHolder.payMethod.setText(topup.MemberTopupPayments.get(0).CompanyBank.PaymentMethod.Name+"\n"+
                        topup.MemberTopupPayments.get(0).CompanyBank.AccountName+"\n" +
                        topup.MemberTopupPayments.get(0).CompanyBank.AccountNo+" | "+
                        topup.MemberTopupPayments.get(0).CompanyBank.Bank.Name);
            else
                theViewHolder.payMethod.setText(topup.MemberTopupPayments.get(0).CompanyBank.PaymentMethod.Name);
        }else
            theViewHolder.layoutMethod.setVisibility(View.GONE);

        if (topup.PaymentStatus.equals("00")) {
            //theViewHolder.btnPayment.setVisibility(View.VISIBLE);
            theViewHolder.btnPayment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MemberTopupDeposit topup=(MemberTopupDeposit)v.getTag();
                    Intent intent = new Intent(activity, PaymentActivity.class);
                    intent.putExtra("TrxId",(long)topup.Id);
                    intent.putExtra("TrxNo",topup.TopupNo);
                    intent.putExtra("TrxDate", PublicFunction.dateToString("dd/MM/yyyy",topup.TopupDate));
                    intent.putExtra("TrxAmount",topup.Amount);
                    intent.putExtra("TrxType","MTU");
                    intent.putExtra("TrxDescr","Member Topup Deposit");
                    intent.putExtra("TrxPayMethod",activity.getString(R.string.api_topup_paymethodtype));

                    activity.startActivityForResult (intent, 1);
                }
            });
        }/*else
            theViewHolder.btnPayment.setVisibility(View.GONE);*/
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    private void vallPaymentMethod()
    {

    }
}
