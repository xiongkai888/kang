package com.lanmei.kang.ui.mine.fragment;

import android.os.Bundle;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.CouponTabAdapter;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.bean.NewsCategoryListBean;
import com.xson.common.app.BaseFragment;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import butterknife.InjectView;



/**
 * Created by xkai on 2017/4/27.
 * 优惠券
 */

public class CouponFragment extends BaseFragment {

    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    CouponTabAdapter mAdapter;

    @Override
    public int getContentViewId() {
        return R.layout.listview_no_divider;
    }


    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initSwipeRefreshLayout();
    }
    private void initSwipeRefreshLayout() {
        smartSwipeRefreshLayout.initWithLinearLayout();
        Bundle bundle = getArguments();
        String cid = bundle.getString("cid");
        KangQiMeiApi api = new KangQiMeiApi("post/index");
        api.add("cid",cid);
        mAdapter = new CouponTabAdapter(context);
        smartSwipeRefreshLayout.setAdapter(mAdapter);
        SwipeRefreshController<NoPageListBean<NewsCategoryListBean>> controller = new SwipeRefreshController<NoPageListBean<NewsCategoryListBean>>(getContext(), smartSwipeRefreshLayout, api, mAdapter) {
        };
        mAdapter.notifyDataSetChanged();
    }
}
