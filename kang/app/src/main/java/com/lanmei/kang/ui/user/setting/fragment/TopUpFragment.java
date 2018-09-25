package com.lanmei.kang.ui.user.setting.fragment;

import android.os.Bundle;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.TopUpAdapter;
import com.lanmei.kang.api.TopUpApi;
import com.lanmei.kang.api.UserInfoApi;
import com.lanmei.kang.bean.RechargeResultBean;
import com.lanmei.kang.bean.UserBean;
import com.lanmei.kang.event.PaySucceedEvent;
import com.lanmei.kang.helper.UserHelper;
import com.lanmei.kang.ui.user.setting.RechargeActivity;
import com.lanmei.kang.util.CommonUtils;
import com.xson.common.app.BaseFragment;
import com.xson.common.bean.DataBean;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
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
    private SwipeRefreshController<NoPageListBean<RechargeResultBean>> controller;


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
        TopUpApi api = new TopUpApi();
        api.type = "2";
        api.token = api.getToken(context);
        api.uid = api.getUserId(context);
        mAdapter = new TopUpAdapter(context);
        smartSwipeRefreshLayout.setAdapter(mAdapter);
        controller = new SwipeRefreshController<NoPageListBean<RechargeResultBean>>(context, smartSwipeRefreshLayout, api, mAdapter) {
        };
        controller.loadFirstPage();

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        ajaxUserInfo();
    }

    @OnClick(R.id.recharge_tv)
    public void showRecharge() {//充值
        IntentUtil.startActivity(context, RechargeActivity.class);
    }

    private void ajaxUserInfo() {
        HttpClient httpClient = HttpClient.newInstance(context);
        UserInfoApi api = new UserInfoApi();
        api.token = UserHelper.getInstance(context).getToken();
        httpClient.request(api, new BeanRequest.SuccessListener<DataBean<UserBean>>() {
            @Override
            public void onResponse(DataBean<UserBean> response) {
               if (mMoneyTv == null){
                   return;
               }
                UserBean data = response.data;
                if (data != null) {
                    UserHelper.getInstance(context).saveBean(data);
                    mMoneyTv.setText("当前余额："+data.getMoney());
                }
            }
        });
    }

    @Subscribe
    public void paySucceedEvent(PaySucceedEvent event){
        ajaxUserInfo();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
