package com.lanmei.kang.ui.mine.fragment;

import android.os.Bundle;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.GoodFriendsSubAdapter;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.bean.GoodFriendsBean;
import com.lanmei.kang.event.AddFriendsEvent;
import com.lanmei.kang.event.FollowInNewFriendEvent;
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
 * Created by Administrator on 2017/7/5.
 * 我的好友  好友
 */

public class GoodFriendsFragment extends BaseFragment {


    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    GoodFriendsSubAdapter mAdapter;
    private SwipeRefreshController<NoPageListBean<GoodFriendsBean>> controller;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_single_listview;
    }

    public static GoodFriendsFragment newInstance() {
        Bundle args = new Bundle();
        GoodFriendsFragment fragment = new GoodFriendsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initSwipeRefreshLayout();
    }

    private void initSwipeRefreshLayout() {
        initListView();
    }

    private void initListView() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        smartSwipeRefreshLayout.initWithLinearLayout();
        smartSwipeRefreshLayout.getRecyclerView().addItemDecoration(new DividerItemDecoration(context));
        KangQiMeiApi api = new KangQiMeiApi("friend/index");
        api.add("uid",api.getUserId(context));
        api.add("token",api.getToken(context));

        mAdapter = new GoodFriendsSubAdapter(context);
        smartSwipeRefreshLayout.setAdapter(mAdapter);
        controller = new SwipeRefreshController<NoPageListBean<GoodFriendsBean>>(context, smartSwipeRefreshLayout, api, mAdapter) {
        };
        controller.loadFirstPage();
    }

    //加好友成功时调用
    @Subscribe
    public void addFriendsEvent(AddFriendsEvent event) {
        if (controller != null) {
            controller.loadFirstPage();
        }
    }

    //新的朋友点击关注时候调用(或搜索好友时)
    @Subscribe
    public void setFollowByUidEvent(FollowInNewFriendEvent event) {
        if (StringUtils.isEmpty(mAdapter)){
            return;
        }
        List<GoodFriendsBean> list = mAdapter.getData();
        if (StringUtils.isEmpty(list)) {
            return;
        }
        String id = event.getUid();
        for (GoodFriendsBean bean : list) {
            if (!StringUtils.isEmpty(bean) && StringUtils.isSame(bean.getId(),id)) {
                bean.setFollowed(event.getFollow());
                mAdapter.notifyDataSetChanged();
                return;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
