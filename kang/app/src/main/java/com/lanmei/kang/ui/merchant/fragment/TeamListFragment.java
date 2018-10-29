package com.lanmei.kang.ui.merchant.fragment;

import android.os.Bundle;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.TeamListAdapter;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.bean.TeamBean;
import com.xson.common.app.BaseFragment;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import butterknife.InjectView;


/**
 * Created by xkai on 2017/4/27.
 * 我的团队
 */

public class TeamListFragment extends BaseFragment {

    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;

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
        Bundle bundle = getArguments();
        KangQiMeiApi api = new KangQiMeiApi("app/my_team");
        api.add("agent",bundle.getInt("agent")).add("uid",api.getUserId(context));
        TeamListAdapter adapter = new TeamListAdapter(context);
        smartSwipeRefreshLayout.setAdapter(adapter);
        SwipeRefreshController<NoPageListBean<TeamBean>> controller = new SwipeRefreshController<NoPageListBean<TeamBean>>(getContext(), smartSwipeRefreshLayout, api, adapter) {
        };
        controller.loadFirstPage();
    }
}
