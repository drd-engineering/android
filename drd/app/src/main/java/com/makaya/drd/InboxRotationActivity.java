package com.makaya.drd;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.makaya.drd.domain.MemberLogin;
import com.makaya.drd.domain.RotationLite;
import com.makaya.drd.domain.RotationLiteRoot;
import com.makaya.drd.domain.RotationNode;
import com.makaya.drd.domain.RotationNodeRemark;
import com.makaya.drd.library.DisplayToast;
import com.makaya.drd.library.PublicFunction;
import com.makaya.drd.service.RotationService;
import com.paginate.Paginate;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

//import com.google.android.gms.ads.MobileAds;

/**
 * Created by xbudi on 31/10/2016.
 */

public class InboxRotationActivity extends AppCompatActivity {

    Activity activity;
    SwipeRefreshLayout mSwipeRefreshLayout;

    SessionManager session;
    MemberLogin user;

    ArrayList<RotationLite> rotations=new ArrayList<>();
    ItemAdapter adapter;
    String status;
    String topCriteria="";
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_content);
        activity=this;
        session=new SessionManager(getApplication());
        user=session.getUserLogin();
        handler=new Handler();
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
        bindRecyclerView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && null != data) {
            if (requestCode == 1) {
                refresh();
            }
        }
    }

    void refresh()
    {
        current_page = 1;
        loadingInProgress=false;
        hasLoadedAllItems=false;
        rotations.clear();
        bindRecyclerView();
    }

    private void bindRecyclerView()
    {
        RecyclerView rv = findViewById(R.id.rv);
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(activity.getBaseContext());
        rv.setLayoutManager(llm);

        adapter = new ItemAdapter(activity, rotations);
        rv.setAdapter(adapter);

        int paginateLoadingTriggerThreshold=getResources().getInteger(R.integer.paginateLoadingTriggerThreshold);
        Paginate.with(rv, callbacks)
                .setLoadingTriggerThreshold(paginateLoadingTriggerThreshold)
                .addLoadingListItem(true)
                //.setLoadingListItemCreator(new CustomLoadingListItemCreator())
                //.setLoadingListItemSpanSizeLookup(new CustomLoadingListItemSpanLookup())
                .build();
    }

    int current_page = 1;
    boolean loadingInProgress=false;
    boolean hasLoadedAllItems=false;
    Paginate.Callbacks callbacks = new Paginate.Callbacks() {
        @Override
        public void onLoadMore() {
            // Load next page of data (e.g. network or database)
            loadingInProgress=true;
            fetchRotation(status, topCriteria, current_page);
        }

        @Override
        public boolean isLoading() {
            // Indicate whether new page loading is in progress or not
            return loadingInProgress;
        }

        @Override
        public boolean hasLoadedAllItems() {
            // Indicate whether all data (pages) are loaded or not
            return hasLoadedAllItems;
        }
    };

    private void fetchRotation(String status, String topCriteria, int page)
    {
        RotationService svr=new RotationService(activity);
        svr.setOnDataPostedListener(new RotationService.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data, boolean isLoadedAllItems) {
                mSwipeRefreshLayout.setRefreshing(false);

                current_page++;
                loadingInProgress=false;
                hasLoadedAllItems=isLoadedAllItems;
                rotations.addAll(((RotationLiteRoot) data).Root);
                if (isLoadedAllItems && rotations.size()==0)
                    handler.post(new DisplayToast(activity, "Data not found."));

                adapter.setData(rotations);
                adapter.notifyDataSetChanged();
            }

            @Override
            public <T> void onDataError() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        svr.getNodeByMemberId(user.Id, status, topCriteria, page);
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
        ArrayList<RotationLite> datas;
        ItemNodeAdapter adapter;

        public class ContentViewHolder extends RecyclerView.ViewHolder {
            TextView rotation;
            TextView workflow;
            TextView activity;
            TextView status;
            //ImageView expand;
            LinearLayout rotationPanel;
            RecyclerView rv;

            public ContentViewHolder(View itemView) {
                super(itemView);
                rotation = itemView.findViewById(R.id.rotation);
                workflow = itemView.findViewById(R.id.workflow);
                activity = itemView.findViewById(R.id.activity);
                status = itemView.findViewById(R.id.status);
                //expand= itemView.findViewById(R.id.expand);
                rotationPanel= itemView.findViewById(R.id.rotationPanel);
                rv = itemView.findViewById(R.id.rv);
            }
        }

        public ItemAdapter(Activity activity, ArrayList<RotationLite> datas) {
            this.activity=activity;
            this.datas = datas;
        }

        public void setData(ArrayList<RotationLite> datas) {
            this.datas = datas;
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        @Override
        public ContentViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.progress_rotation_node_item, viewGroup, false);
            return new ContentViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ContentViewHolder holder, int i) {
            final RotationLite pack=datas.get(i);
            holder.rotation.setText(pack.Subject);
            holder.workflow.setText(pack.WorkflowName);
            holder.activity.setText(pack.ActivityName);
            holder.status.setText(Html.fromHtml("<b>"+pack.StatusDescr+"</b> at <i>"+PublicFunction.dateToString("dd MMM yyyy HH:mm:ss",pack.DateStatus)+"</i>"));
            holder.rotationPanel.setTag(pack);
            //holder.expand.setTag(pack);
            holder.rotationPanel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RotationLite lite=(RotationLite)view.getTag();
                    /*view.setVisibility(View.INVISIBLE);
                    getById(detail.Id, pack, holder.rv);*/
                    /*Intent intent=new Intent(activity, ProgressRotationDetailActivity.class);
                    intent.putExtra("RotationId", lite.Id);
                    intent.putExtra("Caption", lite.Subject);
                    startActivity(intent);*/

                    Intent intent=new Intent(activity, InboxActivity.class);
                    intent.putExtra("NodeId",lite.RotationNodeId);
                    startActivityForResult(intent, 1);
                }
            });

            /*if (pack.RotationNodes.size()>0)
                holder.expand.setVisibility(View.INVISIBLE);
            else
                holder.expand.setVisibility(View.VISIBLE);

            bindItemRecyclerView(holder.rv, pack.RotationNodes);*/
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

            adapter = new ItemNodeAdapter(activity, rtnodes);
            rv.setAdapter(adapter);
        }

        /*public void getById(final long rotationId, final RotationLite pack, final RecyclerView rv){
            RotationService svr=new RotationService(activity);
            svr.setOnDataPostedListener(new RotationService.OnSetDataPostedListener() {
                @Override
                public <T> void onDataPosted(T data, boolean isLoadedAllItems) {
                    pack.RotationNodes=((Rotation)data).RotationNodes;
                    bindItemRecyclerView(rv, pack.RotationNodes);
                    ((ProgressRotationActivity)activity).updateRotationNode(rotationId, pack.RotationNodes);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public <T> void onDataError() {

                }
            });
            svr.getById(rotationId);
        }*/
    }

    public class ItemNodeAdapter extends RecyclerView.Adapter<ItemNodeAdapter.ContentViewHolder> {

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

        public ItemNodeAdapter(Activity activity, ArrayList<RotationNode> datas) {
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
