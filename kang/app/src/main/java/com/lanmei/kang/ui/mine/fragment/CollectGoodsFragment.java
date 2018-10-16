package com.lanmei.kang.ui.mine.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.GoodsListAdapter;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.bean.MerchantTabGoodsBean;
import com.lanmei.kang.event.CollectGoodsEvent;
import com.xson.common.app.BaseFragment;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.InjectView;


/**
 * Created by Administrator on 2017/4/27.
 * 收藏（商品）
 */

public class CollectGoodsFragment extends BaseFragment {


    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    private SwipeRefreshController<NoPageListBean<MerchantTabGoodsBean>> controller;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_single_listview_no;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initSwipeRefreshLayout();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }


    private void initSwipeRefreshLayout() {
        KangQiMeiApi api = new KangQiMeiApi("app/collection");
        api.addParams("operation",4);//操作(1添加收藏2修改收藏3删除收藏4收藏列表)
        api.addParams("userid",api.getUserId(context));
        GoodsListAdapter adapter = new GoodsListAdapter(context);
        smartSwipeRefreshLayout.setLayoutManager(new GridLayoutManager(context, 2));
        smartSwipeRefreshLayout.setAdapter(adapter);
        controller = new SwipeRefreshController<NoPageListBean<MerchantTabGoodsBean>>(context, smartSwipeRefreshLayout, api, adapter) {
        };
        controller.loadFirstPage();
    }


    //商品收藏事件
    @Subscribe
    public void collectGoodsEvent(CollectGoodsEvent event) {
        if (controller != null) {
            controller.loadFirstPage();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
