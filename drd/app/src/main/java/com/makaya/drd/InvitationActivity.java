package com.makaya.drd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.makaya.drd.domain.MemberLogin;
import com.makaya.drd.library.PublicFunction;

import java.util.ArrayList;

/**
 * Created by xbudi on 20/10/2016.
 */

public class InvitationActivity extends AppCompatActivity {
    Activity activity;
    TabLayout tabLayout;
    ViewPager tabViewPager;
    ArrayList<String> tabTexts=new ArrayList<>();
    ArrayList<Fragment> fragments;

    SessionManager session;
    MemberLogin user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invitation);
        activity=this;
        fragments=new ArrayList<>();
        fragments.add(new InvitationInviteFragment());
        fragments.add(new InvitationInvitationFragment());
        session=new SessionManager(getApplication());
        user=session.getUserLogin();
        PublicFunction.setHeaderStatus(activity, "My Contact", new PublicFunction.BackCallbackInterface() {
            @Override
            public void onClickBack() {
                onBackPressed();
            }
        });

        initObject();
        bindTab();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;

        /*if (requestCode == 1){
            current_page = 1;
            stamps =new ArrayList<>();
            loadingInProgress=true;
            fetchData(user.Id, topCriteria, current_page);
        }*/
    }

    private void initObject()
    {
        tabLayout= findViewById(R.id.tabLayout);
        tabViewPager= findViewById(R.id.tabViewPager);

    }

    private void bindTab()
    {
        tabLayout = findViewById(R.id.tabLayout);
        tabViewPager = findViewById(R.id.tabViewPager);
        for (int i=0;i<tabLayout.getTabCount();i++){
            tabTexts.add(tabLayout.getTabAt(i).getText().toString());
        }

        TabAdapter adapter = new TabAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        tabViewPager.setAdapter(adapter);
        tabViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setupWithViewPager(tabViewPager);
    }

    public void refreshAllTab()
    {
        InvitationInviteFragment obj1 =(InvitationInviteFragment)fragments.get(0);
        obj1.refresh();
        InvitationInvitationFragment obj2 =(InvitationInvitationFragment)fragments.get(1);
        obj2.refresh();
    }

    public class TabAdapter extends FragmentStatePagerAdapter {
        int numTab;

        public TabAdapter(FragmentManager fm, int numTab) {
            super(fm);
            this.numTab = numTab;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return fragments.get(0);
                case 1:
                    return fragments.get(1);

                default:
                    return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTexts.get(position);
        }

        @Override
        public int getCount() {
            return numTab;
        }
    }

}
