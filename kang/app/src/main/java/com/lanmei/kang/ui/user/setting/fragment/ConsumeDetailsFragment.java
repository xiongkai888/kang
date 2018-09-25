package com.lanmei.kang.ui.user.setting.fragment;

import android.os.Bundle;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.TopUpAdapter;
import com.lanmei.kang.api.TopUpApi;
import com.lanmei.kang.bean.RechargeResultBean;
import com.xson.common.app.BaseFragment;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.widget.DividerItemDecoration;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import butterknife.InjectView;


/**
 * Created by Administrator on 2017/4/27.
 * 消费明细
 */

public class ConsumeDetailsFragment extends BaseFragment {


    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;

    TopUpAdapter mAdapter;
    private SwipeRefreshController<NoPageListBean<RechargeResultBean>> controller;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_consume_details;
    }

    public static ConsumeDetailsFragment newInstance() {
        Bundle args = new Bundle();
        ConsumeDetailsFragment fragment = new ConsumeDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initSwipeRefreshLayout();
    }
    private void initSwipeRefreshLayout() {
        smartSwipeRefreshLayout.initWithLinearLayout();
        smartSwipeRefreshLayout.getRecyclerView().addItemDecoration(new DividerItemDecoration(context));

        TopUpApi api = new TopUpApi();
        api.token = api.getToken(context);
        api.uid = api.getUserId(context);
        mAdapter = new TopUpAdapter(context);
        smartSwipeRefreshLayout.setAdapter(mAdapter);
        controller = new SwipeRefreshController<NoPageListBean<RechargeResultBean>>(context, smartSwipeRefreshLayout, api, mAdapter) {
        };
        controller.loadFirstPage();
    }
}
