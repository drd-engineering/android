package com.makaya.drd.service;

import android.app.Activity;
import android.os.Handler;

import com.makaya.drd.MainApplication;
import com.makaya.drd.R;
import com.makaya.drd.domain.DocumentLiteRoot;
import com.makaya.drd.domain.InvitationResult;
import com.makaya.drd.domain.MemberDepositTrxRoot;
import com.makaya.drd.domain.MemberInvitedRoot;
import com.makaya.drd.domain.MemberLiteRoot;
import com.makaya.drd.domain.MemberLogin;
import com.makaya.drd.domain.MemberPlan;
import com.makaya.drd.domain.MemberTitleRoot;
import com.makaya.drd.domain.SubscriptionRegistry;
import com.makaya.drd.domain.SubscriptionRegistryResult;
import com.makaya.drd.library.DisplayToast;
import com.makaya.drd.library.PublicFunction;

/**
 * Created by xbudi on 28/09/2017.
 */

public class MemberService {

    // MY EVENT HANDLER

    private OnSetDataPostedListener onSetDataPostedListener;
    public interface OnSetDataPostedListener {
        public <T> void onDataPosted(T data, boolean isLoadedAllItems);
        public <T> void onDataError();
    }

    public void setOnDataPostedListener(OnSetDataPostedListener listener) {
        onSetDataPostedListener = listener;
    }

    // /MY EVENT HANDLER

    Activity activity;
    MainApplication global;
    Handler handler;

    public MemberService(Activity activity)
    {
        this.activity=activity;
        global=(MainApplication)activity.getApplication();
        handler=new Handler();
    }


