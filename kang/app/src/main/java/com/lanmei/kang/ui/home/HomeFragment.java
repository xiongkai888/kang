package com.lanmei.kang.ui.home;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.HomeAdapter;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.bean.HomeBean;
import com.lanmei.kang.event.LocationEvent;
import com.lanmei.kang.ui.mine.activity.SearchUserActivity;
import com.lanmei.kang.util.CommonUtils;
import com.lanmei.kang.util.SharedAccount;
import com.xson.common.app.BaseFragment;
import com.xson.common.bean.HomeListBean;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.utils.IntentUtil;
import com.xson.common.widget.DividerItemDecoration;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.InjectView;
import butterknife.OnClick;


/**
 * Created by Administrator on 2017/4/24.
 * 场地
 */

public class HomeFragment extends BaseFragment {


    @InjectView(R.id.city_tv)
    TextView mCityTv;//定位城市

    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    HomeAdapter mAdapter;
    private SwipeRefreshController<HomeListBean<HomeBean>> controller;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_home;
    }

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initListView();
        mCityTv.setText(SharedAccount.getInstance(context).getCity());
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }
    KangQiMeiApi api;

    private void initListView() {

        smartSwipeRefreshLayout.initWithLinearLayout();
        smartSwipeRefreshLayout.getRecyclerView().addItemDecoration(new DividerItemDecoration(context));
        api = new KangQiMeiApi("index/home");
        api.addParams("limit",6);

        mAdapter = new HomeAdapter(context);
        smartSwipeRefreshLayout.setAdapter(mAdapter);
        controller = new SwipeRefreshController<HomeListBean<HomeBean>>(context, smartSwipeRefreshLayout, api, mAdapter) {
            @Override
            public boolean onSuccessResponse(HomeListBean<HomeBean> response) {
                if (mAdapter != null){
                    mAdapter.setData(response);
                }
                return true;
            }
        };
        api.addParams("lon",SharedAccount.getInstance(context).getLon());
        api.addParams("lat",SharedAccount.getInstance(context).getLat());
        controller.loadFirstPage();
    }



    @OnClick({R.id.ll_search, R.id.message_iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_search://搜索
                IntentUtil.startActivity(context, SearchUserActivity.class);
                break;
            case R.id.message_iv://客服
                if (!CommonUtils.isLogin(context)){
                    return;
                }
                IntentUtil.startActivity(context, com.hyphenate.chatuidemo.ui.MainActivity.class);
                break;
        }
    }

    /**
     * MainActivity 定位成功调用
     * @param event
     */
    @Subscribe
    public void locationEvent(LocationEvent event){
        mCityTv.setText(event.getCity());
        api.addParams("lon",event.getLongitude());
        api.addParams("lat",event.getLatitude());
        controller.loadFirstPage();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
