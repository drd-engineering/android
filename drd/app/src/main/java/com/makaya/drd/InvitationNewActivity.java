package com.makaya.drd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.makaya.drd.domain.MemberLogin;
import com.makaya.drd.library.PublicFunction;
import com.makaya.drd.service.MemberService;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class InvitationNewActivity extends AppCompatActivity {
    MainApplication global;
    SessionManager session;
    MemberLogin user;
    Activity activity;

    LinearLayout layoutDetail;
    EditText findemail;
    TextInputEditText expiryday;
    ImageView foto;
    TextView number;
    TextView name;
    TextView phone;
    Button invite;

    PopupProgress popupProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invitation_new);

        activity=this;
        session=new SessionManager(getApplication());
        user=session.getUserLogin();
        popupProgress=new PopupProgress(activity);
        PublicFunction.setHeaderStatus(activity, "New Invitation");
        initObject();
    }

    private void initObject()
    {
        layoutDetail=findViewById(R.id.layoutDetail);
        layoutDetail.setVisibility(View.GONE);
        findemail=findViewById(R.id.findemail);
        expiryday=findViewById(R.id.expiryday);
        foto=findViewById(R.id.foto);
        number=findViewById(R.id.number);
        name=findViewById(R.id.name);
        phone=findViewById(R.id.phone);
        invite=findViewById(R.id.invite);
        findemail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                layoutDetail.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        findemail.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    PublicFunction.hideKeyboard(activity);
                    fetchData(user.Id, findemail.getText().toString());
                    return true;
                }
                return false;
            }
        });

        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (expiryday.getText().toString().equals("")){
                    Toast.makeText(activity,"Required expiry day",Toast.LENGTH_SHORT).show();
                    expiryday.requestFocus();
                    return;
                }
                save(user.Id,findemail.getText().toString(), Integer.parseInt(expiryday.getText().toString()));
            }
        });

    }

    private void bindObject(MemberLogin member)
    {
        expiryday.setText("3");
        expiryday.requestFocus();
        number.setText(member.Number);
        name.setText(member.Name);
        phone.setText(member.Phone);
        String path = MainApplication.getUrlApplWeb() + "/Images/member/" + member.ImageProfile;
        Picasso.with(activity)
                .load(path)
                .placeholder(com.makaya.xchat.R.drawable.ic_user)
                .error(com.makaya.xchat.R.drawable.ic_user)
                .into(foto, new Callback() {
                    @Override
                    public void onSuccess() {
                        PublicFunction.setPhotoProfile(foto);
                    }

                    @Override
                    public void onError() {

                    }
                });

        layoutDetail.setVisibility(View.VISIBLE);
    }

    private void fetchData(long userId, String email)
    {
        MemberService svr=new MemberService(activity);
        svr.setOnDataPostedListener(new MemberService.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data, boolean isLoadedAllItems) {
                MemberLogin member=(MemberLogin) data;
                if (member.Id > 0) {
                    expiryday.setText("3");
                    bindObject(member);
                } else if (member.Id == 0) {
                    Toast.makeText(activity,"Recipient email is not found in our system", Toast.LENGTH_SHORT).show();
                } else if (member.Id == -1) {
                    Toast.makeText(activity,"Not allowed to invite to yourself", Toast.LENGTH_SHORT).show();
                } else if (member.Id == -2) {
                    Toast.makeText(activity,"Recipient is already in the invited list", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public <T> void onDataError() {

            }
        });
        svr.checkInvitation(userId, email);
    }

    private void save(long userId, String email, int expiryDay)
    {
        popupProgress.show();
        MemberService svr=new MemberService(activity);
        svr.setOnDataPostedListener(new MemberService.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data, boolean isLoadedAllItems) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
                popupProgress.dismiss();
            }

            @Override
            public <T> void onDataError() {
                popupProgress.dismiss();
            }
        });
        svr.save(userId, email, expiryDay, MainApplication.getUrlApplWeb());
    }
}
