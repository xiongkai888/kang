package com.lanmei.kang.ui.merchant.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.MerchantItemsListAdapter;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.bean.MerchantInfoBean;
import com.lanmei.kang.bean.MerchantItemsListBean;
import com.lanmei.kang.event.CompileProductEvent;
import com.lanmei.kang.util.CommonUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.DataBean;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.utils.IntentUtil;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.DividerItemDecoration;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.InjectView;

/**
 * 我的产品(服务项目)
 */
public class MerchantItemsActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;

    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    MerchantItemsListAdapter mAdapter;
    private SwipeRefreshController<NoPageListBean<MerchantItemsListBean>> controller;

    @Override
    public int getContentViewId() {
        return R.layout.activity_merchant_items;
    }

    String pid;

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle("服务项目");
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.mipmap.back_g);


        smartSwipeRefreshLayout.initWithLinearLayout();
        smartSwipeRefreshLayout.getRecyclerView().addItemDecoration(new DividerItemDecoration(this));

        final KangQiMeiApi api = new KangQiMeiApi("member/product");
        mAdapter = new MerchantItemsListAdapter(this);
        smartSwipeRefreshLayout.setAdapter(mAdapter);
        controller = new SwipeRefreshController<NoPageListBean<MerchantItemsListBean>>(this, smartSwipeRefreshLayout, api, mAdapter) {
        };
        KangQiMeiApi infoApi = new KangQiMeiApi("place/index");
        infoApi.addParams("uid",api.getUserId(this));
        HttpClient.newInstance(this).loadingRequest(infoApi, new BeanRequest.SuccessListener<DataBean<MerchantInfoBean>>() {
            @Override
            public void onResponse(DataBean<MerchantInfoBean> response) {
                if (isFinishing()){
                    return;
                }
                MerchantInfoBean bean = response.data;
                if (bean == null){
                    return;
                }
                pid = bean.getId();
                api.addParams("pid",pid);
                mAdapter.setPid(pid);
                controller.loadFirstPage();

            }
        });

        EventBus.getDefault().register(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                if (!CommonUtils.isLogin(this)) {
                    break;
                }
                Bundle bundle = new Bundle();
                bundle.putString("pid",pid);
                IntentUtil.startActivity(this,ItemCompileActivity.class,bundle);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void compileProductEvent(CompileProductEvent event){
        if (controller != null){
            controller.loadFirstPage();
        }
    }


    //    @OnClick({R.id.ll_add_new_item, R.id.ll_batch_manage})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.ll_add_new_item://添加新产品
//                IntentUtil.startActivity(this, 1 ,ItemCompileActivity.class);
//                break;
//            case R.id.ll_batch_manage://批量管理
//                UIHelper.ToastMessage(this,R.string.developing);
//                break;
//        }
//    }
}
