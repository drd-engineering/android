package com.makaya.drd;

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

import com.makaya.drd.domain.MemberLite;
import com.makaya.drd.library.PublicFunction;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by xbudi on 25/10/2016.
 */

public class RecipientListAdapter extends
        RecyclerView.Adapter<RecipientListAdapter.InboxViewHolder>{

    Activity activity;

    public static class InboxViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        ImageView foto;
        TextView name;
        TextView email;
        View itemView;

        InboxViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv);
            foto = itemView.findViewById(R.id.foto);
            name = itemView.findViewById(R.id.name);
            email = itemView.findViewById(R.id.email);
            this.itemView=itemView;
        }
    }
    Handler handler;
    ArrayList<MemberLite> members;
    MainApplication main;

    public RecipientListAdapter(ArrayList<MemberLite> members, Activity activity){
        this.members = members;
        this.activity=activity;
        handler=new Handler();
    }

    public void setData(ArrayList<MemberLite> members) {
        this.members = members;
    }

    @Override
    public int getItemCount() {
        return members.size();
    }

    @Override
    public InboxViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recipient_list_item, viewGroup, false);

        v.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                CardView cv= v.findViewById(R.id.cv);
                MemberLite member=(MemberLite)cv.getTag();
                Intent returnIntent = new Intent();
                returnIntent.putExtra("MemberLite", member);
                activity.setResult(Activity.RESULT_OK, returnIntent);
                activity.finish();
            }
        });
        InboxViewHolder pvh = new InboxViewHolder(v);
        return pvh;
    }


    @Override
    public void onBindViewHolder(final InboxViewHolder vehicleViewHolder, int i) {

        MemberLite member=members.get(i);
        vehicleViewHolder.cv.setTag(member);

        vehicleViewHolder.name.setText(member.Name);
        vehicleViewHolder.email.setText(member.Email);

        String path = main.getUrlApplWeb() + "/Images/member/" + member.ImageProfile;
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
