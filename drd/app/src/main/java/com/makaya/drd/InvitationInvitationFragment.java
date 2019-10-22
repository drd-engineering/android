package com.makaya.drd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.makaya.drd.domain.MemberInvited;
import com.makaya.drd.domain.MemberInvitedRoot;
import com.makaya.drd.domain.MemberLogin;
import com.makaya.drd.library.PublicFunction;
import com.makaya.drd.service.MemberService;
import com.paginate.Paginate;

import java.util.ArrayList;

/**
 * Created by xbudi on 20/10/2016.
 */

public class InvitationInvitationFragment extends Fragment {
    Activity activity;
    ArrayList<MemberInvited> members =new ArrayList<>();
    View view;

    FloatingActionButton fabAdd;
    SessionManager session;
    MemberLogin user;
    InvitationInvitationAdapter adapter;
    int screenWidth;
    TextView search;
    ImageView imgClear;
    String topCriteria="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.invitation_invitation, container, false);
        activity=getActivity();
        session=new SessionManager(activity);
        user=session.getUserLogin();
        initObject();
        bindRecyclerView();
        return view;
    }

    private void initObject()
    {
        fabAdd = view.findViewById(com.makaya.xchat.R.id.fabAdd);
        search= view.findViewById(com.makaya.xchat.R.id.search);
        imgClear= view.findViewById(com.makaya.xchat.R.id.imgClear);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activity, InvitationNewActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        imgClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setText("");
                refresh();
            }
        });
        search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    refresh();
                    return true;
                }
                return false;
            }
        });

    }

    public void refresh()
    {
        PublicFunction.hideKeyboard(activity);
        topCriteria=search.getText().toString();
        current_page = 1;
        members.clear();
        loadingInProgress=true;
        fetchData(user.Id, topCriteria, current_page);
    }

    private void bindRecyclerView()
    {
        RecyclerView rv = view.findViewById(com.makaya.xchat.R.id.rv);
        rv.setHasFixedSize(true);

        int numberOfColumns = 3;
        rv.setLayoutManager(new GridLayoutManager
                (activity,
                        numberOfColumns,
                        GridLayoutManager.VERTICAL, false));

        //GridLayoutManager layoutManager=new GridLayoutManager(activity, 3);
        //LinearLayoutManager llm = new LinearLayoutManager(this.getBaseContext());
        //rv.setLayoutManager(layoutManager);

        adapter = new InvitationInvitationAdapter(members, activity);
        rv.setAdapter(adapter);

        int paginateLoadingTriggerThreshold=getResources().getInteger(com.makaya.xchat.R.integer.paginateLoadingTriggerThreshold);
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
            fetchData(user.Id, topCriteria, current_page);
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

    private void fetchData(long userId, String topCriteria, int page)
    {
        MemberService svr=new MemberService(activity);
        svr.setOnDataPostedListener(new MemberService.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data, boolean isLoadedAllItems) {
                ArrayList<MemberInvited> datas=((MemberInvitedRoot) data).Root;

                current_page++;
                loadingInProgress=false;
                hasLoadedAllItems=isLoadedAllItems;

                members.addAll(datas);
                adapter.setData(members);
                adapter.notifyDataSetChanged();
            }

            @Override
            public <T> void onDataError() {

            }
        });
        svr.getInvitationList(userId, topCriteria, page);
    }

}
