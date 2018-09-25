package com.lanmei.kang.ui.mine.fragment;

import android.os.Bundle;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.MyCollectMerchantAdapter;
import com.lanmei.kang.api.MyCollectMerchantApi;
import com.lanmei.kang.bean.CollectBean;
import com.lanmei.kang.event.CollectMerchantEvent;
import com.lanmei.kang.util.SharedAccount;
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
 * 收藏（商家）
 */

public class CollectMerchantFragment extends BaseFragment {


    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    MyCollectMerchantAdapter mAdapter;
    private SwipeRefreshController<NoPageListBean<CollectBean>> controller;

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

        MyCollectMerchantApi api = new MyCollectMerchantApi();
//        Bundle bundle = getArguments();
//        api.order = bundle.getString("order");
        api.uid = api.getUserId(context);
        api.lat = SharedAccount.getInstance(context).getLat();
        api.lon = SharedAccount.getInstance(context).getLon();
        mAdapter = new MyCollectMerchantAdapter(context);
        smartSwipeRefreshLayout.setAdapter(mAdapter);
        controller = new SwipeRefreshController<NoPageListBean<CollectBean>>(context, smartSwipeRefreshLayout, api, mAdapter) {
        };
        controller.loadFirstPage();
    }

    @Subscribe
    public void onClickCollectEvent(CollectMerchantEvent event) {
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
