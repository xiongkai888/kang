package com.lanmei.kang.ui.mine.fragment;

import android.os.Bundle;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.CouponTabAdapter;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.helper.coupon.BeanCoupon;
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

    @Override
    public int getContentViewId() {
        return R.layout.fragment_single_listview_no;
    }


    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initSwipeRefreshLayout();
    }
    private void initSwipeRefreshLayout() {
        smartSwipeRefreshLayout.initWithLinearLayout();
        Bundle bundle = getArguments();
        KangQiMeiApi api = new KangQiMeiApi("app/coupon");
        api.add("order",bundle.getInt("order"));
        api.add("uid",api.getUserId(context));
//        api.setMethod(AbstractApi.Method.GET);
        CouponTabAdapter adapter = new CouponTabAdapter(context);
        smartSwipeRefreshLayout.setAdapter(adapter);
        SwipeRefreshController<NoPageListBean<BeanCoupon>> controller = new SwipeRefreshController<NoPageListBean<BeanCoupon>>(context, smartSwipeRefreshLayout, api, adapter) {
        };
        smartSwipeRefreshLayout.setMode(SmartSwipeRefreshLayout.Mode.NO_PAGE);
        controller.loadFirstPage();
//        adapter.notifyDataSetChanged();
    }
}
