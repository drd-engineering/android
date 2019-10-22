package com.makaya.xchat;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.makaya.xchat.domain.MemberLite;
import com.makaya.xchat.domain.XChatLogin;
import com.makaya.xchat.library.DisplayToast;
import com.makaya.xchat.library.PostDataModelUrl;
import com.makaya.xchat.library.PublicFunction;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by xbudi on 25/10/2016.
 */

public class InboxListAdvisorAdapter extends
        RecyclerView.Adapter<InboxListAdvisorAdapter.InboxViewHolder>{

    Activity activity;

    public static class InboxViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        ImageView foto;
        TextView name;
        TextView profession;
        View itemView;

        InboxViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv);
            foto = itemView.findViewById(R.id.foto);
            name = itemView.findViewById(R.id.name);
            profession = itemView.findViewById(R.id.profession);
            this.itemView=itemView;
        }
    }
    Handler handler;
    ArrayList<MemberLite> advisors;
    MainApplication main;
    XChatLogin user;
    int screenWidth;

    public InboxListAdvisorAdapter(ArrayList<MemberLite> advisors, Activity activity, XChatLogin user, int screenWidth){
        this.advisors = advisors;
        this.activity=activity;
        handler=new Handler();
        main=new MainApplication(activity);
        this.user=user;
        this.screenWidth=screenWidth;
    }

    @Override
    public int getItemCount() {
        return advisors.size();
    }

    @Override
    public InboxViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.inbox_list_advisor_item, viewGroup, false);

        ViewGroup.LayoutParams params = v.getLayoutParams();
        if (advisors.size()<5)
            params.width = screenWidth/advisors.size();
        else
            params.width = screenWidth/4;
        v.setLayoutParams(params);
        v.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                CardView cv=(CardView)v.findViewById(R.id.cv);
                MemberLite msg=(MemberLite)cv.getTag();
                Intent intent = new Intent(v.getContext(), InboxDetailActivity.class);
                intent.putExtra("yourFoto",msg.ImageProfile);
                intent.putExtra("yourId",msg.Id);
                intent.putExtra("yourName",msg.Name);
                intent.putExtra("yourProfession",msg.Profession);
                intent.putExtra("yourType",msg.MemberType);
                intent.putExtra("meId",user.Id);
                intent.putExtra("meName",user.Name);
                intent.putExtra("User",user);
                v.getContext().startActivity (intent);
                setRead(activity, msg);
            }
        });
        InboxViewHolder pvh = new InboxViewHolder(v);
        return pvh;
    }

    void setRead(final Activity activity, MemberLite msg)
    {
        MainApplication main=new MainApplication(activity);
        String url=String.format(activity.getResources().getString(R.string.api_set_msg_open),
                main.getUrlApplApi(), msg.Id, user.Id);
        final PostDataModelUrl posData = new PostDataModelUrl();
        posData.execute(url, Integer.class);
        posData.setOnDataPostedListener(new PostDataModelUrl.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data) {
                if (data==null)
                {
                    handler.post(new DisplayToast(activity, activity.getResources().getString(R.string.save_data_problem)));
                }
            }

            @Override
            public <T> void onPostedError(Exception data) {

            }
        });
    }

    @Override
    public void onBindViewHolder(final InboxViewHolder vehicleViewHolder, int i) {

        MemberLite advisor=advisors.get(i);
        vehicleViewHolder.cv.setTag(advisor);

        vehicleViewHolder.name.setText(advisor.Name);
        vehicleViewHolder.profession.setText(advisor.Profession);

        String path = main.getUrlApplWeb() + "/Images/member/" + advisor.ImageProfile;
        Picasso.with(activity)
                .load(path)
                .placeholder(R.drawable.ic_user)
                .error(R.drawable.ic_user)
                .into(vehicleViewHolder.foto, new Callback() {
                    @Override
                    public void onSuccess() {
                        PublicFunction.setPhotoProfile(vehicleViewHolder.foto);
                    }

                    @Override
                    public void onError() {

                    }
                });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
