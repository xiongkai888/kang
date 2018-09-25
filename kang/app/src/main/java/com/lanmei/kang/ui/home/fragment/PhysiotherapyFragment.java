package com.lanmei.kang.ui.home.fragment;

import android.os.Bundle;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.PhysiotherapyTabAdapter;
import com.lanmei.kang.api.MerchantListApi;
import com.lanmei.kang.bean.MerchantListBean;
import com.lanmei.kang.util.SharedAccount;
import com.xson.common.app.BaseFragment;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.widget.DividerItemDecoration;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import butterknife.InjectView;


/**
 * Created by Administrator on 2017/4/27.
 * 理疗界面（中医、西医、中西、高端理疗）
 */

public class PhysiotherapyFragment extends BaseFragment {
    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout refreshLayout;

    PhysiotherapyTabAdapter mAdapter;
    private SwipeRefreshController<NoPageListBean<MerchantListBean>> controller;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_single_listview;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        refreshLayout.initWithLinearLayout();
        refreshLayout.getRecyclerView().addItemDecoration(new DividerItemDecoration(context));
        MerchantListApi api = new MerchantListApi();
        api.cid = bundle.getString("cid");
        api.order = bundle.getString("order");
        api.lat = SharedAccount.getInstance(context).getLat();
        api.lon = SharedAccount.getInstance(context).getLon();
        mAdapter = new PhysiotherapyTabAdapter(context);
        refreshLayout.setAdapter(mAdapter);
        controller = new SwipeRefreshController<NoPageListBean<MerchantListBean>>(context, refreshLayout, api, mAdapter) {
        };
        controller.loadFirstPage();
    }
}
