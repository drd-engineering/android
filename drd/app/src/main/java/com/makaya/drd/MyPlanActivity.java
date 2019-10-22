package com.makaya.drd;

import android.app.Activity;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.makaya.drd.domain.MemberLogin;
import com.makaya.drd.domain.MemberPlan;
import com.makaya.drd.library.PublicFunction;
import com.makaya.drd.service.MemberService;

import java.text.NumberFormat;

public class MyPlanActivity extends AppCompatActivity {
    MainApplication global;
    SessionManager session;
    MemberLogin user;
    Activity activity;

    ProgressBar progressBar;
    ScrollView layout;
    TextView className;
    TextView price;

    TextView packRot;
    TextView packDoc;
    TextView packDrv;

    TextView exRot;
    TextView exDoc;
    TextView exDrv;

    TextView usRot;
    TextView usDoc;
    TextView usDrv;

    TextView balRot;
    TextView balDoc;
    TextView balDrv;

    TextView valRot;
    TextView valDoc;
    TextView valDrv;


    MemberPlan plan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_plan);

        activity=this;
        global=(MainApplication)getApplication();
        session=new SessionManager(getApplication());
        user=session.getUserLogin();

        PublicFunction.setHeaderStatus(activity,"My Plan");
        initObject();
        fetchData();
    }

    private void initObject()
    {
        progressBar=findViewById(R.id.progressBar);
        layout=findViewById(R.id.layout);
        layout.setVisibility(View.GONE);

        className=findViewById(R.id.className);
        price=findViewById(R.id.price);

        packRot=findViewById(R.id.packRot);
        packDoc=findViewById(R.id.packDoc);
        packDrv=findViewById(R.id.packDrv);

        exRot=findViewById(R.id.exRot);
        exDoc=findViewById(R.id.exDoc);
        exDrv=findViewById(R.id.exDrv);

        usRot=findViewById(R.id.usRot);
        usDoc=findViewById(R.id.usDoc);
        usDrv=findViewById(R.id.usDrv);

        balRot=findViewById(R.id.balRot);
        balDoc=findViewById(R.id.balDoc);
        balDrv=findViewById(R.id.balDrv);

        valRot=findViewById(R.id.valRot);
        valDoc=findViewById(R.id.valDoc);
        valDrv=findViewById(R.id.valDrv);
    }

    private void bindObject()
    {
        className.setText(plan.SubscriptType.ClassName);
        price.setText("DRD Point "+NumberFormat.getInstance().format(plan.Price));

        packRot.setText(plan.RotationCount+"");
        packDoc.setText(plan.StrStorageSize);
        packDrv.setText(plan.StrDrDriveSize);

        exRot.setText(plan.RotationCountAdd+"");
        exDoc.setText(plan.StrStorageSizeAdd);
        exDrv.setText(plan.StrDrDriveSizeAdd);

        usRot.setText(plan.RotationCountUsed+"");
        usDoc.setText(plan.StrStorageSizeUsed);
        usDrv.setText(plan.StrDrDriveSizeUsed);

        balRot.setText(plan.RotationCount+plan.RotationCountAdd-plan.RotationCountUsed+"");
        balDoc.setText(plan.StrStorageSizeBal);
        balDrv.setText(plan.StrDrDriveSizeBal);

        if (plan.ValidPackage!=null) {
            valRot.setText(PublicFunction.dateToString("dd MMM yyyy HH:mm", plan.ValidPackage));
            valDoc.setText(PublicFunction.dateToString("dd MMM yyyy HH:mm", plan.ValidPackage));
        }
        if (plan.ValidDrDrive!=null)
            valDrv.setText(PublicFunction.dateToString("dd MMM yyyy HH:mm", plan.ValidDrDrive));

        progressBar.setVisibility(View.GONE);
        layout.setVisibility(View.VISIBLE);
    }

    private void fetchData()
    {
        MemberService msvr=new MemberService(activity);
        msvr.setOnDataPostedListener(new MemberService.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data, boolean isLoadedAllItems) {
                plan=(MemberPlan)data;
                bindObject();
            }

            @Override
            public <T> void onDataError() {
                progressBar.setVisibility(View.GONE);
            }
        });
        msvr.getPlan(user.Id);
    }
}
