package com.lanmei.kang.ui.home.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.NotificationAdapter;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.bean.NotificationBean;
import com.lanmei.kang.util.AKDialog;
import com.lanmei.kang.view.SwipeItemLayout;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import butterknife.InjectView;


/**
 * 私信
 */
public class NotificationActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;

    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    private NotificationAdapter adapter;
    private SwipeRefreshController<NoPageListBean<NotificationBean>> controller;
    @Override
    public int getContentViewId() {
        return R.layout.activity_single_listview_no;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
//        EventBus.getDefault().register(this);
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.private_letter);
        actionbar.setHomeAsUpIndicator(R.mipmap.back_g);

        smartSwipeRefreshLayout.initWithLinearLayout();
        KangQiMeiApi api = new KangQiMeiApi("station/sixin");
        api.add("uid",api.getUserId(this));
        adapter = new NotificationAdapter(this);
        smartSwipeRefreshLayout.setAdapter(adapter);
        smartSwipeRefreshLayout.addOnItemTouchListener(new SwipeItemLayout.OnSwipeItemTouchListener(this));
        controller = new SwipeRefreshController<NoPageListBean<NotificationBean>>(this, smartSwipeRefreshLayout, api, adapter) {
        };
        adapter.setDeleteSiXinListener(new NotificationAdapter.DeleteSiXinListener() {
            @Override
            public void delete(final String id,final int position) {
                AKDialog.getConfirmDialog(getContext(), "确定删除该私信？", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteSiXin(id,position);
                    }
                }).show();

            }
        });
        controller.loadFirstPage();
    }
    private void deleteSiXin(String id,final int position){
        KangQiMeiApi api = new KangQiMeiApi("station/del_sixin");
        api.add("uid",api.getUserId(this));
        api.add("sid",id);
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()){
                    return;
                }
                adapter.getData().remove(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyDataSetChanged();
            }
        });
    }
    //会员升级同意或者拒绝调用
//    @Subscribe
//    public void UpgradeEvent(UpgradeEvent event){
//        controller.loadFirstPage();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        EventBus.getDefault().unregister(this);
//    }
}
