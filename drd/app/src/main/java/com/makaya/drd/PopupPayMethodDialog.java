package com.makaya.drd;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Window;

import com.makaya.drd.domain.PaymentMethod;

import java.util.ArrayList;

/**
 * Created by xbudi on 22/11/2016.
 */

public class PopupPayMethodDialog extends Dialog {

    // MY EVENT HANDLER
    private OnSelectedListener onSelectedListener;
    public interface OnSelectedListener {
        public void onSelected(PaymentMethod method);
    }

    public void setOnSelectedListener(OnSelectedListener listener) {
        onSelectedListener = listener;
    }
    // /MY EVENT HANDLER

    Dialog dialog;
    ArrayList<PaymentMethod> paymethods;
    Activity activity;
    RecyclerView rv;

    public PopupPayMethodDialog(Activity activity, ArrayList<PaymentMethod> paymethods)
    {
        super(activity);
        this.paymethods=paymethods;
        this.activity=activity;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_pay_method);
        dialog=this;

        bindRecyclerView();
    }

    private void bindRecyclerView()
    {
        rv = (RecyclerView)findViewById(R.id.rv);
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(activity);
        rv.setLayoutManager(llm);

        PopupPayMethodAdapter adapter = new PopupPayMethodAdapter(paymethods);
        rv.setAdapter(adapter);
        adapter.setOnSelectedListener(new PopupPayMethodAdapter.OnSelectedListener() {
            @Override
            public void onSelected(PaymentMethod method) {
                if (onSelectedListener!=null) {
                    onSelectedListener.onSelected(method);
                    dismiss();
                }
            }
        });

    }
}
