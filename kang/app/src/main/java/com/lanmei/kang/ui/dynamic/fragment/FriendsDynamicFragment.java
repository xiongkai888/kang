package com.lanmei.kang.ui.dynamic.fragment;

import android.os.Bundle;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.DynamicListAdapter;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.bean.DynamicBean;
import com.lanmei.kang.event.AttentionFriendEvent;
import com.lanmei.kang.event.DynamicLikedEvent;
import com.lanmei.kang.event.PublishDynamicEvent;
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
 * Created by xkai on 2018/1/15.
 * 好友的动态列表
 */

public class FriendsDynamicFragment extends BaseFragment {

    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    DynamicListAdapter mAdapter;
    private SwipeRefreshController<NoPageListBean<DynamicBean>> controller;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_single_listview;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initSwipeRefreshLayout();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    String uid;

    private void initSwipeRefreshLayout() {
        smartSwipeRefreshLayout.initWithLinearLayout();
        smartSwipeRefreshLayout.getRecyclerView().addItemDecoration(new DividerItemDecoration(context));

        Bundle bundle = getArguments();
        if (bundle != null){
            uid = bundle.getString("uid");
        }
        KangQiMeiApi api = new KangQiMeiApi("");
        api.add("uid",uid);
        api.add("id",api.getUserId(context));
        mAdapter = new DynamicListAdapter(context);
        mAdapter.setType(3);
        smartSwipeRefreshLayout.setAdapter(mAdapter);
        controller = new SwipeRefreshController<NoPageListBean<DynamicBean>>(context, smartSwipeRefreshLayout, api, mAdapter) {
        };
        controller.loadFirstPage();
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
        EventBus.getDefault().unregister(this);
    }


}
