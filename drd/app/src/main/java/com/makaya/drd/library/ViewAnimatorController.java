package com.makaya.drd.library;

import android.app.Activity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ViewAnimator;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by xbudi on 20/09/2016.
 */
public class ViewAnimatorController {

    ViewAnimator viewAnimator;
    Animation slide_in_left, slide_out_right;
    Activity activity;
    int viewAnimatorId;

    public ViewAnimatorController(Activity activity, int viewAnimatorId) {
        this.activity=activity;
        this.viewAnimatorId=viewAnimatorId;
    }

    public void initViewAnimator(ArrayList<Integer> images)
    {
        viewAnimator = (ViewAnimator) activity.findViewById(viewAnimatorId); // get the reference of ViewAnimator
        for (int i = 0; i < images.size(); i++) {
            ImageView imageView = new ImageView(activity); // create a new object  for ImageView
            imageView.setImageResource(images.get(i)); // set image resource for ImageView
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewAnimator.addView(imageView); // add child view in ViewAnimator
        }

        if (images.size()==1)
            return;

        /*slide_in_left = AnimationUtils.loadAnimation(activity, android.R.anim.slide_in_left);
        slide_out_right = AnimationUtils.loadAnimation(activity, android.R.anim.slide_out_right);
        slide_in_left.setDuration(1000);
        slide_out_right.setDuration(1000);
        viewAnimator.setInAnimation(slide_in_left);
        viewAnimator.setOutAnimation(slide_out_right);*/

        Timer timer=new Timer();
        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        viewAnimator.showNext();
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(task, 0, 10000);
    }

    public void initViewAnimatorImg(ArrayList<ImageView> images)
    {

        viewAnimator = (ViewAnimator) activity.findViewById(viewAnimatorId); // get the reference of ViewAnimator
        for (int i = 0; i < images.size(); i++) {
            //images.get(i).setScaleType(ImageView.ScaleType.FIT_XY);
            viewAnimator.addView(images.get(i)); // add child view in ViewAnimator
        }

        if (images.size()==1)
            return;

        slide_in_left = AnimationUtils.loadAnimation(activity, android.R.anim.slide_in_left);
        slide_out_right = AnimationUtils.loadAnimation(activity, android.R.anim.slide_out_right);
        slide_in_left.setDuration(1000);
        slide_out_right.setDuration(1000);
        viewAnimator.setInAnimation(slide_in_left);
        viewAnimator.setOutAnimation(slide_out_right);

        Timer timer=new Timer();
        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        viewAnimator.showNext();
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(task, 0, 5000);
    }
}