    public void Logout(long memberId)
    {
        String url= String.format(activity.getString(R.string.api_member_logout),
                global.getUrlApplApi(), memberId);

        PostDataModelUrl posData = new PostDataModelUrl();
        posData.execute(url, Integer.class);
        posData.setOnDataPostedListener(new PostDataModelUrl.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data) {
                if (data==null){
                    if (onSetDataPostedListener != null) {
                        onSetDataPostedListener.onDataError();
                    }
                    handler.post(new DisplayToast(activity, activity.getResources().getString(R.string.save_data_problem)));
                    return;
                }
                if (onSetDataPostedListener != null) {
                    onSetDataPostedListener.onDataPosted((Integer)data, true);
                }
            }

            @Override
            public <T> void onPostedError(Exception data) {
                if (onSetDataPostedListener != null) {
                    onSetDataPostedListener.onDataError();
                }
                handler.post(new DisplayToast(activity, activity.getResources().getString(R.string.save_data_problem)));
            }
        });
    }


    public void updatePhotoProfile(long memberId, String filename)
    {
        String url= String.format(activity.getString(R.string.api_member_photo),
                global.getUrlApplApi(), memberId, filename);

        PostDataModelUrl posData = new PostDataModelUrl();
        posData.execute(url, Integer.class);
        posData.setOnDataPostedListener(new PostDataModelUrl.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data) {
                if (data==null){
                    if (onSetDataPostedListener != null) {
                        onSetDataPostedListener.onDataError();
                    }
                    handler.post(new DisplayToast(activity, activity.getResources().getString(R.string.save_data_problem)));
                    return;
                }
                if (onSetDataPostedListener != null) {
                    onSetDataPostedListener.onDataPosted(data, true);
                }
            }

            @Override
            public <T> void onPostedError(Exception data) {
                if (onSetDataPostedListener != null) {
                    onSetDataPostedListener.onDataError();
                }
                handler.post(new DisplayToast(activity, activity.getResources().getString(R.string.save_data_problem)));
            }
        });
    }

    public void updateImage(long memberId, String filename, int imageType)
    {
        String url= String.format(activity.getString(R.string.api_member_image),
                global.getUrlApplApi(), memberId, filename, imageType);

        PostDataModelUrl posData = new PostDataModelUrl();
        posData.execute(url, Integer.class);
        posData.setOnDataPostedListener(new PostDataModelUrl.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data) {
                if (data==null){
                    if (onSetDataPostedListener != null) {
                        onSetDataPostedListener.onDataError();
                    }
                    handler.post(new DisplayToast(activity, activity.getResources().getString(R.string.save_data_problem)));
                    return;
                }
                if (onSetDataPostedListener != null) {
                    onSetDataPostedListener.onDataPosted(data, true);
                }
            }

            @Override
            public <T> void onPostedError(Exception data) {
                if (onSetDataPostedListener != null) {
                    onSetDataPostedListener.onDataError();
                }
                handler.post(new DisplayToast(activity, activity.getResources().getString(R.string.save_data_problem)));
            }
        });
    }

    public void updateKtpNo(long memberId, String ktpNo)
    {
        String url= String.format(activity.getString(R.string.api_member_ktpno),
                global.getUrlApplApi(), memberId, ktpNo);

        PostDataModelUrl posData = new PostDataModelUrl();
        posData.execute(url, Integer.class);
        posData.setOnDataPostedListener(new PostDataModelUrl.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data) {
                if (data==null){
                    if (onSetDataPostedListener != null) {
                        onSetDataPostedListener.onDataError();
                    }
                    handler.post(new DisplayToast(activity, activity.getResources().getString(R.string.save_data_problem)));
                    return;
                }
                if (onSetDataPostedListener != null) {
                    onSetDataPostedListener.onDataPosted(data, true);
                }
            }

            @Override
            public <T> void onPostedError(Exception data) {
                if (onSetDataPostedListener != null) {
                    onSetDataPostedListener.onDataError();
                }
                handler.post(new DisplayToast(activity, activity.getResources().getString(R.string.save_data_problem)));
            }
        });
    }

    public void getMemberTitle()
    {
        String url= String.format(activity.getString(R.string.api_member_title),
                global.getUrlApplApi());

        PostDataModelUrl posData = new PostDataModelUrl();
        posData.execute(url, MemberTitleRoot.class);
        posData.setOnDataPostedListener(new PostDataModelUrl.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data) {
                if (data==null){
                    if (onSetDataPostedListener != null) {
                        onSetDataPostedListener.onDataError();
                    }
                    handler.post(new DisplayToast(activity, activity.getResources().getString(R.string.fetch_data_problem)));
                    return;
                }
                if (onSetDataPostedListener != null) {
                    MemberTitleRoot root=((MemberTitleRoot) data);
                    onSetDataPostedListener.onDataPosted(root.Root, true);
                }
            }

            @Override
            public <T> void onPostedError(Exception data) {
                if (onSetDataPostedListener != null) {
                    onSetDataPostedListener.onDataError();
                }
                handler.post(new DisplayToast(activity, activity.getResources().getString(R.string.fetch_data_problem)));
            }
        });
    }

    public void getPlan(long memberId)
    {
        String url= String.format(activity.getString(R.string.api_member_plan),
                global.getUrlApplApi(), memberId);

        PostDataModelUrl posData = new PostDataModelUrl();
        posData.execute(url, MemberPlan.class);
        posData.setOnDataPostedListener(new PostDataModelUrl.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data) {
                if (data==null){
                    if (onSetDataPostedListener != null) {
                        onSetDataPostedListener.onDataError();
                    }
                    handler.post(new DisplayToast(activity, activity.getResources().getString(R.string.fetch_data_problem)));
                    return;
                }
                if (onSetDataPostedListener != null)
                    onSetDataPostedListener.onDataPosted(data, true);
            }

            @Override
            public <T> void onPostedError(Exception data) {
                if (onSetDataPostedListener != null) {
                    onSetDataPostedListener.onDataError();
                }
                handler.post(new DisplayToast(activity, activity.getResources().getString(R.string.fetch_data_problem)));
            }
        });
    }

    public void getInvitationList(long memberId, String topCriteria, int current_page)
    {
        String url= String.format(activity.getString(R.string.api_invitation_invitation),
                global.getUrlApplApi(), memberId, topCriteria, current_page,
                MainApplication.getPageSize());

        PostDataModelUrl posData = new PostDataModelUrl();
        posData.execute(url, MemberInvitedRoot.class);
        posData.setOnDataPostedListener(new PostDataModelUrl.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data) {
                if (data==null){
                    if (onSetDataPostedListener != null) {
                        onSetDataPostedListener.onDataError();
                    }
                    handler.post(new DisplayToast(activity, activity.getResources().getString(R.string.fetch_data_problem)));
                    return;
                }
                if (onSetDataPostedListener != null) {
                    MemberInvitedRoot root=((MemberInvitedRoot) data);
                    onSetDataPostedListener.onDataPosted(root, (root.Root).size() == 0);
                }
            }

            @Override
            public <T> void onPostedError(Exception data) {
                if (onSetDataPostedListener != null) {
                    onSetDataPostedListener.onDataError();
                }
                handler.post(new DisplayToast(activity, activity.getResources().getString(R.string.fetch_data_problem)));
            }
        });
    }

    public void getInvitedList(long memberId, String topCriteria, int current_page)
    {
        String url= String.format(activity.getString(R.string.api_invitation_list),
                global.getUrlApplApi(), memberId, topCriteria, current_page,
                MainApplication.getPageSize());

        PostDataModelUrl posData = new PostDataModelUrl();
        posData.execute(url, MemberInvitedRoot.class);
        posData.setOnDataPostedListener(new PostDataModelUrl.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data) {
                if (data==null){
                    if (onSetDataPostedListener != null) {
                        onSetDataPostedListener.onDataError();
                    }
                    handler.post(new DisplayToast(activity, activity.getResources().getString(R.string.fetch_data_problem)));
                    return;
                }
                if (onSetDataPostedListener != null) {
                    MemberInvitedRoot root=((MemberInvitedRoot) data);
                    onSetDataPostedListener.onDataPosted(root, (root.Root).size() == 0);
                }
            }

            @Override
            public <T> void onPostedError(Exception data) {
                if (onSetDataPostedListener != null) {
                    onSetDataPostedListener.onDataError();
                }
                handler.post(new DisplayToast(activity, activity.getResources().getString(R.string.fetch_data_problem)));
            }
        });
    }

    public void fetchContact(long userId, String topCriteria, int page)
    {
        String url=String.format(activity.getString(R.string.api_member_contact),
                MainApplication.getUrlApplApi(), userId, topCriteria, page, MainApplication.getPageSize());
        final com.makaya.xchat.library.PostDataModelUrl posData = new com.makaya.xchat.library.PostDataModelUrl();
        posData.execute(url, MemberLiteRoot.class);
        posData.setOnDataPostedListener(new com.makaya.xchat.library.PostDataModelUrl.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data) {
                if (data==null){
                    if (onSetDataPostedListener != null) {
                        onSetDataPostedListener.onDataError();
                    }
                    handler.post(new com.makaya.xchat.library.DisplayToast(activity, activity.getResources().getString(com.makaya.xchat.R.string.fetch_data_problem)));
                    return;
                }
                if (onSetDataPostedListener != null) {
                    MemberLiteRoot root=((MemberLiteRoot) data);
                    onSetDataPostedListener.onDataPosted(root, (root.Root).size() == 0);
                }
            }

            @Override
            public <T> void onPostedError(Exception data) {
                if (onSetDataPostedListener != null) {
                    onSetDataPostedListener.onDataError();
                }
                handler.post(new com.makaya.xchat.library.DisplayToast(activity, activity.getResources().getString(com.makaya.xchat.R.string.fetch_data_problem)));
            }
        });
    }

    public void checkInvitation(long memberId, String email)
    {
        String url= String.format(activity.getString(R.string.api_invitation_check),
                global.getUrlApplApi(), memberId, email);

        PostDataModelUrl posData = new PostDataModelUrl();
        posData.execute(url, MemberLogin.class);
        posData.setOnDataPostedListener(new PostDataModelUrl.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data) {
                if (data==null){
                    if (onSetDataPostedListener != null) {
                        onSetDataPostedListener.onDataError();
                    }
                    handler.post(new DisplayToast(activity, activity.getResources().getString(R.string.fetch_data_problem)));
                    return;
                }
                if (onSetDataPostedListener != null) {
                    onSetDataPostedListener.onDataPosted(data, true);
                }
            }

            @Override
            public <T> void onPostedError(Exception data) {
                if (onSetDataPostedListener != null) {
                    onSetDataPostedListener.onDataError();
                }
                handler.post(new DisplayToast(activity, activity.getResources().getString(R.string.fetch_data_problem)));
            }
        });
    }

    public void save(long memberId, String email, int expiryDay, String domain)
    {
        String url= String.format(activity.getString(R.string.api_invitation_save),
                global.getUrlApplApi(), memberId, email, expiryDay, domain);

        PostDataModelUrl posData = new PostDataModelUrl();
        posData.execute(url, Long.class);
        posData.setOnDataPostedListener(new PostDataModelUrl.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data) {
                if (data==null){
                    if (onSetDataPostedListener != null) {
                        onSetDataPostedListener.onDataError();
                    }
                    handler.post(new DisplayToast(activity, activity.getResources().getString(R.string.save_data_problem)));
                    return;
                }
                if (onSetDataPostedListener != null) {
                    onSetDataPostedListener.onDataPosted(data, true);
                }
            }

            @Override
            public <T> void onPostedError(Exception data) {
                if (onSetDataPostedListener != null) {
                    onSetDataPostedListener.onDataError();
                }
                handler.post(new DisplayToast(activity, activity.getResources().getString(R.string.save_data_problem)));
            }
        });
    }

    public void validationPassword(long memberId, String pwd)
    {
        String url= String.format(activity.getString(R.string.api_member_validpwd),
                global.getUrlApplApi(), memberId, pwd);

        PostDataModelUrl posData = new PostDataModelUrl();
        posData.execute(url, Boolean.class);
        posData.setOnDataPostedListener(new PostDataModelUrl.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data) {
                if (data==null){
                    if (onSetDataPostedListener != null) {
                        onSetDataPostedListener.onDataError();
                    }
                    handler.post(new DisplayToast(activity, activity.getResources().getString(R.string.fetch_data_problem)));
                    return;
                }
                if (onSetDataPostedListener != null) {
                    onSetDataPostedListener.onDataPosted(data, true);
                }
            }

            @Override
            public <T> void onPostedError(Exception data) {
                if (onSetDataPostedListener != null) {
                    onSetDataPostedListener.onDataError();
                }
                handler.post(new DisplayToast(activity, activity.getResources().getString(R.string.fetch_data_problem)));
            }
        });
    }

    public void acceptInvitation(long id)
    {
        String url= String.format(activity.getString(R.string.api_invitation_accept),
                global.getUrlApplApi(), id);

        PostDataModelUrl posData = new PostDataModelUrl();
        posData.execute(url, InvitationResult.class);
        posData.setOnDataPostedListener(new PostDataModelUrl.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data) {
                if (data==null){
                    if (onSetDataPostedListener != null) {
                        onSetDataPostedListener.onDataError();
                    }
                    handler.post(new DisplayToast(activity, activity.getResources().getString(R.string.save_data_problem)));
                    return;
                }
                if (onSetDataPostedListener != null) {
                    onSetDataPostedListener.onDataPosted(data, true);
                }
            }

            @Override
            public <T> void onPostedError(Exception data) {
                if (onSetDataPostedListener != null) {
                    onSetDataPostedListener.onDataError();
                }
                handler.post(new DisplayToast(activity, activity.getResources().getString(R.string.save_data_problem)));
            }
        });
    }
}
