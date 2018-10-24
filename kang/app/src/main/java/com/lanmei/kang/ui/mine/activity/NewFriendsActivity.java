package com.lanmei.kang.ui.mine.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.NewFriendsAdapter;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.bean.NewFriendsBean;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.DividerItemDecoration;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import butterknife.InjectView;

/**
 * (好友)新的朋友
 */
public class NewFriendsActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;

    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;

    NewFriendsAdapter mAdapter;

    @Override
    public int getContentViewId() {
        return R.layout.activity_single_listview;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.new_friends);
        actionbar.setHomeAsUpIndicator(R.mipmap.back_g);

        initSwipeRefreshLayout();
    }


    private void initSwipeRefreshLayout() {
        smartSwipeRefreshLayout.initWithLinearLayout();
        smartSwipeRefreshLayout.getRecyclerView().addItemDecoration(new DividerItemDecoration(this));

        KangQiMeiApi api = new KangQiMeiApi("friend/index");
        api.add("uid",api.getUserId(this));
        api.add("type",1);//1、新朋友 2、感兴趣的
        api.add("token",api.getToken(this));

        mAdapter = new NewFriendsAdapter(this);
        smartSwipeRefreshLayout.setAdapter(mAdapter);
        SwipeRefreshController<NoPageListBean<NewFriendsBean>> controller = new SwipeRefreshController<NoPageListBean<NewFriendsBean>>(this, smartSwipeRefreshLayout, api, mAdapter) {
        };
        controller.loadFirstPage();
    }



}
