package com.lanmei.kang.ui.merchant.fragment;

import android.os.Bundle;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.OrderListMerchantAdapter;
import com.lanmei.kang.api.OrderDelApi;
import com.lanmei.kang.api.OrderListMerchantApi;
import com.lanmei.kang.api.ReserveOrderApi;
import com.lanmei.kang.bean.OrderListMerchantBean;
import com.lanmei.kang.event.ScanSucceedEvent;
import com.lanmei.kang.helper.UserHelper;
import com.xson.common.app.BaseFragment;
import com.xson.common.bean.BaseBean;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.DividerItemDecoration;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.InjectView;


/**
 * Created by Administrator on 2017/4/27.
 * 商家订单列表
 */

public class MerchantOrderListFragment extends BaseFragment {


    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    OrderListMerchantAdapter mAdapter;
    private SwipeRefreshController<NoPageListBean<OrderListMerchantBean>> controller;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_single_listview;
    }


    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initSwipeRefreshLayout();
    }

    private void initSwipeRefreshLayout() {

        smartSwipeRefreshLayout.initWithLinearLayout();
        smartSwipeRefreshLayout.getRecyclerView().addItemDecoration(new DividerItemDecoration(context));

        OrderListMerchantApi api = new OrderListMerchantApi();
        Bundle bundle = getArguments();
        if (bundle != null) {
            api.status = bundle.getString("status");
        }
        api.uid = api.getUserId(context);
        api.token = UserHelper.getInstance(context).getToken();
        mAdapter = new OrderListMerchantAdapter(context);
        smartSwipeRefreshLayout.setAdapter(mAdapter);
        controller = new SwipeRefreshController<NoPageListBean<OrderListMerchantBean>>(context, smartSwipeRefreshLayout, api, mAdapter) {
        };
        controller.loadFirstPage();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    private void ajaxDelOrder(String id) {//删除订单
        HttpClient httpClient = HttpClient.newInstance(context);
        OrderDelApi api = new OrderDelApi();
        api.id = id;
        api.token = UserHelper.getInstance(context).getToken();
        httpClient.loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (context == null){
                    return;
                }
                UIHelper.ToastMessage(context,getString(R.string.delete_succeed));
            }
        });
    }

    private void ajaxCancelOrder(String id,String status) {//取消订单
        HttpClient httpClient = HttpClient.newInstance(context);
        ReserveOrderApi api = new ReserveOrderApi();
        api.status = status;//1、下单 2、确认订单 3、取消订单 4、作废 5、完成
        api.token = UserHelper.getInstance(context).getToken();
        httpClient.loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (context == null){
                    return;
                }
                UIHelper.ToastMessage(context,response.getInfo().toString());
            }
        });
    }

    @Subscribe
    public void scanSucceedEvent(ScanSucceedEvent event) {
        if (controller != null){
            controller.loadFirstPage();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


}
