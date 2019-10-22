package com.makaya.drd;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.makaya.drd.domain.MemberLogin;
import com.makaya.drd.domain.Rotation;
import com.makaya.drd.domain.RotationNode;
import com.makaya.drd.domain.RotationNodeDoc;
import com.makaya.drd.domain.RotationNodeRemark;
import com.makaya.drd.domain.RotationNodeUpDoc;
import com.makaya.drd.library.DownloadFile;
import com.makaya.drd.library.PublicFunction;
import com.makaya.drd.service.RotationService;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;

//import com.google.android.gms.ads.MobileAds;

/**
 * Created by xbudi on 31/10/2016.
 */

public class ProgressRotationDetailActivity extends AppCompatActivity {

    Activity activity;
    SwipeRefreshLayout mSwipeRefreshLayout;

    SessionManager session;
    MemberLogin user;

    Rotation rotation;
    Handler handler;
    long rotationId;
    String rotationStatus;

    LinearLayout layoutDoc;
    LinearLayout layoutUpDoc;
    SwipeRefreshLayout swipe_container;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_content_detail);
        activity=this;
        session=new SessionManager(getApplication());
        user=session.getUserLogin();
        handler=new Handler();
        String caption=getIntent().getStringExtra("Caption");
        rotationId=getIntent().getLongExtra("RotationId",0);
        rotationStatus=getIntent().getStringExtra("RotationStatus");
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
        fetchRotationNode();
    }

    void refresh()
    {
        fetchRotationNode();
    }

    private void bindRecyclerView()
    {
        RecyclerView rv = findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setNestedScrollingEnabled(false);

        LinearLayoutManager llm = new LinearLayoutManager(activity.getBaseContext());
        rv.setLayoutManager(llm);

        ItemAdapter adapter = new ItemAdapter(activity, rotation.RotationNodes);
        rv.setAdapter(adapter);

    }

    private void bindDocRecyclerView()
    {
        RecyclerView rv = findViewById(R.id.rvDoc);
        rv.setHasFixedSize(true);
        rv.setNestedScrollingEnabled(false);

        LinearLayoutManager llm = new LinearLayoutManager(activity.getBaseContext());
        rv.setLayoutManager(llm);

        ItemDocAdapter adapter = new ItemDocAdapter(activity, rotation.SumRotationNodeDocs);
        rv.setAdapter(adapter);

    }

    private void bindUpDocRecyclerView()
    {
        RecyclerView rv = findViewById(R.id.rvUpDoc);
        rv.setHasFixedSize(true);
        rv.setNestedScrollingEnabled(false);

        LinearLayoutManager llm = new LinearLayoutManager(activity.getBaseContext());
        rv.setLayoutManager(llm);

        ItemUpDocAdapter adapter = new ItemUpDocAdapter(activity, rotation.SumRotationNodeUpDocs);
        rv.setAdapter(adapter);

    }

    private void fetchRotationNode()
    {
        RotationService svr=new RotationService(activity);
        svr.setOnDataPostedListener(new RotationService.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data, boolean isLoadedAllItems) {
                mSwipeRefreshLayout.setRefreshing(false);
                rotation=(Rotation)data;
                bindRecyclerView();
                bindDocRecyclerView();
                bindUpDocRecyclerView();

                if (rotation.SumRotationNodeDocs.size()==0)
                    layoutDoc.setVisibility(View.GONE);
                if (rotation.SumRotationNodeUpDocs.size()==0)
                    layoutUpDoc.setVisibility(View.GONE);

                progressBar.setVisibility(View.GONE);
                swipe_container.setVisibility(View.VISIBLE);
            }

            @Override
            public <T> void onDataError() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        svr.getById(rotationId, user.Id);
    }

    public MemberLogin getUserLogin(){
        return user;
    }


    void initObject() {
        layoutDoc=findViewById(R.id.layoutDoc);
        layoutUpDoc=findViewById(R.id.layoutUpDoc);
        swipe_container=findViewById(R.id.swipe_container);
        progressBar=findViewById(R.id.progressBar);
        swipe_container.setVisibility(View.GONE);
    }

    public void bindObject() {}

    public void showBrowser(String url){
        Intent intent=new Intent(activity, DocRevisiBrowserActivity.class);
        intent.putExtra("URL", url);
        intent.putExtra("Title", "PDF Viewer");
        intent.putExtra("ViewType", 2);
        startActivity(intent);
    }

    public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ContentViewHolder> {

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

                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (rotationStatus==null || !rotationStatus.equals("01"))
                            return;

                        RotationNode pack=(RotationNode)view.getTag();

                        ArrayList<String> imgs = new ArrayList<>();
                        ArrayList<String> ttls = new ArrayList<>();
                        /*if (pack.Member.ImageProfile!=null) {
                            imgs.add(pack.Member.ImageProfile);
                            ttls.add("Profile");
                        }*/
                        if (pack.Member.ImageKtp1!=null) {
                            imgs.add(pack.Member.ImageKtp1);
                            ttls.add("KTP 1");
                        }
                        if (pack.Member.ImageKtp2!=null){
                            imgs.add(pack.Member.ImageKtp2);
                            ttls.add("KTP 2");
                        }
                        /*if (pack.Member.ImageSignature!=null){
                            imgs.add(pack.Member.ImageSignature);
                            ttls.add("Signature");
                        }
                        if (pack.Member.ImageInitials!=null){
                            imgs.add(pack.Member.ImageInitials);
                            ttls.add("Initial");
                        }
                        if (pack.Member.ImageStamp!=null){
                            imgs.add(pack.Member.ImageStamp);
                            ttls.add("Private Stamp");
                        }*/

                        if (imgs.size()==0){
                            Toast.makeText(activity,"No ID image found",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Intent intent = new Intent(activity, PreviewImageGenericActivity.class);
                        intent.putExtra("Images", imgs);
                        intent.putExtra("Titles", ttls);
                        intent.putExtra("Pos", 0);
                        intent.putExtra("BackColor", Color.WHITE);
                        intent.putExtra("Folder", "/Images/member/");
                        activity.startActivity(intent);
                    }
                });
            }
        }

        public ItemAdapter(Activity activity, ArrayList<RotationNode> datas) {
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
            holder.image.setTag(pack);
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

    public class ItemDocAdapter extends RecyclerView.Adapter<ItemDocAdapter.ContentViewHolder> {

        Activity activity;
        ArrayList<RotationNodeDoc> datas;

        public class ContentViewHolder extends RecyclerView.ViewHolder {
            ImageView image;
            TextView title;
            TextView filename;
            TextView action;
            TextView size;

            public ContentViewHolder(View itemView) {
                super(itemView);
                image = itemView.findViewById(R.id.image);
                title = itemView.findViewById(R.id.title);
                filename = itemView.findViewById(R.id.filename);
                action = itemView.findViewById(R.id.action);
                size = itemView.findViewById(R.id.size);
            }
        }

        public ItemDocAdapter(Activity activity, ArrayList<RotationNodeDoc> datas) {
            this.activity=activity;
            this.datas = datas;
        }

        public void setData(ArrayList<RotationNodeDoc> datas) {
            this.datas = datas;
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        @Override
        public ContentViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.progress_rotation_detail_doc_item, viewGroup, false);
            return new ContentViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ContentViewHolder holder, int i) {
            RotationNodeDoc pack=datas.get(i);
            holder.title.setTag(pack);
            holder.title.setText(pack.Document.Title);
            holder.title.setPaintFlags(holder.title.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            holder.filename.setText(pack.Document.FileNameOri);
            holder.size.setText(NumberFormat.getInstance().format(pack.Document.FileSize)+PublicFunction.toJamak(" byte",pack.Document.FileSize) );
            String action="";
            if ((pack.FlagAction & 1)==1)
                action+="signed ";
            if ((pack.FlagAction & 2)==2)
                action+="revised ";
            if ((pack.FlagAction & 4)==4)
                action+="viewed ";
            if ((pack.FlagAction & 8)==8)
                action+="printed ";
            if ((pack.FlagAction & 16)==16)
                action+="downloaded ";

            holder.action.setText(action);
            if (action.equals(""))
                holder.action.setVisibility(View.GONE);
            else
                holder.action.setVisibility(View.VISIBLE);

            String path = MainApplication.getUrlApplWeb() + "/Images/FileType/" + pack.Document.ExtFile+".png";
            Picasso.with(activity)
                    .load(path)
                    .placeholder(R.drawable.ic_nofiletype)
                    .error(R.drawable.ic_nofiletype)
                    .into(holder.image);

            holder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RotationNodeDoc pack=(RotationNodeDoc)view.getTag();
                    if (pack.Document.DocumentMember==null || pack.Document.DocumentMember.FlagPermission<=1){
                        Toast.makeText(activity,"You don't have permission to open this file.",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    showBrowser(MainApplication.getUrlApplWeb()+ "/Document/XPdfViewer?documentId="+pack.DocumentId+"&memberId="+user.Id+"&type=2");
                }
            });
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

    }

    public class ItemUpDocAdapter extends RecyclerView.Adapter<ItemUpDocAdapter.ContentViewHolder> {

        Activity activity;
        ArrayList<RotationNodeUpDoc> datas;

        public class ContentViewHolder extends RecyclerView.ViewHolder {
            ImageView image;
            TextView filename;
            TextView size;

            public ContentViewHolder(View itemView) {
                super(itemView);
                image = itemView.findViewById(R.id.image);
                filename = itemView.findViewById(R.id.filename);
                size = itemView.findViewById(R.id.size);
            }
        }

        public ItemUpDocAdapter(Activity activity, ArrayList<RotationNodeUpDoc> datas) {
            this.activity=activity;
            this.datas = datas;
        }

        public void setData(ArrayList<RotationNodeUpDoc> datas) {
            this.datas = datas;
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        @Override
        public ContentViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.progress_rotation_detail_updoc_item, viewGroup, false);
            return new ContentViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ContentViewHolder holder, int i) {
            final RotationNodeUpDoc pack=datas.get(i);
            holder.filename.setText(pack.DocumentUpload.FileNameOri);
            holder.filename.setPaintFlags(holder.filename.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            holder.size.setText(NumberFormat.getInstance().format(pack.DocumentUpload.FileSize)+PublicFunction.toJamak(" byte",pack.DocumentUpload.FileSize) );

            String path = MainApplication.getUrlApplWeb() + "/Images/FileType/" + pack.DocumentUpload.ExtFile+".png";
            Picasso.with(activity)
                    .load(path)
                    .placeholder(R.drawable.ic_nofiletype)
                    .error(R.drawable.ic_nofiletype)
                    .into(holder.image);

            holder.filename.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DownloadFile download=new DownloadFile(activity, pack.DocumentUpload.FileNameOri);
                    download.execute(MainApplication.getUrlApplWeb() + "/updownfile/XDownload?ufileName=" + pack.DocumentUpload.FileName + "&isDocument=false");
                }
            });
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

    }
}
