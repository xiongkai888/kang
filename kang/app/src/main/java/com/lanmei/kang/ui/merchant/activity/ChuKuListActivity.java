package com.lanmei.kang.ui.merchant.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.ChuKuListAdapter;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.bean.MerchantListBean;
import com.lanmei.kang.util.CommonUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.StringUtils;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 出库（入库）列表
 */
public class ChuKuListActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    @InjectView(R.id.time_tv)
    TextView timeTv;
    private SwipeRefreshController<NoPageListBean<MerchantListBean>> controller;
    private boolean isChuKu;

    @Override
    public int getContentViewId() {
        return R.layout.activity_chu_ku_list;
    }


    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {

        isChuKu = StringUtils.isSame(getIntent().getStringExtra("value"), CommonUtils.isZero);

        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(isChuKu ? R.string.chu_ku_list : R.string.ru_ku_list);
        actionbar.setHomeAsUpIndicator(R.mipmap.back_g);

        initSwipeRefreshLayout();
    }


    private void initSwipeRefreshLayout() {
        KangQiMeiApi api = new KangQiMeiApi("place/Placelist");
        api.addParams("more", 1);
        ChuKuListAdapter adapter = new ChuKuListAdapter(this);
        smartSwipeRefreshLayout.initWithLinearLayout();
        smartSwipeRefreshLayout.setAdapter(adapter);
        controller = new SwipeRefreshController<NoPageListBean<MerchantListBean>>(this, smartSwipeRefreshLayout, api, adapter) {
        };
        adapter.notifyDataSetChanged();
//        controller.loadFirstPage();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                IntentUtil.startActivity(this, GoodsChuKuActivity.class, isChuKu ? CommonUtils.isZero : CommonUtils.isOne);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @OnClick(R.id.filter_tv)
    public void onViewClicked() {
        CommonUtils.developing(this);
    }
}
