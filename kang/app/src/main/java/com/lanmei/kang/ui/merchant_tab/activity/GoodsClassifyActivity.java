package com.lanmei.kang.ui.merchant_tab.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.GoodsClassifyAdapter;
import com.lanmei.kang.adapter.GoodsClassifyVerticalTabAdapter;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.bean.MerchantListBean;
import com.lanmei.kang.util.CommonUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import butterknife.InjectView;
import butterknife.OnClick;
import q.rorbin.verticaltablayout.VerticalTabLayout;
import q.rorbin.verticaltablayout.widget.TabView;

public class GoodsClassifyActivity extends BaseActivity {

    @InjectView(R.id.tablayout)
    VerticalTabLayout tablayout;//垂直tabLayout
    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;

    GoodsClassifyAdapter mAdapter;
    private SwipeRefreshController<NoPageListBean<MerchantListBean>> controller;

    @Override
    public int getContentViewId() {
        return R.layout.activity_goods_classify;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        smartSwipeRefreshLayout.setLayoutManager(new GridLayoutManager(this, 2));

        KangQiMeiApi api = new KangQiMeiApi("place/Placelist");
        mAdapter = new GoodsClassifyAdapter(this);
        smartSwipeRefreshLayout.setAdapter(mAdapter);
        controller = new SwipeRefreshController<NoPageListBean<MerchantListBean>>(this, smartSwipeRefreshLayout, api, mAdapter) {
        };
//        controller.loadFirstPage();
        mAdapter.notifyDataSetChanged();


        //垂直VerticalTab
        tablayout.setTabAdapter(new GoodsClassifyVerticalTabAdapter(this));
        tablayout.addOnTabSelectedListener(new VerticalTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabView tab, int position) {
//                CommonUtils.developing(getContext());
//                changeStatus(position);//改变订单状态（全部、未付款、代练中等）
            }

            @Override
            public void onTabReselected(TabView tab, int position) {

            }
        });
    }

    @OnClick({R.id.back_iv, R.id.search_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                onBackPressed();
                break;
            case R.id.search_tv:
                CommonUtils.developing(this);
                break;
        }
    }
}
