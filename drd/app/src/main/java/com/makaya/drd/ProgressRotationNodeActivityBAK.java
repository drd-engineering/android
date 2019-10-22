package com.makaya.drd;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.makaya.drd.domain.MemberLogin;
import com.makaya.drd.domain.Rotation;
import com.makaya.drd.domain.RotationNode;
import com.makaya.drd.domain.RotationNodeRemark;
import com.makaya.drd.domain.RotationRoot;
import com.makaya.drd.library.PublicFunction;
import com.makaya.drd.service.RotationService;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

//import com.google.android.gms.ads.MobileAds;

/**
 * Created by xbudi on 31/10/2016.
 */

public class ProgressRotationNodeActivityBAK extends AppCompatActivity {

    Activity activity;
    SwipeRefreshLayout mSwipeRefreshLayout;

    SessionManager session;
    MemberLogin user;

    ArrayList<Rotation> rotations;
    ItemAdapter adapter;
    String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_content);
        activity=this;
        session=new SessionManager(getApplication());
        user=session.getUserLogin();
        String caption=getIntent().getStringExtra("Caption");
        status=getIntent().getStringExtra("Status");
        PublicFunction.setHeaderStatus(activity,caption);
        initObject();

        mSwipeRefreshLayout=findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                refresh();
            }
        });

        bindObject();
        fetchRotation(status);
    }

    void refresh()
    {
        rotations.clear();
        fetchRotation(status);
    }

    private void bindRecyclerView()
    {
        RecyclerView rv = findViewById(R.id.rv);
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(activity.getBaseContext());
        rv.setLayoutManager(llm);

        adapter = new ItemAdapter(activity, rotations);
        rv.setAdapter(adapter);
    }

    private void fetchRotation(String status)
    {
        RotationService svr=new RotationService(activity);
        svr.setOnDataPostedListener(new RotationService.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data, boolean isLoadedAllItems) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (data!=null) {
                    rotations = ((RotationRoot) data).Root;
                    bindRecyclerView();
                }
            }

            @Override
            public <T> void onDataError() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        //svr.getNodeByMemberId(user.Id, status);
    }

    public MemberLogin getUserLogin(){
        return user;
    }


    void initObject()
    {

    }

    public void bindObject() {
    }

    public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ContentViewHolder> {

        Activity activity;
        ArrayList<Rotation> datas;

        public class ContentViewHolder extends RecyclerView.ViewHolder {
            TextView rotation;
            TextView status;
            ImageView expand;
            RecyclerView rv;

            public ContentViewHolder(View itemView) {
                super(itemView);
                rotation = itemView.findViewById(R.id.rotation);
                status = itemView.findViewById(R.id.status);
                expand = itemView.findViewById(R.id.expand);
                rv = itemView.findViewById(R.id.rv);
            }
        }

        public ItemAdapter(Activity activity, ArrayList<Rotation> datas) {
            this.activity=activity;
            this.datas = datas;
        }

        public void setData(ArrayList<Rotation> datas) {
            this.datas = datas;
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        @Override
        public ContentViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.progress_rotation_item, viewGroup, false);
            return new ContentViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ContentViewHolder holder, int i) {
            Rotation pack=datas.get(i);
            holder.rotation.setText(pack.Subject);
            //holder.status.setText(Html.fromHtml("<b>"+pack.StatusDescr+"</b> at <i>"+PublicFunction.dateToString("dd MMM yyyy HH:mm:ss",pack.DateStatus)+"</i>"));

            bindItemRecyclerView(holder.rv, pack.RotationNodes);
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        public void bindItemRecyclerView(RecyclerView rv, ArrayList<RotationNode> rtnodes)
        {
            rv.setHasFixedSize(true);

            LinearLayoutManager llm = new LinearLayoutManager(activity.getBaseContext());
            rv.setLayoutManager(llm);

            ItemItemAdapter adapter = new ItemItemAdapter(activity, rtnodes);
            rv.setAdapter(adapter);
        }
    }

    public class ItemItemAdapter extends RecyclerView.Adapter<ItemItemAdapter.ContentViewHolder> {

        Activity activity;
        ArrayList<RotationNode> datas;

        public class ContentViewHolder extends RecyclerView.ViewHolder {
            ImageView image;
            TextView title;
            TextView status;
            TextView name;
            TextView remark;
            TextView note;

            public ContentViewHolder(View itemView) {
                super(itemView);
                image = itemView.findViewById(R.id.image);
                title = itemView.findViewById(R.id.title);
                status = itemView.findViewById(R.id.status);
                name = itemView.findViewById(R.id.name);
                remark = itemView.findViewById(R.id.remark);
                note = itemView.findViewById(R.id.note);
            }
        }

        public ItemItemAdapter(Activity activity, ArrayList<RotationNode> datas) {
            this.activity=activity;
            this.datas = datas;
        }

        public void setData(ArrayList<RotationNode> datas) {
            this.datas = datas;
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        @Override
        public ContentViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

            View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.progress_rotation_detail_item, viewGroup, false);
            return new ContentViewHolder(v);

        }

        @Override
        public void onBindViewHolder(final ContentViewHolder holder, int i) {

            RotationNode pack=datas.get(i);
            holder.title.setText(PublicFunction.dateToString("dd/MM/yyyy HH:mm:ss", pack.DateCreated));
            holder.status.setText(pack.StatusCode.Descr);
            holder.status.setTextColor(Color.parseColor(pack.StatusCode.TextColor));
            holder.status.setBackgroundColor(Color.parseColor(pack.StatusCode.BackColor));
            holder.name.setText(Html.fromHtml(pack.Member.Name+", Activity: <b>"+pack.WorkflowNode.Caption+"</b>"));
            holder.note.setText(pack.Note);
            String remark="";
            for(RotationNodeRemark rnr: pack.RotationNodeRemarks){
                if (!remark.equals(""))
                    remark+="\r\n";
                remark+=PublicFunction.dateToString("dd/MM/yyyy HH:mm:ss", rnr.DateStamp)+": "+rnr.Remark;
            }
            holder.remark.setText(remark);

            if (holder.note==null)
                holder.note.setVisibility(View.GONE);
            else
                holder.note.setVisibility(View.VISIBLE);

            String path = MainApplication.getUrlApplWeb() + "/Images/member/" + pack.Member.ImageProfile;
            Picasso.with(activity)
                    .load(path)
                    .placeholder(R.drawable.pic_male)
                    .error(R.drawable.pic_male)
                    .into(holder.image, new Callback() {
                        @Override
                        public void onSuccess() {
                            PublicFunction.setPhotoProfile(holder.image);
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

}
