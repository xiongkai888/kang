package com.lanmei.kang.ui.merchant_tab;

import android.os.Bundle;
import android.view.View;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.MerchantTabAdapter;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.bean.MerchantListBean;
import com.lanmei.kang.util.CommonUtils;
import com.xson.common.app.BaseFragment;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import butterknife.InjectView;
import butterknife.OnClick;


/**
 * Created by Administrator on 2017/4/24.
 * 商家 - 的商家界面
 */

public class MerchantTabFragment extends BaseFragment {

    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;

    MerchantTabAdapter mAdapter;
    private SwipeRefreshController<NoPageListBean<MerchantListBean>> controller;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_merchant_tab;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        smartSwipeRefreshLayout.initGridLinearLayout(2,0);
        KangQiMeiApi api = new KangQiMeiApi("place/Placelist");
        mAdapter = new MerchantTabAdapter(context);
        smartSwipeRefreshLayout.setAdapter(mAdapter);
        controller = new SwipeRefreshController<NoPageListBean<MerchantListBean>>(context, smartSwipeRefreshLayout, api, mAdapter) {
        };
//        controller.loadFirstPage();
        mAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.classify_iv, R.id.search_tv, R.id.message_iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.classify_iv://分类
                CommonUtils.developing(context);
                break;
            case R.id.search_tv://搜索
                CommonUtils.developing(context);
                break;
            case R.id.message_iv://消息
                CommonUtils.developing(context);
                break;
        }
    }
}
