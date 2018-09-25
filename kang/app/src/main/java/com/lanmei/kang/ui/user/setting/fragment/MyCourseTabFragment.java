package com.lanmei.kang.ui.user.setting.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.MyCourseListAdapter;
import com.lanmei.kang.api.MyCourseApi;
import com.lanmei.kang.bean.MyCourseBean;
import com.lanmei.kang.helper.UserHelper;
import com.xson.common.app.BaseFragment;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.widget.DividerItemDecoration;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import butterknife.InjectView;

import static com.lanmei.kang.ui.details.CourseDetailsActivity.COLLECT_COURSE_DETAILS;


/**
 * Created by Administrator on 2017/4/27.
 * 我的课程（全部、已报名、已参数）
 */

public class MyCourseTabFragment extends BaseFragment {


    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;

    MyCourseListAdapter mAdapter;
    private SwipeRefreshController<NoPageListBean<MyCourseBean>> controller;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_single_listview;
    }

    public static MyCourseTabFragment newInstance() {
        Bundle args = new Bundle();
        MyCourseTabFragment fragment = new MyCourseTabFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initSwipeRefreshLayout();
    }

    private void initSwipeRefreshLayout() {


        IntentFilter filter = new IntentFilter();
        filter.addAction(COLLECT_COURSE_DETAILS);//课程、活动收藏
        context.registerReceiver(mReceiver, filter);

        smartSwipeRefreshLayout.initWithLinearLayout();
        smartSwipeRefreshLayout.getRecyclerView().addItemDecoration(new DividerItemDecoration(context));
        MyCourseApi api = new MyCourseApi();
        Bundle bundle = getArguments();
        if (bundle != null) {
            api.status = bundle.getString("status");
        }
        api.token = UserHelper.getInstance(context).getToken();
        mAdapter = new MyCourseListAdapter(context);
        smartSwipeRefreshLayout.setAdapter(mAdapter);
        controller = new SwipeRefreshController<NoPageListBean<MyCourseBean>>(context, smartSwipeRefreshLayout, api, mAdapter) {
        };
        controller.loadFirstPage();
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (COLLECT_COURSE_DETAILS.equals(intent.getAction())) {
                if (controller != null) {
                    controller.loadFirstPage();
                }
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        context.unregisterReceiver(mReceiver);
    }
}
