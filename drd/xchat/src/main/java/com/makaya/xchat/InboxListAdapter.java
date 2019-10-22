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

import com.makaya.xchat.domain.MessageSumClass;
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

public class InboxListAdapter extends
        RecyclerView.Adapter<InboxListAdapter.InboxViewHolder>{

    Activity activity;

    public static class InboxViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        ImageView foto;
        TextView name;
        TextView profession;
        TextView count;
        TextView time;
        View itemView;

        InboxViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv);
            foto = itemView.findViewById(R.id.foto);
            name = itemView.findViewById(R.id.name);
            profession = itemView.findViewById(R.id.profession);
            count = itemView.findViewById(R.id.count);
            time = itemView.findViewById(R.id.time);
            this.itemView=itemView;
        }
    }
    Handler handler;
    ArrayList<MessageSumClass> msgs;
    MainApplication main;
    XChatLogin user;

    public InboxListAdapter(ArrayList<MessageSumClass> msgs, Activity activity, XChatLogin user){
        this.msgs = msgs;
        this.activity=activity;
        handler=new Handler();
        main=new MainApplication(activity);
        this.user=user;
    }

    public void setData(ArrayList<MessageSumClass> msgs) {
        this.msgs = msgs;
    }

    @Override
    public int getItemCount() {
        return msgs.size();
    }

    @Override
    public InboxViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.inbox_list_item, viewGroup, false);

        v.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                CardView cv=(CardView)v.findViewById(R.id.cv);
                TextView count=(TextView)v.findViewById(R.id.count);
                MessageSumClass msg=(MessageSumClass)cv.getTag();
                msg.Unread=0;
                Intent intent = new Intent(v.getContext(), InboxDetailActivity.class);
                intent.putExtra("yourFoto",msg.SenderFoto);
                intent.putExtra("yourId",msg.SenderId);
                intent.putExtra("yourName",msg.SenderName);
                intent.putExtra("yourProfession",msg.SenderProfession);
                intent.putExtra("yourType",msg.SenderType);
                intent.putExtra("meId",msg.ReceiverId);
                intent.putExtra("meName",msg.ReceiverName);
                intent.putExtra("User",user);
                count.setVisibility(View.GONE);
                activity.startActivityForResult(intent, 1);
                setRead(activity, msg);
            }
        });
        InboxViewHolder pvh = new InboxViewHolder(v);
        return pvh;
    }

    void setRead(final Activity activity, MessageSumClass msg)
    {
        MainApplication main=new MainApplication(activity);
        String url=String.format(activity.getResources().getString(R.string.api_set_msg_open),
                main.getUrlApplApi(), msg.SenderId, msg.ReceiverId);
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

        MessageSumClass msg=msgs.get(i);
        vehicleViewHolder.cv.setTag(msg);

        vehicleViewHolder.name.setText(msg.SenderName);
        vehicleViewHolder.profession.setText(msg.SenderProfession);
        if (msg.SenderType<4096)
            vehicleViewHolder.profession.setVisibility(View.GONE);
        else
            vehicleViewHolder.profession.setVisibility(View.VISIBLE);

        String period=PublicFunction.dateToString("dd MMM yyyy hh:mm a",msg.TheDate);
        if (PublicFunction.dateToString("yyyyMMdd",msg.TheDate).equals(
                PublicFunction.dateToString("yyyyMMdd",PublicFunction.getToday())))
            period=PublicFunction.dateToString("hh:mm a",msg.TheDate);
        vehicleViewHolder.time.setText(period);
        vehicleViewHolder.count.setText(msg.Unread+"");

        String path = main.getUrlApplWeb() + "/Images/member/" + msg.SenderFoto;
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

        if (msg.Unread==0) {
            vehicleViewHolder.count.setVisibility(View.GONE);
        }else{
            vehicleViewHolder.count.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
