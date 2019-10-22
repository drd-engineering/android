package com.makaya.drd;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Window;

import com.makaya.drd.domain.CompanyBank;

import java.util.ArrayList;

/**
 * Created by xbudi on 22/11/2016.
 */

public class PopupCompanyBankDialog extends Dialog {

    // MY EVENT HANDLER
    private OnSelectedListener onSelectedListener;
    public interface OnSelectedListener {
        public void onSelected(CompanyBank cbc);
    }

    public void setOnSelectedListener(OnSelectedListener listener) {
        onSelectedListener = listener;
    }
    // /MY EVENT HANDLER

    Dialog dialog;
    ArrayList<CompanyBank> compbanks;
    Activity activity;
    RecyclerView rv;

    public PopupCompanyBankDialog(Activity activity, ArrayList<CompanyBank> compbanks)
    {
        super(activity);
        this.compbanks=compbanks;
        this.activity=activity;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_company_bank);
        dialog=this;

        bindRecyclerView();
    }

    private void bindRecyclerView()
    {
        rv = (RecyclerView)findViewById(R.id.rv);
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(activity);
        rv.setLayoutManager(llm);

        PopupCompanyBankAdapter adapter = new PopupCompanyBankAdapter(compbanks);
        adapter.setOnSelectedListener(new PopupCompanyBankAdapter.OnSelectedListener() {
            @Override
            public void onSelected(CompanyBank cbc) {
                if (onSelectedListener!=null) {
                    onSelectedListener.onSelected(cbc);
                    dismiss();
                }
            }
        });
        rv.setAdapter(adapter);
    }
}
