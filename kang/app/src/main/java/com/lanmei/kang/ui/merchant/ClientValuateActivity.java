package com.lanmei.kang.ui.merchant;

import android.os.Bundle;
import android.support.v7.app.ActionBar;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.ClientValuateAdapter;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.bean.ClientValuateBean;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.DividerItemDecoration;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import butterknife.InjectView;

/**
 * 客户评价
 */
public class ClientValuateActivity extends BaseActivity {



    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;

    ClientValuateAdapter mAdapter;
    private SwipeRefreshController<NoPageListBean<ClientValuateBean>> controller;


    @Override
    public int getContentViewId() {
        return R.layout.activity_client_valuate;
    }


    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle("客户评价");
        actionbar.setHomeAsUpIndicator(R.mipmap.back_g);

        initSwipeRefreshLayout();

    }

    private void initSwipeRefreshLayout() {
        smartSwipeRefreshLayout.initWithLinearLayout();
        smartSwipeRefreshLayout.getRecyclerView().addItemDecoration(new DividerItemDecoration(this));

        KangQiMeiApi api = new KangQiMeiApi("PlaceReviews/index");
        api.addParams("mid",api.getUserId(this));

        mAdapter = new ClientValuateAdapter(this);
        smartSwipeRefreshLayout.setAdapter(mAdapter);
        controller = new SwipeRefreshController<NoPageListBean<ClientValuateBean>>(this, smartSwipeRefreshLayout, api, mAdapter) {
        };
        controller.loadFirstPage();
    }
}
