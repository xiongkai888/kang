package com.lanmei.kang.ui.mine.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.MyCollectNewsAdapter;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.bean.CollectNewsBean;
import com.lanmei.kang.event.CollectNewsEvent;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.DividerItemDecoration;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.InjectView;


/**
 * 我的收藏
 */
public class MyCollectActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
//    @InjectView(R.id.viewPager)
//    ViewPager mViewPager;
//    @InjectView(R.id.tabLayout)
//    TabLayout mTabLayout;

    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    MyCollectNewsAdapter mAdapter;
    private SwipeRefreshController<NoPageListBean<CollectNewsBean>> controller;

    @Override
    public int getContentViewId() {
//        return R.layout.activity_my_collect;
        return R.layout.activity_single_listview;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle("资讯收藏");
        actionbar.setHomeAsUpIndicator(R.mipmap.back_g);

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

//        mViewPager.setAdapter(new MyCollectAdapter(getSupportFragmentManager()));
//        mViewPager.setOffscreenPageLimit(2);
//        mTabLayout.setupWithViewPager(mViewPager);

        smartSwipeRefreshLayout.initWithLinearLayout();
        smartSwipeRefreshLayout.getRecyclerView().addItemDecoration(new DividerItemDecoration(this));
        KangQiMeiApi api = new KangQiMeiApi("post/favour");
        api.add("uid",api.getUserId(this));
        mAdapter = new MyCollectNewsAdapter(this);
        smartSwipeRefreshLayout.setAdapter(mAdapter);
        controller = new SwipeRefreshController<NoPageListBean<CollectNewsBean>>(this, smartSwipeRefreshLayout, api, mAdapter) {
        };
        controller.loadFirstPage();
    }

    //资讯收藏事件
    @Subscribe
    public void newsCollectEvent(CollectNewsEvent event) {
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
