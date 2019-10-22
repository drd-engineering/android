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
import com.makaya.xchat.library.PublicFunction;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by xbudi on 25/10/2016.
 */

public class ContactListAdapter extends
        RecyclerView.Adapter<ContactListAdapter.InboxViewHolder>{

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
    ArrayList<MemberLite> contacts;
    MainApplication main;
    XChatLogin user;

    public ContactListAdapter(ArrayList<MemberLite> contacts, Activity activity, XChatLogin user){
        this.contacts = contacts;
        this.activity=activity;
        handler=new Handler();
        main=new MainApplication(activity);
        this.user=user;
    }

    public void setData(ArrayList<MemberLite> contacts) {
        this.contacts = contacts;
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    @Override
    public InboxViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.contact_list_item, viewGroup, false);

        v.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                CardView cv=v.findViewById(R.id.cv);
                MemberLite msg=(MemberLite)cv.getTag();
                Intent returnIntent = new Intent();
                returnIntent.putExtra("MemberLite", msg);
                activity.setResult(Activity.RESULT_OK, returnIntent);
                activity.finish();

            }
        });
        InboxViewHolder pvh = new InboxViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final InboxViewHolder vehicleViewHolder, int i) {

        MemberLite contact=contacts.get(i);
        vehicleViewHolder.cv.setTag(contact);

        vehicleViewHolder.name.setText(contact.Name);
        vehicleViewHolder.profession.setText(contact.Profession);
        if (contact.MemberType<4096)
            vehicleViewHolder.profession.setVisibility(View.GONE);
        else
            vehicleViewHolder.profession.setVisibility(View.VISIBLE);

        String path = main.getUrlApplWeb() + "/Images/member/" + contact.ImageProfile;
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
