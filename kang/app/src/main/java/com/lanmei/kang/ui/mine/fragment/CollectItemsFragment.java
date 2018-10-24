package com.lanmei.kang.ui.mine.fragment;

import android.os.Bundle;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.MyCollectItemsAdapter;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.bean.MerchantDetailsBean;
import com.lanmei.kang.event.CollectItemsEvent;
import com.xson.common.app.BaseFragment;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.widget.DividerItemDecoration;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.InjectView;


/**
 * Created by Administrator on 2017/4/27.
 * 收藏（服务）
 */

public class CollectItemsFragment extends BaseFragment {


    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    MyCollectItemsAdapter mAdapter;
    private SwipeRefreshController<NoPageListBean<MerchantDetailsBean.GoodsBean>> controller;

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

    private void initSwipeRefreshLayout() {
        smartSwipeRefreshLayout.initWithLinearLayout();
        smartSwipeRefreshLayout.getRecyclerView().addItemDecoration(new DividerItemDecoration(context));

        KangQiMeiApi api = new KangQiMeiApi("member/favour");
        api.add("uid",api.getUserId(context));
        mAdapter = new MyCollectItemsAdapter(context);
        smartSwipeRefreshLayout.setAdapter(mAdapter);
        controller = new SwipeRefreshController<NoPageListBean<MerchantDetailsBean.GoodsBean>>(context, smartSwipeRefreshLayout, api, mAdapter) {
        };
        controller.loadFirstPage();
    }

    //收藏或取消收藏服务是调用
    @Subscribe
    public void collectItemsEvent(CollectItemsEvent event) {
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
