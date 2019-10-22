package com.makaya.drd;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.makaya.drd.domain.InvitationResult;
import com.makaya.drd.domain.MemberInvited;
import com.makaya.drd.library.PublicFunction;
import com.makaya.drd.service.MemberService;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by xbudi on 25/10/2016.
 */

public class InvitationInvitationAdapter extends
        RecyclerView.Adapter<InvitationInvitationAdapter.InboxViewHolder>{

    Activity activity;

    public static class InboxViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        ImageView foto;
        TextView name;
        TextView email;
        TextView date;
        TextView status;
        View itemView;

        InboxViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv);
            foto = itemView.findViewById(R.id.foto);
            name = itemView.findViewById(R.id.name);
            email = itemView.findViewById(R.id.email);
            date = itemView.findViewById(R.id.date);
            status = itemView.findViewById(R.id.status);
            this.itemView=itemView;
        }
    }
    Handler handler;
    ArrayList<MemberInvited> members;
    MainApplication main;

    public InvitationInvitationAdapter(ArrayList<MemberInvited> members, Activity activity){
        this.members = members;
        this.activity=activity;
        handler=new Handler();
    }

    public void setData(ArrayList<MemberInvited> members) {
        this.members = members;
    }

    @Override
    public int getItemCount() {
        return members.size();
    }

    @Override
    public InboxViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.invitation_list_item, viewGroup, false);

        v.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                CardView cv= v.findViewById(R.id.cv);
                final MemberInvited member=(MemberInvited)cv.getTag();
                if (member.Status.equals("10")){

                    new android.app.AlertDialog.Builder( activity )
                            .setTitle( "Confirmation" )
                            .setMessage( "Are you sure do you want accept this invitation?" )
                            .setNegativeButton( "No", null )
                            .setPositiveButton( "Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    acceptInvitation(member.Id);
                                }
                            })
                            .show();
                }else {
                    Intent intent = new Intent(activity, PreviewImageGenericActivity.class);
                    ArrayList<String> imgs = new ArrayList<>();
                    imgs.add(member.Member.ImageProfile);
                    intent.putExtra("Images", imgs);
                    intent.putExtra("Pos", 0);
                    intent.putExtra("Folder", "/Images/member/");
                    activity.startActivity(intent);
                }
            }
        });
        InboxViewHolder pvh = new InboxViewHolder(v);
        return pvh;
    }


    @Override
    public void onBindViewHolder(final InboxViewHolder vehicleViewHolder, int i) {

        MemberInvited member=members.get(i);
        vehicleViewHolder.cv.setTag(member);

        vehicleViewHolder.name.setText(member.Member.Name);
        vehicleViewHolder.email.setText(member.Member.Email);
        vehicleViewHolder.status.setText(member.StatusDescr);
        if (member.Status.equals("10"))
            vehicleViewHolder.status.setTextColor(Color.BLUE);
        else if (member.Status.equals("11"))
            vehicleViewHolder.status.setTextColor(Color.BLACK);
        else if (member.Status.equals("97"))
            vehicleViewHolder.status.setTextColor(Color.RED);

        String period= PublicFunction.dateToString("dd MMM yyyy hh:mm a",member.DateCreated);
        vehicleViewHolder.date.setText(period);

        String path = main.getUrlApplWeb() + "/Images/member/" + member.Member.ImageProfile;
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

    private void acceptInvitation(long id){
        MemberService svr=new MemberService(activity);
        svr.setOnDataPostedListener(new MemberService.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data, boolean isLoadedAllItems) {
                InvitationResult result=(InvitationResult)data;
                if (result.Result==1){
                    Toast.makeText(activity,"Acceptance of invitations is successful",Toast.LENGTH_SHORT).show();
                    ((InvitationActivity) activity).refreshAllTab();
                }else
                    Toast.makeText(activity,result.Message,Toast.LENGTH_SHORT).show();
            }

            @Override
            public <T> void onDataError() {

            }
        });
        svr.acceptInvitation(id);
    }
}
