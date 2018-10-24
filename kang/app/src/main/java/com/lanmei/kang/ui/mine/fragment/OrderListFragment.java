package com.lanmei.kang.ui.mine.fragment;

import android.os.Bundle;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.OrderListAdapter;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.bean.OrderListBean;
import com.lanmei.kang.event.OrderOperationEvent;
import com.xson.common.app.BaseFragment;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.InjectView;


/**
 * Created by Administrator on 2017/4/27.
 * 订单列表
 */

public class OrderListFragment extends BaseFragment {

    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    private SwipeRefreshController<NoPageListBean<OrderListBean>> controller;

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
        String status = getArguments().getString("status");
        KangQiMeiApi api = new KangQiMeiApi("Reservation/index");
        api.add("uid",api.getUserId(context));
        api.add("token",api.getToken(context));
        api.add("status",status);//0全部1待付款2已付款3未消费4已完成
        OrderListAdapter adapter = new OrderListAdapter(context);
        smartSwipeRefreshLayout.setAdapter(adapter);
        controller = new SwipeRefreshController<NoPageListBean<OrderListBean>>(context, smartSwipeRefreshLayout, api, adapter) {
        };
        controller.loadFirstPage();
    }

    @Subscribe
    public void orderOperationEvent(OrderOperationEvent event) {
        if (controller != null){//简单暴力
            controller.loadFirstPage();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
