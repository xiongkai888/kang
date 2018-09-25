package com.lanmei.kang.ui.home.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.HuoDongSubAdapter;
import com.lanmei.kang.api.NewsCategoryListApi;
import com.lanmei.kang.bean.NewsCategoryListBean;
import com.lanmei.kang.helper.ReceiverHelper;
import com.xson.common.app.BaseFragment;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.widget.DividerItemDecoration;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import butterknife.InjectView;

import static com.lanmei.kang.ui.details.CourseDetailsActivity.COLLECT_COURSE_DETAILS;


/**
 * Created by xkai on 2017/4/27.
 * 首页  （高端游学，商务会议、聚会联谊）
 */

public class HuoDongFragment extends BaseFragment {


    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    ReceiverHelper mReceiverHelper;
    HuoDongSubAdapter mAdapter;
    private SwipeRefreshController<NoPageListBean<NewsCategoryListBean>> controller;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_single_listview;
    }

    public static HuoDongFragment newInstance() {
        Bundle args = new Bundle();
        HuoDongFragment fragment = new HuoDongFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initSwipeRefreshLayout();
    }

    private void initSwipeRefreshLayout() {
        mReceiverHelper = new ReceiverHelper(context);
        mReceiverHelper.addAction(COLLECT_COURSE_DETAILS);// 活动、课程详情收藏时发广播,
        mReceiverHelper.registerReceiver();
        mReceiverHelper.setReceiveHelperListener(new ReceiverHelper.ReceiveHelperListener() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (COLLECT_COURSE_DETAILS.equals(intent.getAction())) {
                    if (controller != null) {
                        controller.loadFirstPage();
                    }
                }
            }
        });
        smartSwipeRefreshLayout.initWithLinearLayout();
        smartSwipeRefreshLayout.getRecyclerView().addItemDecoration(new DividerItemDecoration(getContext()));
        Bundle bundle = getArguments();
        String cid = bundle.getString("cid");
        NewsCategoryListApi api = new NewsCategoryListApi();
        api.cid = cid;//
        mAdapter = new HuoDongSubAdapter(getContext());
        smartSwipeRefreshLayout.setAdapter(mAdapter);
        controller = new SwipeRefreshController<NoPageListBean<NewsCategoryListBean>>(getContext(), smartSwipeRefreshLayout, api, mAdapter) {
        };
        controller.loadFirstPage();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mReceiverHelper.onDestroy();
    }

}
