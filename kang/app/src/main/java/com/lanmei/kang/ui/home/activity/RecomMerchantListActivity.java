package com.lanmei.kang.ui.home.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.PhysiotherapyTabAdapter;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.bean.MerchantListBean;
import com.lanmei.kang.util.SharedAccount;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.DividerItemDecoration;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import butterknife.InjectView;

/**
 * 推荐商家列表
 */
public class RecomMerchantListActivity extends BaseActivity {

    PhysiotherapyTabAdapter mAdapter;
    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;


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
        actionbar.setTitle("推荐商家");
        actionbar.setHomeAsUpIndicator(R.mipmap.back_g);

        initSwipeRefreshLayout();
    }


    private void initSwipeRefreshLayout() {
        smartSwipeRefreshLayout.initWithLinearLayout();
        smartSwipeRefreshLayout.getRecyclerView().addItemDecoration(new DividerItemDecoration(this));
        KangQiMeiApi api = new KangQiMeiApi("place/Placelist");
        api.add("lat",SharedAccount.getInstance(this).getLat());
        api.add("lon",SharedAccount.getInstance(this).getLon());
        api.add("more",1);

        mAdapter = new PhysiotherapyTabAdapter(this);
        smartSwipeRefreshLayout.setAdapter(mAdapter);
        SwipeRefreshController<NoPageListBean<MerchantListBean>> controller = new SwipeRefreshController<NoPageListBean<MerchantListBean>>(this, smartSwipeRefreshLayout, api, mAdapter) {
        };
        controller.loadFirstPage();
    }

}
