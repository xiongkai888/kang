package com.lanmei.kang.ui.mine.fragment;

import android.os.Bundle;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.GoodsOrderListAdapter;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.bean.GoodsOrderListBean;
import com.lanmei.kang.event.OrderOperationEvent;
import com.xson.common.app.BaseFragment;
import com.xson.common.bean.BaseBean;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.InjectView;


/**
 * Created by Administrator on 2017/4/27.
 * 商品订单列表
 */

public class GoodsOrderListFragment extends BaseFragment {

    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    private SwipeRefreshController<NoPageListBean<GoodsOrderListBean>> controller;

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
        KangQiMeiApi api = new KangQiMeiApi("app/order_list");
        api.add("uid", api.getUserId(context));
        api.add("state", getArguments().getInt("state"));//0全部1待付款2已付款3未消费4已完成
        GoodsOrderListAdapter adapter = new GoodsOrderListAdapter(context);
        adapter.setOrderAlterListener(new GoodsOrderListAdapter.OrderAlterListener() {
            @Override
            public void affirm(String status, String oid) {
                statusAffirm(status, oid);
            }
        });
        smartSwipeRefreshLayout.setAdapter(adapter);
        controller = new SwipeRefreshController<NoPageListBean<GoodsOrderListBean>>(context, smartSwipeRefreshLayout, api, adapter) {
        };
        controller.loadFirstPage();
    }

    //修改订单状态
    private void statusAffirm(String status, String oid) {
        KangQiMeiApi api = new KangQiMeiApi("app/status_save");
        api.add("id",oid);
        api.add("uid",api.getUserId(context));
        api.add("status",status);
        HttpClient.newInstance(context).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (controller != null){
//                    controller.loadFirstPage();
                    UIHelper.ToastMessage(context,response.getInfo());
                    EventBus.getDefault().post(new OrderOperationEvent());
                }
            }
        });
    }

    @Subscribe
    public void orderOperationEvent(OrderOperationEvent event) {
        controller.loadFirstPage(SmartSwipeRefreshLayout.Mode.BOTH);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
