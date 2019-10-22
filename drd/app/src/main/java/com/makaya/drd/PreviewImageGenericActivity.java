package com.makaya.drd;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.makaya.drd.library.PublicFunction;
import com.makaya.drd.library.ViewPagerTransformer;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by xbudi on 14/12/2017.
 */

public class PreviewImageGenericActivity extends AppCompatActivity {

    Activity activity;

    ArrayList<String> titles=new ArrayList<>();
    ArrayList<String> details=new ArrayList<>();
    int pos;
    String folder;
    int color;
    LinearLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preview_image);
        activity=this;
        PublicFunction.setStatusBarColor(this);
        titles=(ArrayList<String>)getIntent().getSerializableExtra("Titles");
        details=(ArrayList<String>)getIntent().getSerializableExtra("Images");
        pos=getIntent().getIntExtra("Pos",0);
        folder=getIntent().getStringExtra("Folder");
        color=getIntent().getIntExtra("BackColor",Color.BLACK);
        mainLayout=findViewById(R.id.mainLayout);
        mainLayout.setBackgroundColor(color);

        bindPagerImage();
    }



    private void bindPagerImage()
    {
        ViewPager vp = (ViewPager)findViewById(R.id.pagerImage);
        PagerImageAdapter vpAdapter=new PagerImageAdapter(this, details, titles);
        vp.setPageTransformer(true, new ViewPagerTransformer(). new DepthPage());
        vp.setAdapter(vpAdapter);

        CirclePageIndicator indicator=(CirclePageIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(vp);
        indicator.setFillColor(Color.RED);
        if (details.size()<=1)
            indicator.setVisibility(View.INVISIBLE);
        else
            indicator.setVisibility(View.VISIBLE);

        vp.setCurrentItem(pos);
    }

    public class PagerImageAdapter extends PagerAdapter implements OnTouchListener{

        // these matrices will be used to move and zoom image
        private Matrix matrix = new Matrix();
        private Matrix savedMatrix = new Matrix();
        // we can be in one of these 3 states
        private static final int NONE = 0;
        private static final int DRAG = 1;
        private static final int ZOOM = 2;
        private int mode = NONE;
        // remember some things for zooming
        private PointF start = new PointF();
        private PointF mid = new PointF();
        private float oldDist = 1f;
        private float d = 0f;
        private float newRot = 0f;
        private float[] lastEvent = null;

        private Context mContext;
        ArrayList<String> images;
        ArrayList<String> titles;

        /*public PagerImageAdapter(Context context, ArrayList<String> images) {
            mContext = context;
            this.images=images;
            this.titles=null;
        }*/

        public PagerImageAdapter(Context context, ArrayList<String> images, ArrayList<String> titles) {
            mContext = context;
            this.images=images;
            this.titles=titles;
        }
        @Override
        public Object instantiateItem(ViewGroup collection, int position) {
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.pager_item_full, collection, false);

            ImageView typeImage =  itemView.findViewById(R.id.img_pager_item);
            TextView title = itemView.findViewById(R.id.title);
            //typeImage.setOnTouchListener(this);

            PhotoViewAttacher photoAttacher;
            photoAttacher= new PhotoViewAttacher(typeImage);
            photoAttacher.update();

            String path = MainApplication.getUrlApplWeb() + folder + images.get(position);
            Picasso.with(activity)
                    .load(path)
                    .placeholder(R.drawable.nopicture)
                    .error(R.drawable.nopicture)
                    .into(typeImage);

            if (titles!=null)
                title.setText(titles.get(position));
            else
                title.setText("");

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


        public boolean onTouch(View v, MotionEvent event) {
            // handle touch events here
            ImageView view = (ImageView) v;
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    savedMatrix.set(matrix);
                    start.set(event.getX(), event.getY());
                    mode = DRAG;
                    lastEvent = null;
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    oldDist = spacing(event);
                    if (oldDist > 10f) {
                        savedMatrix.set(matrix);
                        midPoint(mid, event);
                        mode = ZOOM;
                    }
                    lastEvent = new float[4];
                    lastEvent[0] = event.getX(0);
                    lastEvent[1] = event.getX(1);
                    lastEvent[2] = event.getY(0);
                    lastEvent[3] = event.getY(1);
                    d = rotation(event);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                    mode = NONE;
                    lastEvent = null;
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (mode == DRAG) {
                        matrix.set(savedMatrix);
                        float dx = event.getX() - start.x;
                        float dy = event.getY() - start.y;
                        matrix.postTranslate(dx, dy);
                    } else if (mode == ZOOM) {
                        float newDist = spacing(event);
                        if (newDist > 10f) {
                            matrix.set(savedMatrix);
                            float scale = (newDist / oldDist);
                            matrix.postScale(scale, scale, mid.x, mid.y);
                        }
                        if (lastEvent != null && event.getPointerCount() == 3) {
                            newRot = rotation(event);
                            float r = newRot - d;
                            float[] values = new float[9];
                            matrix.getValues(values);
                            float tx = values[2];
                            float ty = values[5];
                            float sx = values[0];
                            float xc = (view.getWidth() / 2) * sx;
                            float yc = (view.getHeight() / 2) * sx;
                            matrix.postRotate(r, tx + xc, ty + yc);
                        }
                    }
                    break;
            }

            view.setImageMatrix(matrix);
            return true;
        }

        /**
         * Determine the space between the first two fingers
         */
        private float spacing(MotionEvent event) {
            float x = event.getX(0) - event.getX(1);
            float y = event.getY(0) - event.getY(1);

            //return FloatMath.sqrt(x * x + y * y);
            return (float)Math.sqrt(x * x + y * y);
        }

        /**
         * Calculate the mid point of the first two fingers
         */
        private void midPoint(PointF point, MotionEvent event) {
            float x = event.getX(0) + event.getX(1);
            float y = event.getY(0) + event.getY(1);
            point.set(x / 2, y / 2);
        }

        /**
         * Calculate the degree to be rotated by.
         *
         * @param event
         * @return Degrees
         */
        private float rotation(MotionEvent event) {
            double delta_x = (event.getX(0) - event.getX(1));
            double delta_y = (event.getY(0) - event.getY(1));
            double radians = Math.atan2(delta_y, delta_x);
            return (float) Math.toDegrees(radians);
        }

    }
}
