package com.lanmei.kang.ui.mine.fragment;

import android.content.Intent;
import android.os.Bundle;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.DynamicListAdapter;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.bean.DynamicBean;
import com.lanmei.kang.event.AttentionFriendEvent;
import com.lanmei.kang.event.DynamicLikedEvent;
import com.lanmei.kang.event.PublishDynamicEvent;
import com.lanmei.kang.helper.ShareHelper;
import com.lanmei.kang.util.CommonUtils;
import com.xson.common.api.AbstractApi;
import com.xson.common.app.BaseFragment;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.utils.StringUtils;
import com.xson.common.widget.DividerItemDecoration;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.InjectView;

/**
 * Created by Administrator on 2017/4/25.
 * 达人 动态
 */

public class MyDynamicFragment extends BaseFragment {

    private String status;//是朋友圈还是达人动态

    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    DynamicListAdapter mAdapter;
    private ShareHelper mShareHelper;
    private SwipeRefreshController<NoPageListBean<DynamicBean>> controller;

    public static MyDynamicFragment newInstance() {
        Bundle args = new Bundle();
        MyDynamicFragment fragment = new MyDynamicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_single_listview;
    }

    private void initListView() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        Bundle bundle = getArguments();
        if (bundle != null) {
            status = bundle.getString("status");
        }
        smartSwipeRefreshLayout.initWithLinearLayout();
        smartSwipeRefreshLayout.getRecyclerView().addItemDecoration(new DividerItemDecoration(context));
        mAdapter = new DynamicListAdapter(context);
        smartSwipeRefreshLayout.setAdapter(mAdapter);
        KangQiMeiApi api = new KangQiMeiApi("posts/index");
        api.addParams("id",api.getUserId(context));
        if (com.xson.common.utils.StringUtils.isSame(status, CommonUtils.isZero)) {
            api.addParams("uid",api.getUserId(context));
        } else {
            api.addParams("type",status);
        }
        api.setMethod(AbstractApi.Method.GET);
        controller = new SwipeRefreshController<NoPageListBean<DynamicBean>>(context, smartSwipeRefreshLayout, api, mAdapter) {
        };
        controller.loadFirstPage();
        mAdapter.setShare(mShareHelper);
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        //分享初始化
        mShareHelper = new ShareHelper(context);
        // 监听发表帖子（动态）广播或者被关注时广播
        initListView();
    }

    //评论详情时或点赞时调用
    @Subscribe
    public void commEvent(DynamicLikedEvent event) {
        if (mAdapter != null) {
            List<DynamicBean> list = mAdapter.getData();
            if (StringUtils.isEmpty(list)) {
                return;
            }
            String id = event.getId();
            String commNum = event.getCommNum();
            String like = event.getLike();
            String liked = event.getLiked();
            int size = list.size();
            for (int i = 0; i < size; i++) {
                DynamicBean bean = list.get(i);
                if (StringUtils.isSame(id, bean.getId())) {
                    bean.setReviews(commNum);
                    bean.setLike(like);
                    bean.setLiked(liked);
                    mAdapter.notifyDataSetChanged();
                    return;
                }
            }

        }
    }

    //在好友动态详情中关注或取消关注该好友时调用
    @Subscribe
    public void attentionFriendEvent(AttentionFriendEvent event) {
        if (mAdapter != null) {
            List<DynamicBean> list = mAdapter.getData();
            if (StringUtils.isEmpty(list)) {
                return;
            }
            String uid = event.getUid();
            String followed = event.getFollowed();
            int size = list.size();
            for (int i = 0; i < size; i++) {
                DynamicBean bean = list.get(i);
                if (StringUtils.isSame(uid, bean.getUid())) {
                    bean.setFollowed(followed);
                }
            }
            mAdapter.notifyDataSetChanged();
        }
    }


    //发布动态成功或删除动态时调用
    @Subscribe
    public void publishDynamicEvent(PublishDynamicEvent event) {
        if (controller != null) {
            controller.loadFirstPage();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mShareHelper.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 结果返回
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mShareHelper.onActivityResult(requestCode, resultCode, data);
    }

}
