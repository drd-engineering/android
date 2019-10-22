package com.makaya.drd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.makaya.drd.service.NewsService;
import com.paginate.Paginate;
import com.squareup.picasso.Picasso;
import com.makaya.drd.domain.FilterData;
import com.makaya.drd.domain.MemberLogin;
import com.makaya.drd.domain.NewsLite;
import com.makaya.drd.domain.SortData;
import com.makaya.drd.library.PublicFunction;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by xbudi on 16/09/2017.
 */

public class NewsListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    Activity activity;
    View view;
    ArrayList<NewsLite> datas=new ArrayList<>();
    int localAdsPos=0;
    ItemAdapter adapter;
    NewsService dashsvr;

    int insertionAdsItem=2;
    int maxAdsItem=3;

    Button btnFilter;
    Button btnSort;
    EditText search;
    SwipeRefreshLayout mSwipeRefreshLayout;

    SessionManager session;
    MemberLogin user;
    String contentType;
    String title;

    String topCriteria="";
    String sortdata="DateCreated desc";
    String filterdata="";
    SortData current;
    ArrayList<SortData> sortdatas=new ArrayList<>(
            Arrays.asList(
                    new SortData("Title","Title"),
                    new SortData("Older","DateCreated"),
                    new SortData("Newer","DateCreated desc")
            )
    );

    ArrayList<FilterData> filterdatas=new ArrayList<>(
            Arrays.asList(
                    new FilterData("Title","Title", InputType.TYPE_CLASS_TEXT, 50)
            )
    );

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_list);
        activity=this;
        contentType=getIntent().getStringExtra("ContentType");
        String title=getIntent().getStringExtra("Title");
        session=new SessionManager(getApplication());
        user=session.getUserLogin();
        dashsvr=new NewsService(activity);
        current=sortdatas.get(2);
        PublicFunction.setHeaderStatus(activity,title);
        initButton();
        bindObject();
        //fetchDataBanner();
        bindRecyclerView();

    }
    @Override
    public void onRefresh() {

        // Fetching data from server
        mSwipeRefreshLayout.setRefreshing(true);
        refresh();
    }
    void refresh()
    {
        localAdsPos=0;
        current_page = 1;
        loadingInProgress=false;
        hasLoadedAllItems=false;
        datas.clear();
        bindRecyclerView();
    }

    public void bindObject()
    {

        dashsvr.setOnDataPostedListener(new NewsService.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data, boolean isLoadedAllItems) {
                mSwipeRefreshLayout.setRefreshing(false);
                current_page++;
                loadingInProgress=false;
                hasLoadedAllItems=isLoadedAllItems;

                datas.addAll((ArrayList<NewsLite>)data);
                /*//----
                localAds=bannerAdvs;
                ArrayList<NewsLite> tmps=(ArrayList<NewsLite>)data;

                int dataPos=datas.size()-1;
                if (dataPos<0) dataPos=0;
                int adsSize=localAds.size();

                if (adsSize>0){
                    ArrayList<NewsLite> tmps2=new ArrayList<>();
                    Random r = new Random();
                    // generate value for random (no redudance)
                    ArrayList<Integer> apos=new ArrayList<>();
                    for(int i=0;i<adsSize;i++){
                        apos.add(i);
                    }

                    for(int i=0;i<tmps.size();i++) {
                        tmps2.add(tmps.get(i));
                        if ((i+1) % insertionAdsItem==0 && localAdsPos<adsSize && localAdsPos<maxAdsItem) {
                            NewsLite nl=new NewsLite();
                            int randomIndex = r.nextInt(apos.size());
                            Integer rand = apos.get(randomIndex);
                            apos.remove(randomIndex); // remove biar tidak duplikat
                            BannerLite bl=localAds.get(rand);
                            nl.type=1;
                            nl.Title=bl.WebUrl;
                            nl.Id=bl.Id;
                            nl.Image=bl.Image;
                            tmps2.add(nl);
                            localAdsPos++;
                        }
                    }
                    datas.addAll(tmps2);
                }else
                    datas.addAll(tmps);*/
                //----

                //datas.addAll((ArrayList<NewsLite>)data);
                /*if (isLoadedAllItems && datas.size()==0)
                    Toast.makeText(activity, "Data not found.", Toast.LENGTH_SHORT).show();*/

                adapter.setData(datas);
                adapter.notifyDataSetChanged();

            }

            @Override
            public <T> void onDataError() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        //dashsvr.fetchPodCast(current_page);
    }

    void initButton()
    {
        search=(EditText) findViewById(R.id.search);
        btnFilter=(Button)findViewById(R.id.btnFilter);
        btnSort=(Button)findViewById(R.id.btnSort);

        mSwipeRefreshLayout=findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    //Toast.makeText(activity,"ENTER", Toast.LENGTH_SHORT).show();
                    topCriteria=search.getText().toString();
                    refresh();
                    return true;
                }
                return false;
            }
        });


        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupFilterDialog psd=new PopupFilterDialog(activity, filterdatas);
                psd.setOnSelectedListener(new PopupFilterDialog.OnSelectedListener() {
                    @Override
                    public void onSelected(ArrayList<FilterData> datas) {
                        String lastfilter=filterdata;
                        filterdata= PublicFunction.filterDataParse(datas);

                        if (!filterdata.equals(lastfilter)) {
                            refresh();
                        }
                    }
                });
                psd.show();
            }
        });

        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupSortDialog psd=new PopupSortDialog(activity, sortdatas, current);
                psd.setOnSelectedListener(new PopupSortDialog.OnSelectedListener() {
                    @Override
                    public void onSelected(SortData data) {
                        current=(SortData) data;
                        sortdata=current.SortField;
                        refresh();
                    }
                });
                psd.show();

            }
        });


    }

    int current_page = 1;
    boolean loadingInProgress=false;
    boolean hasLoadedAllItems=false;
    Paginate.Callbacks callbacks = new Paginate.Callbacks() {
        @Override
        public void onLoadMore() {
            // Load next page of data (e.g. network or database)
            loadingInProgress=true;
            dashsvr.fetchNewsLite2(user.Id,contentType, topCriteria,current_page,sortdata,filterdata);
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

    private void bindRecyclerView()
    {
        RecyclerView rv = findViewById(R.id.rv);
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(activity.getBaseContext());
        rv.setLayoutManager(llm);

        adapter = new ItemAdapter(activity, datas);
        rv.setAdapter(adapter);

        Paginate.with(rv, callbacks)
                .setLoadingTriggerThreshold(2)
                .addLoadingListItem(true)
                .build();

    }

    public class ItemAdapter extends
            RecyclerView.Adapter<ItemAdapter.AdapterViewHolder> {

        Activity activity;
        ArrayList<NewsLite> datas;
        View view;

        public class AdapterViewHolder extends RecyclerView.ViewHolder {
            ImageView image;
            TextView title;
            TextView descr;
            TextView dateCreated;
            CardView cvNews;
            ImageView imageAds;
            //View separator;


            public AdapterViewHolder(View itemView) {
                super(itemView);
                view = itemView;

                image = (ImageView) itemView.findViewById(R.id.image);
                title = (TextView) itemView.findViewById(R.id.title);
                descr= (TextView) itemView.findViewById(R.id.descr);
                dateCreated= (TextView) itemView.findViewById(R.id.dateCreated);

                cvNews= itemView.findViewById(R.id.cvNews);
                imageAds= itemView.findViewById(R.id.imageAds);
            }
        }

        public ItemAdapter(Activity activity, ArrayList<NewsLite> datas) {
            this.activity=activity;
            this.datas = datas;
        }

        public void setData(ArrayList<NewsLite> datas) {
            this.datas = datas;
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        @Override
        public AdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.news_list_item, viewGroup, false);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView image=(ImageView)v.findViewById(R.id.image);
                    NewsLite pack=(NewsLite)image.getTag();
                    //if (pack.type==0) {
                    Intent intent = new Intent(activity, NewsDetailActivity.class);
                    intent.putExtra("DataId", pack.Id);
                    activity.startActivity(intent);
                    /*}else{
                        PublicFunction2.showDetailBanner(activity,pack.Id,pack.Title);
                    }*/
                }
            });
            AdapterViewHolder pvh = new AdapterViewHolder(v);
            return pvh;
        }

        @Override
        public void onBindViewHolder(AdapterViewHolder adapterViewHolder, int i) {

            NewsLite pack=datas.get(i);
            adapterViewHolder.image.setTag(pack);
            //if (pack.type==0) {
            adapterViewHolder.title.setText(PublicFunction.fromHtml(pack.Title));
            adapterViewHolder.descr.setText(PublicFunction.fromHtml(pack.Descr));

            String created = "Created " + PublicFunction.dateToString("dd/MM/yyyy", pack.DateCreated);
            if (pack.DateUpdated != null)
                created += " | Modified " + PublicFunction.dateToString("dd/MM/yyyy", pack.DateUpdated);
            adapterViewHolder.dateCreated.setText(created);

            adapterViewHolder.cvNews.setVisibility(View.VISIBLE);
            adapterViewHolder.imageAds.setVisibility(View.GONE);

            String path = MainApplication.getUrlApplWeb() + "/Images/news/" + pack.Image;
            Picasso.with(activity)
                    .load(path)
                    .placeholder(R.drawable.initbanner)
                    .error(R.drawable.initbanner)
                    .into(adapterViewHolder.image);
            /*}else{
                adapterViewHolder.cvNews.setVisibility(View.GONE);
                adapterViewHolder.imageAds.setVisibility(View.VISIBLE);

                String path = MainApplication.getUrlApplWeb() + "/Images/banner/" + pack.Image;
                Picasso.with(activity)
                        .load(path)
                        .placeholder(R.drawable.initbanner)
                        .error(R.drawable.initbanner)
                        .into(adapterViewHolder.imageAds);
            }*/
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }
    }



}
