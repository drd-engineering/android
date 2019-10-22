package com.makaya.drd;

import android.app.Activity;
import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.makaya.drd.domain.CompanyBank;
import com.makaya.drd.domain.PaymentMethod;

import java.util.ArrayList;

/**
 * Created by xbudi on 03/01/2017.
 */

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.DataViewHolder>{
    /*// MY EVENT HANDLER
    private OnSelectedListener onSelectedListener;
    public interface OnSelectedListener {
        public void onSelected(PaymentMethod method);
    }

    public void setOnSelectedListener(OnSelectedListener listener) {
        onSelectedListener = listener;
    }
    // /MY EVENT HANDLER*/

    Context context;
    ArrayList<PaymentMethod> paymethods;
    Activity activity;
    View view;

    public class DataViewHolder extends RecyclerView.ViewHolder {
        TextView methodName;
        RecyclerView rv;
        View separator;

        public DataViewHolder(View itemView) {
            super(itemView);

            methodName = itemView.findViewById(R.id.methodName);
            rv = itemView.findViewById(R.id.rv2);
            separator = itemView.findViewById(R.id.separator);

            context=itemView.getContext();
            view=itemView;
        }
    }

    PaymentAdapter(Activity activity, ArrayList<PaymentMethod> paymethods){
        this.paymethods = paymethods;
        this.activity=activity;
    }

    @Override
    public int getItemCount() {
        return paymethods.size();
    }

    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.payment_content_item, viewGroup, false);

        v.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
            }
        });

        DataViewHolder pvh = new DataViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(DataViewHolder dataViewHolder, int i) {

        PaymentMethod cb=paymethods.get(i);

        dataViewHolder.methodName.setTag(cb);
        dataViewHolder.methodName.setText(cb.Name);

        for(CompanyBank cmp:cb.CompanyBanks){
            cmp.PaymentMethod.Code=cb.Code;
            cmp.PaymentMethod.ConfirmType=cb.ConfirmType;
            cmp.PaymentMethod.UsingType=cb.UsingType;
        }

        bindRecyclerView(activity,dataViewHolder.rv, cb.CompanyBanks);

        if (i==getItemCount()-1)
            dataViewHolder.separator.setVisibility(View.GONE);
        else
            dataViewHolder.separator.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    private void bindRecyclerView(final Activity activity, RecyclerView rv, ArrayList<CompanyBank> companyBanks)
    {
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(activity);
        rv.setLayoutManager(llm);

        PaymentAdapter2 adapter = new PaymentAdapter2(companyBanks);
        rv.setAdapter(adapter);
        adapter.setOnSelectedListener(new PaymentAdapter2.OnSelectedListener() {
            @Override
            public void onSelected(CompanyBank method) {
                ((PaymentActivity)activity).doContinue(method);
            }
        });
    }

}
