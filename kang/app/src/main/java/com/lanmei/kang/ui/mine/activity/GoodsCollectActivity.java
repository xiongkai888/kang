package com.lanmei.kang.ui.mine.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.GoodsListAdapter;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.bean.MerchantTabGoodsBean;
import com.lanmei.kang.event.CollectGoodsEvent;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.InjectView;


/**
 * 商品收藏
 */
public class GoodsCollectActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;

    @Override
    public int getContentViewId() {
        return R.layout.activity_single_listview_no;
    }

    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    private SwipeRefreshController<NoPageListBean<MerchantTabGoodsBean>> controller;


    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle("商品收藏");
        actionbar.setHomeAsUpIndicator(R.mipmap.back_g);

        initSwipeRefreshLayout();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }


    private void initSwipeRefreshLayout() {
        KangQiMeiApi api = new KangQiMeiApi("app/collection_list");
        api.add("userid",api.getUserId(this));
        GoodsListAdapter adapter = new GoodsListAdapter(this);
        smartSwipeRefreshLayout.setLayoutManager(new GridLayoutManager(this, 2));
        smartSwipeRefreshLayout.setAdapter(adapter);
        controller = new SwipeRefreshController<NoPageListBean<MerchantTabGoodsBean>>(this, smartSwipeRefreshLayout, api, adapter) {
        };
        controller.loadFirstPage();
    }


    //商品收藏事件
    @Subscribe
    public void collectGoodsEvent(CollectGoodsEvent event) {
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
