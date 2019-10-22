package com.makaya.drd;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.makaya.drd.domain.GenericData;
import com.makaya.drd.domain.MemberTitle;
import com.makaya.drd.domain.PointLocation;
import com.makaya.drd.domain.SubscriptionRegistry;
import com.makaya.drd.domain.SubscriptionRegistryResult;
import com.makaya.drd.domain.XLatLng;
import com.makaya.drd.library.PublicFunction;
import com.makaya.drd.service.CompanyService;
import com.makaya.drd.service.MemberService;

import java.util.ArrayList;

/**
 * Created by xbudi on 14/09/2017.
 */

public class RegistrationActivity extends AppCompatActivity {

    Activity activity;

    EditText title;
    EditText name;
    EditText email;
    EditText phone;

    EditText company;
    EditText cemail;
    EditText cphone;
    EditText address;
    EditText location;
    Button btnSubmit;
    View focusId=null;
    CompanyService service;
    ArrayList<GenericData> titles=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=this;
        setContentView(R.layout.registration);
        PublicFunction.setStatusBarColor(activity, R.color.colorBased);

        service=new CompanyService(activity);

        getMemberTitle();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && null != data) {
            if (requestCode == 5) {
                PointLocation point=(PointLocation)data.getSerializableExtra("Point");
                location.setText(point.AddressDetail);
                location.setTag(point);
            }
        }
    }

    private void initObject()
    {
        btnSubmit=(Button)findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

        title=findViewById(R.id.title);
        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        phone=findViewById(R.id.phone);

        company=findViewById(R.id.company);
        address=findViewById(R.id.address);
        location=findViewById(R.id.location);
        cemail=findViewById(R.id.cemail);
        cphone=findViewById(R.id.cphone);

        PublicFunction.setNotEditable(title);
        PublicFunction.setNotEditable(location);

        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewTitle(view);
            }
        });
        title.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b && focusId!=view)
                    viewTitle(view);
            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewLocation(view);
            }
        });
        location.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b && focusId!=view)
                    viewLocation(view);
            }
        });
    }

    private void getMemberTitle()
    {
        MemberService svr=new MemberService(activity);
        svr.setOnDataPostedListener(new MemberService.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data, boolean isLoadedAllItems) {
                ArrayList<MemberTitle> ttls=(ArrayList<MemberTitle>)data;
                for(MemberTitle t:ttls){
                    titles.add(new GenericData(t.Id, t.Title));
                }
                initObject();
            }

            @Override
            public <T> void onDataError() {

            }
        });
        svr.getMemberTitle();
    }
    private void viewLocation(View view)
    {
        focusId=view;
        PointLocation pl=(PointLocation)location.getTag();
        XLatLng ll=new XLatLng(0,0);
        if (pl!=null) {
            ll.Latitude = pl.Latitude;
            ll.Longitude = pl.Longitude;
        }
        Intent intent = new Intent(activity, PopupLocation.class);
        intent.putExtra("LatLong",ll);
        startActivityForResult(intent,5);
    }

    private void viewTitle(View view)
    {
        focusId=view;
        PopupDialogList pd=new PopupDialogList(view, R.id.title);
        pd.Show(R.layout.popup_list);
        pd.BindData(titles);
        pd.setOnSelectedListener(new PopupDialogList.OnSelectedListener() {
            @Override
            public void onSelected(Object data) {

            }
        });
    }
    private boolean isValidForm()
    {
        if (title.getText().toString().trim().equals("")){
            Toast.makeText(activity,"Title required!",Toast.LENGTH_SHORT).show();
            title.requestFocus();
            return false;
        }
        if (name.getText().toString().trim().equals("")){
            Toast.makeText(activity,"Name required!",Toast.LENGTH_SHORT).show();
            name.requestFocus();
            return false;
        }
        if (email.getText().toString().trim().equals("") || !PublicFunction.isEmailValid(email.getText().toString())){
            Toast.makeText(activity,"Invalid email address.",Toast.LENGTH_SHORT).show();
            email.requestFocus();
            return false;
        }
        if (phone.getText().toString().trim().equals("")){
            Toast.makeText(activity,"Phone required!",Toast.LENGTH_SHORT).show();
            phone.requestFocus();
            return false;
        }


        /*if (company.getText().toString().trim().equals("")){
            Toast.makeText(activity,"Company required!",Toast.LENGTH_SHORT).show();
            company.requestFocus();
            return false;
        }
        if (cemail.getText().toString().trim().equals("") || !PublicFunction2.isEmailValid(email.getText().toString())){
            Toast.makeText(activity,"Invalid company email address.",Toast.LENGTH_SHORT).show();
            cemail.requestFocus();
            return false;
        }
        if (cphone.getText().toString().trim().equals("")){
            Toast.makeText(activity,"Company phone required!",Toast.LENGTH_SHORT).show();
            cphone.requestFocus();
            return false;
        }*/
        if (address.getText().toString().trim().equals("")){
            Toast.makeText(activity,"Address required!",Toast.LENGTH_SHORT).show();
            address.requestFocus();
            return false;
        }
        if (location.getText().toString().trim().equals("")){
            Toast.makeText(activity,"Point location required!",Toast.LENGTH_SHORT).show();
            location.requestFocus();
            return false;
        }
        return true;
    }

    private void submit()
    {
        if (!isValidForm())
            return;

        PublicFunction.setEnableButton(btnSubmit,false,"Processing...");
        service.setOnDataPostedListener(new CompanyService.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data, boolean isLoadedAllItems) {
                SubscriptionRegistryResult result=(SubscriptionRegistryResult)data;
                if (result.UserNumber.equals("DBLEMAIL")) {
                    new AlertDialog.Builder(activity)
                            .setTitle("Error")
                            .setMessage("User email already used by other.")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //finish();
                                }
                            }).show();
                    PublicFunction.setEnableButton(btnSubmit,true,"Submit");
                    return;
                }
                if (result.SubscriptionId.equals("DBLEMAIL")) {
                    new AlertDialog.Builder(activity)
                            .setTitle("Error")
                            .setMessage("Company email already used by other.")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //finish();
                                }
                            }).show();
                    PublicFunction.setEnableButton(btnSubmit,true,"Submit");
                    return;
                }
                PublicFunction.hideKeyboard(activity);
                Intent intent=new Intent(activity,RegistrationSuccessActivity.class);
                intent.putExtra("SubscriptionRegistryResult",(SubscriptionRegistryResult)data);
                startActivity(intent);
                finish();

            }

            @Override
            public <T> void onDataError() {
                PublicFunction.setEnableButton(btnSubmit,true,"Submit");
            }
        });
        SubscriptionRegistry reg=new SubscriptionRegistry();

        reg.MemberTitleId=(int)((GenericData)title.getTag()).Id;
        reg.MemberName=name.getText().toString().trim();
        reg.MemberPhone=phone.getText().toString().trim();
        reg.MemberEmail=email.getText().toString().trim();
        reg.SubscriptTypeId=0;

        reg.CompanyName=iifEmpty(company.getText().toString().trim(), reg.MemberName);
        reg.CompanyPhone=iifEmpty(cphone.getText().toString().trim(), reg.MemberPhone);;
        reg.CompanyEmail=iifEmpty(cemail.getText().toString().trim(), reg.MemberEmail);;

        reg.CompanyAddress=address.getText().toString().trim();
        reg.CompanyPointLocation=location.getText().toString().trim();
        PointLocation pl=((PointLocation)location.getTag());
        reg.Latitude=pl.Latitude;
        reg.Longitude=pl.Longitude;
        service.saveRegistration(reg);

    }

    private String iifEmpty(String var, String value)
    {
        String result=var;
        if (result.equals(""))
            result=value;

        return result;
    }
}