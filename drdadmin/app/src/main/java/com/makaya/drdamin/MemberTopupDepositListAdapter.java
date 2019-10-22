package com.makaya.drdamin;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.makaya.drdamin.domain.Dashboard;
import com.makaya.drdamin.domain.MemberTopupDeposit;
import com.makaya.drdamin.service.PostDataModelUrl;
import com.makaya.drdamin.service.PublicFunction;

import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Created by xbudi on 11/11/2016.
 */

public class MemberTopupDepositListAdapter extends
        RecyclerView.Adapter<MemberTopupDepositListAdapter.TheViewHolder>{

    // MY EVENT HANDLER

    private OnSetProcessListener onSetProcessListener;
    public interface OnSetProcessListener {
        public <T> void onProcess();
    }

    public void setOnProcessListener(OnSetProcessListener listener) {
        onSetProcessListener = listener;
    }
    // MY EVENT HANDLER

    Activity activity;

    public static class TheViewHolder extends RecyclerView.ViewHolder {
        //LinearLayout cv;
        TextView topup;
        TextView amount;
        TextView descr;
        //TextView detail;
        TextView account;
        TextView status;
        TextView payMethod;
        TextView payment;

        Button btnVerify;
        Button btnConfirmed;
        Button btnNotconfirmed;

        LinearLayout layoutAccount;
        LinearLayout layoutMethod;
        LinearLayout layoutButton;

        View itemView;

        TheViewHolder(View itemView) {
            super(itemView);
            //cv = (LinearLayout)itemView.findViewById(R.id.cv);
            topup = (TextView)itemView.findViewById(R.id.topup);
            amount = (TextView)itemView.findViewById(R.id.amount);
            descr = (TextView)itemView.findViewById(R.id.descr);
            //detail = (TextView)itemView.findViewById(R.id.detail);
            account = (TextView)itemView.findViewById(R.id.account);
            status = (TextView)itemView.findViewById(R.id.status);
            payMethod = (TextView)itemView.findViewById(R.id.payMethod);
            payment = (TextView)itemView.findViewById(R.id.payment);

            btnVerify= (Button)itemView.findViewById(R.id.btnVerify);
            btnConfirmed= (Button)itemView.findViewById(R.id.btnConfirmed);
            btnNotconfirmed= (Button)itemView.findViewById(R.id.btnNotconfirmed);

            layoutAccount = (LinearLayout)itemView.findViewById(R.id.layoutAccount);
            layoutMethod = (LinearLayout)itemView.findViewById(R.id.layoutMethod);
            layoutButton=(LinearLayout)itemView.findViewById(R.id.layoutButton);
            this.itemView=itemView;
        }
    }

    ArrayList<MemberTopupDeposit> topups;
    MainApplication global;
    PopupProgress popupProgress;

    MemberTopupDepositListAdapter(ArrayList<MemberTopupDeposit> topups, Activity activity){
        this.topups = topups;
        this.activity=activity;
        global=(MainApplication)activity.getApplication();
        popupProgress=new PopupProgress(activity);
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
                /*TextView status=(TextView)v.findViewById(R.id.status);
                MemberTopupDeposit topup=(MemberTopupDeposit)status.getTag();
                if (topup.PaymentStatus.equals("00")){
                    Intent intent = new Intent(activity, PaymentActivity.class);
                    intent.putExtra("TrxId",(long)topup.Id);
                    intent.putExtra("TrxNo",topup.TopupNo);
                    intent.putExtra("TrxDate", PublicFunction.dateToString("dd/MM/yyyy",topup.TopupDate));
                    intent.putExtra("TrxAmount",topup.Amount);
                    intent.putExtra("TrxType","TU");
                    intent.putExtra("TrxDescr","Klaxon Topup Deposit");

                    activity.startActivityForResult (intent, 1);
                }else
                {
                    Toast.makeText(activity,"The status already "+topup.PaymentStatusDescr,Toast.LENGTH_SHORT).show();
                }*/
            }
        });
        TheViewHolder pvh = new TheViewHolder(v);
        return pvh;
    }


    @Override
    public void onBindViewHolder(TheViewHolder theViewHolder, int i) {

        MemberTopupDeposit topup=topups.get(i);

        theViewHolder.topup.setText(topup.TopupNo+" | "+PublicFunction.dateToString("dd/MM/yyyy", topup.TopupDate));
        theViewHolder.topup.setTag(topup);
        theViewHolder.status.setTag(topup);
        theViewHolder.status.setText(topup.PaymentStatusDescr);
        theViewHolder.status.setBackgroundColor(MainApplication.generateColor(topup.PaymentStatus));
        theViewHolder.amount.setText("IDR. " + NumberFormat.getInstance().format(topup.Amount));
        theViewHolder.descr.setText(topup.MemberDescr);
        /*String detail="<u>Detail Topup:</u>";
        int x=1;
        for (MemberTopupModule ptm:topup.MemberTopupModules){
            detail+="<br/>"+x+". "+ptm.MemberModule.BusinessName+" <b>IDR. "+NumberFormat.getInstance().format(ptm.Amount)+"</b>";
            x++;
        }
        theViewHolder.detail.setText(Html.fromHtml(detail));*/

        //theViewHolder.payment.setText(topup.);

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

        if (("00,02,99").contains(topup.PaymentStatus))
            theViewHolder.layoutButton.setVisibility(View.GONE);
        else
            theViewHolder.layoutButton.setVisibility(View.VISIBLE);

        theViewHolder.btnConfirmed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogBox dialog=new DialogBox(activity);
                dialog.setOnSetDialogListener(new DialogBox.OnSetDialogListener() {
                    @Override
                    public <T> void onDialogPositive() {
                        confirmed(activity.getCurrentFocus().getRootView());
                    }

                    @Override
                    public <T> void onDialogNegative() {

                    }
                });

                dialog.show("Confirmation","Are you sure data set to confirmed?","Yes","No");
            }
        });
        theViewHolder.btnNotconfirmed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogBox dialog=new DialogBox(activity);
                dialog.setOnSetDialogListener(new DialogBox.OnSetDialogListener() {
                    @Override
                    public <T> void onDialogPositive() {
                        notconfirmed(activity.getCurrentFocus().getRootView());
                    }

                    @Override
                    public <T> void onDialogNegative() {

                    }
                });

                dialog.show("Confirmation","Are you sure data set to unconfirmed?","Yes","No");
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    void confirmed(View v)
    {
        popupProgress.show();
        TextView pay=(TextView)v.findViewById(R.id.topup);
        MemberTopupDeposit topup=(MemberTopupDeposit)pay.getTag();
        String url=String.format(activity.getResources().getString(R.string.api_memtopup_confirmed),
                global.getUrlLinkApi(),topup.Id);
        final PostDataModelUrl posData = new PostDataModelUrl();
        posData.viewTag=v;
        posData.execute(url, Dashboard.class);

        posData.setOnDataPostedListener(new PostDataModelUrl.OnSetDataPostedViewListener() {
            @Override
            public <T> void onDataPosted(T data, View v) {
                popupProgress.dismiss();
                if (onSetProcessListener!=null)
                    onSetProcessListener.onProcess();
                Toast.makeText(activity, "Confirmed data success.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public <T> void onPostedError(Exception data, View viewTag) {
                popupProgress.dismiss();
                Toast.makeText(activity, "Problem update data success. Please try again", Toast.LENGTH_SHORT).show();
            }
        });

    }

    void notconfirmed(View v)
    {
        popupProgress.show();
        TextView pay=(TextView)v.findViewById(R.id.topup);
        MemberTopupDeposit topup=(MemberTopupDeposit)pay.getTag();
        String url=String.format(activity.getResources().getString(R.string.api_memtopup_notconfirmed),
                global.getUrlLinkApi(),topup.Id);
        final PostDataModelUrl posData = new PostDataModelUrl();
        posData.viewTag=v;
        posData.execute(url, Dashboard.class);

        posData.setOnDataPostedListener(new PostDataModelUrl.OnSetDataPostedViewListener() {
            @Override
            public <T> void onDataPosted(T data, View v) {
                popupProgress.dismiss();
                if (onSetProcessListener!=null)
                    onSetProcessListener.onProcess();
                Toast.makeText(activity, "Not confirmed data success.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public <T> void onPostedError(Exception data, View viewTag) {
                popupProgress.dismiss();
                Toast.makeText(activity, "Problem update data success. Please try again", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
