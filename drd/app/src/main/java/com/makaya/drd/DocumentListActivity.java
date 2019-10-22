package com.makaya.drd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.makaya.drd.domain.DocumentLite;
import com.makaya.drd.domain.DocumentLiteRoot;
import com.makaya.drd.domain.MemberLogin;
import com.makaya.drd.library.PublicFunction;
import com.makaya.drd.service.DocumentService;
import com.paginate.Paginate;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by xbudi on 31/10/2016.
 */

public class DocumentListActivity extends AppCompatActivity {

    Activity activity;

    SessionManager session;
    MemberLogin user;

    ArrayList<DocumentLite> documents=new ArrayList<>();

    DocumentAdapter docAdapter;

    EditText search;
    ImageView btnSearch;
    DocumentService docsvr;

    String topCriteria="";
    String sortdata="Title";
    String filterdata="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.document_list);
        activity=this;
        session=new SessionManager(getApplication());
        user=session.getUserLogin();
        docsvr=new DocumentService(activity);
        PublicFunction.setHeaderStatus(activity,"Document List");
        initObject();
        bindObject();
        bindDocRecyclerView();
    }

    int current_page = 1;
    boolean loadingInProgress=false;
    boolean hasLoadedAllItems=false;
    Paginate.Callbacks callbacks = new Paginate.Callbacks() {
        @Override
        public void onLoadMore() {
            // Load next page of data (e.g. network or database)
            loadingInProgress=true;
            docsvr.getLite(user.Id, topCriteria,current_page,sortdata,filterdata);
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

    private void bindDocRecyclerView()
    {
        RecyclerView rv = findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setNestedScrollingEnabled(false);

        LinearLayoutManager llm = new LinearLayoutManager(activity.getBaseContext());
        rv.setLayoutManager(llm);

        docAdapter = new DocumentAdapter(activity, documents);
        rv.setAdapter(docAdapter);

        Paginate.with(rv, callbacks)
                .setLoadingTriggerThreshold(2)
                .addLoadingListItem(true)
                .build();
    }

    void initObject()
    {
        search=findViewById(R.id.search);
        btnSearch=findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                current_page = 1;
                loadingInProgress=false;
                hasLoadedAllItems=false;
                topCriteria=search.getText().toString();
                documents=new ArrayList<>();
                //bindDocRecyclerView();

                docAdapter.setData(documents);
                docAdapter.notifyDataSetChanged();

                PublicFunction.hideKeyboard(activity);
            }
        });
    }

    public void bindObject() {
        docsvr.setOnDataPostedListener(new DocumentService.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data, boolean isLoadedAllItems) {
                if (data!=null) {
                    ArrayList<DocumentLite> datas=((DocumentLiteRoot) data).Root;
                    current_page++;
                    loadingInProgress=false;
                    hasLoadedAllItems=isLoadedAllItems;

                    documents.addAll(datas);
                    docAdapter.setData(documents);
                    docAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public <T> void onDataError() {
            }
        });
    }

    public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.ContentViewHolder> {

        Activity activity;
        ArrayList<DocumentLite> datas;

        public class ContentViewHolder extends RecyclerView.ViewHolder {
            TextView title;
            ImageView ext;
            LinearLayout layoutDoc;

            public ContentViewHolder(View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.title);
                ext = itemView.findViewById(R.id.ext);
                layoutDoc = itemView.findViewById(R.id.layoutDoc);
                layoutDoc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("Document",(DocumentLite)view.getTag());
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    }
                });
            }
        }

        public DocumentAdapter(Activity activity, ArrayList<DocumentLite> datas) {
            this.activity=activity;
            this.datas = datas;
        }

        public void setData(ArrayList<DocumentLite> datas) {
            this.datas = datas;
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        @Override
        public ContentViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.document_list_item, viewGroup, false);
            return new ContentViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ContentViewHolder holder, int i) {
            DocumentLite pack=datas.get(i);
            holder.layoutDoc.setTag(pack);
            holder.title.setText(pack.Title);
            String path = MainApplication.getUrlApplWeb() + "/Images/filetype/" + pack.ExtFile+".png";
            Picasso.with(activity)
                    .load(path)
                    .placeholder(R.drawable.no_document)
                    .error(R.drawable.no_document)
                    .into(holder.ext);
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }
    }
}
