package com.lanmei.kang.ui.user.setting;

import android.os.Bundle;
import android.support.v7.app.ActionBar;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.MyIntegralAdapter;
import com.lanmei.kang.api.HomeApi;
import com.lanmei.kang.bean.HomeBean;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.ListBean;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.DividerItemDecoration;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import butterknife.InjectView;

/**
 * 我的积分
 */
public class MyIntegralActivity extends BaseActivity {

    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;

    MyIntegralAdapter mAdapter;
    private SwipeRefreshController<ListBean<HomeBean>> controller;


    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;


    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.my_integral);
        actionbar.setHomeAsUpIndicator(R.mipmap.back);

        initSwipeRefreshLayout();
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_my_integral;
    }


    private void initSwipeRefreshLayout() {
        smartSwipeRefreshLayout.initWithLinearLayout();
        smartSwipeRefreshLayout.getRecyclerView().addItemDecoration(new DividerItemDecoration(getContext()));

        HomeApi api = new HomeApi();
        mAdapter = new MyIntegralAdapter(this);
        smartSwipeRefreshLayout.setAdapter(mAdapter);
        controller = new SwipeRefreshController<ListBean<HomeBean>>(getContext(), smartSwipeRefreshLayout, api, mAdapter) {
        };
        mAdapter.notifyDataSetChanged();
    }

}
