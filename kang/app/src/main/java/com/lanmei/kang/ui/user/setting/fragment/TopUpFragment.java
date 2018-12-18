package com.lanmei.kang.ui.user.setting.fragment;

import android.os.Bundle;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.TopUpAdapter;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.bean.RechargeResultBean;
import com.lanmei.kang.event.PaySucceedEvent;
import com.lanmei.kang.event.SetUserInfoEvent;
import com.lanmei.kang.ui.user.setting.RechargeActivity;
import com.lanmei.kang.util.CommonUtils;
import com.xson.common.api.AbstractApi;
import com.xson.common.app.BaseFragment;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.bean.UserBean;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.utils.IntentUtil;
import com.xson.common.widget.DividerItemDecoration;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.InjectView;
import butterknife.OnClick;


/**
 * Created by Administrator on 2017/4/27.
 * 充值记录
 */

public class TopUpFragment extends BaseFragment {

    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    @InjectView(R.id.money_tv)
    TextView mMoneyTv;//当前余额
    TopUpAdapter mAdapter;


    @Override
    public int getContentViewId() {
        return R.layout.fragment_top_up;
    }

    public static TopUpFragment newInstance() {
        Bundle args = new Bundle();
        TopUpFragment fragment = new TopUpFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {

        UserBean bean = CommonUtils.getUserBean(context);
        if (bean != null) {
//            mMoneyTv.setText("当前余额："+bean.getMoney());
        }
        smartSwipeRefreshLayout.initWithLinearLayout();
        smartSwipeRefreshLayout.getRecyclerView().addItemDecoration(new DividerItemDecoration(context));
        KangQiMeiApi api = new KangQiMeiApi("member/money_log");
        api.add("type",2);
        api.add("token",api.getToken(context));
        api.add("uid",api.getUserId(context));
        api.setMethod(AbstractApi.Method.GET);
        mAdapter = new TopUpAdapter(context);
        smartSwipeRefreshLayout.setAdapter(mAdapter);
        SwipeRefreshController<NoPageListBean<RechargeResultBean>> controller = new SwipeRefreshController<NoPageListBean<RechargeResultBean>>(context, smartSwipeRefreshLayout, api, mAdapter) {
        };
        controller.loadFirstPage();

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @OnClick(R.id.recharge_tv)
    public void showRecharge() {//充值
        IntentUtil.startActivity(context, RechargeActivity.class);
    }


    @Subscribe
    public void paySucceedEvent(PaySucceedEvent event){
       CommonUtils.loadUserInfo(context,null);
    }

    @Subscribe
    public void onEventMainThread(SetUserInfoEvent event) {
        UserBean bean = event.getBean();
        if (bean != null){
            if (bean != null) {
                mMoneyTv.setText("当前余额："+bean.getMoney());
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
