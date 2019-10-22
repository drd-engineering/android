package com.makaya.drd;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.makaya.drd.service.NewsService;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;
import com.makaya.drd.domain.MemberLogin;
import com.makaya.drd.domain.News;
import com.makaya.drd.domain.NewsDetail;
import com.makaya.drd.library.PublicFunction;
import com.makaya.drd.library.ViewPagerTransformer;

import java.util.ArrayList;

/**
 * Created by xbudi on 11/09/2017.
 */

public class NewsDetailActivity extends AppCompatActivity {
    Activity activity;
    SessionManager session;
    MemberLogin user;
    long dataId;
    NewsService service;
    News news;
    MainApplication global;

    ProgressBar progressBar;
    CardView cv;
    TextView title;
    TextView dateCreated;
    ImageView image;
    TextView imageDescr;
    TextView descr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_detail);
        activity=this;
        global=(MainApplication)getApplication();
        session=new SessionManager(getApplication());
        user=session.getUserLogin();

        dataId=getIntent().getLongExtra("DataId",0);
        PublicFunction.setHeaderStatus(activity,"");
        PublicFunction.setBackgroundColorPage(activity,new int[]{R.id.layout});
        initObject();

        service=new NewsService(activity);
        service.setOnDataPostedListener(new NewsService.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data, boolean isLoadedAllItems) {
                news=(News)data;

                String title="";
                if ((news.NewsType & 1) == 1)
                    title = "Article";

                if ((news.NewsType & 2) == 2) {
                    if (title != "")
                        title += " | ";

                    title += "Event";
                }
                if ((news.NewsType & 4) == 4) {
                    if (title != "")
                        title += " | ";

                    title += "Profile";
                }
                if ((news.NewsType & 8) == 8) {
                    if (title != "")
                        title += " | ";

                    title += "Information";
                }

                PublicFunction.setHeaderStatus(activity,title);
                bindField();
            }

            @Override
            public <T> void onDataError() {

            }
        });
        service.getNewsById(dataId,user.Id);
    }

    private void initObject()
    {
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        cv=(CardView)findViewById(R.id.cv);
        title=(TextView)findViewById(R.id.title);
        dateCreated=(TextView)findViewById(R.id.dateCreated);
        image=(ImageView)findViewById(R.id.image);
        imageDescr=(TextView)findViewById(R.id.imageDescr);
        descr=(TextView)findViewById(R.id.descr);

        cv.setVisibility(View.GONE);
    }

    private void bindField()
    {
        progressBar.setVisibility(View.GONE);
        cv.setVisibility(View.VISIBLE);


        title.setText(PublicFunction.fromHtml(news.Title));
        String created="Created "+ PublicFunction.dateToString("dd/MM/yyyy",news.DateCreated);
        if (news.DateUpdated!=null)
            created+=" | Modified "+PublicFunction.dateToString("dd/MM/yyyy",news.DateUpdated);
        dateCreated.setText(created);

        imageDescr.setText(PublicFunction.fromHtml(news.NewsDetails.get(0).Descr));
        if (news.NewsDetails.get(0).Descr==null || news.NewsDetails.get(0).Descr.equals(""))
            imageDescr.setVisibility(View.GONE);

        descr.setText(PublicFunction.fromHtml(news.Descr));

        /*String path = global.getUrlApplWeb() + "/Images/news/" + news.NewsDetails.get(0).Image;
        Picasso.with(activity)
                .load(path)
                .placeholder(R.drawable.nopicture)
                .error(R.drawable.nopicture)
                .into(image);*/

        bindPagerImage();

    }

    private void bindPagerImage()
    {
        ViewPager vp = (ViewPager)findViewById(R.id.pagerImage);

        PagerImageAdapter vpAdapter=new PagerImageAdapter(this, news.NewsDetails);
        vp.setPageTransformer(true, new ViewPagerTransformer(). new DepthPage());
        vp.setAdapter(vpAdapter);

        CirclePageIndicator indicator=(CirclePageIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(vp);
        indicator.setFillColor(Color.RED);
        if (news.NewsDetails.size()<=1)
            indicator.setVisibility(View.INVISIBLE);
        else
            indicator.setVisibility(View.VISIBLE);
    }

    public  class PagerImageAdapter extends PagerAdapter {

        private Context mContext;
        ArrayList<NewsDetail> images;

        public PagerImageAdapter(Context context, ArrayList<NewsDetail> images) {
            mContext = context;
            this.images=images;
        }

        @Override
        public Object instantiateItem(ViewGroup collection, int position) {
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.pager_item, collection, false);

            ImageView typeImage = (ImageView) itemView.findViewById(R.id.img_pager_item);
            typeImage.setTag(position);
            typeImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(activity, PreviewImageGenericActivity.class);
                    ArrayList<String> imgs=new ArrayList<>();
                    for(NewsDetail nd:news.NewsDetails){
                        imgs.add(nd.Image);
                    }
                    intent.putExtra("Images", imgs);
                    intent.putExtra("Pos",(int)view.getTag());
                    intent.putExtra("Folder","/Images/news/");
                    startActivity(intent);
                }
            });
            String path = MainApplication.getUrlApplWeb() + "/Images/news/" + images.get(position).Image;
            Picasso.with(activity)
                    .load(path)
                    .placeholder(R.drawable.nopicture)
                    .error(R.drawable.nopicture)
                    .into(typeImage);


            collection.addView(itemView);
            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }
}
