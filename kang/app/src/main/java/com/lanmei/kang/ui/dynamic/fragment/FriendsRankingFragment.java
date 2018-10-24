package com.lanmei.kang.ui.dynamic.fragment;

import android.os.Bundle;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.FriendsRankAdapter;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.bean.FriendsRankBean;
import com.xson.common.api.AbstractApi;
import com.xson.common.app.BaseFragment;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.widget.DividerItemDecoration;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import butterknife.InjectView;

/**
 * Created by xkai on 2018/1/15.
 * 好友的排名列表
 */

public class FriendsRankingFragment extends BaseFragment {

    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    FriendsRankAdapter mAdapter;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_single_listview;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initSwipeRefreshLayout();
    }

    String uid;

    private void initSwipeRefreshLayout() {
        smartSwipeRefreshLayout.initWithLinearLayout();
        smartSwipeRefreshLayout.getRecyclerView().addItemDecoration(new DividerItemDecoration(context));

        Bundle bundle = getArguments();
        if (bundle != null){
            uid = bundle.getString("uid");
        }
        KangQiMeiApi api = new KangQiMeiApi("friend/rank");
        api.add("uid",uid);
        api.setMethod(AbstractApi.Method.GET);
        mAdapter = new FriendsRankAdapter(context);
        smartSwipeRefreshLayout.setAdapter(mAdapter);
        SwipeRefreshController<NoPageListBean<FriendsRankBean>> controller = new SwipeRefreshController<NoPageListBean<FriendsRankBean>>(context, smartSwipeRefreshLayout, api, mAdapter) {
        };
        controller.loadFirstPage();
    }


}
