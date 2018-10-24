package com.lanmei.kang.ui.mine.fragment;

import android.os.Bundle;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.GoodsOrderListAdapter;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.bean.GoodsOrderListBean;
import com.xson.common.app.BaseFragment;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import butterknife.InjectView;


/**
 * Created by Administrator on 2017/4/27.
 * 商品订单列表
 */

public class GoodsOrderListFragment extends BaseFragment {

    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_single_listview;
    }


    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initSwipeRefreshLayout();
//        if (!EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().register(this);
//        }
    }

    private void initSwipeRefreshLayout() {
        smartSwipeRefreshLayout.initWithLinearLayout();
        KangQiMeiApi api = new KangQiMeiApi("app/order_list");
        api.add("uid",api.getUserId(context));
        api.add("state",getArguments().getInt("state"));//0全部1待付款2已付款3未消费4已完成
        GoodsOrderListAdapter adapter = new GoodsOrderListAdapter(context);
        smartSwipeRefreshLayout.setAdapter(adapter);
        SwipeRefreshController<NoPageListBean<GoodsOrderListBean>> controller = new SwipeRefreshController<NoPageListBean<GoodsOrderListBean>>(context, smartSwipeRefreshLayout, api, adapter) {
        };
        controller.loadFirstPage();
    }

//    @Subscribe
//    public void orderOperationEvent(OrderOperationEvent event) {
//        if (controller != null){//简单暴力
//            controller.loadFirstPage();
//        }
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
    }

}
