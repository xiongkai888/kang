package com.lanmei.kang.ui.merchant.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.ChuKuListAdapter;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.bean.ChuKuListBean;
import com.lanmei.kang.event.ChuKuEvent;
import com.lanmei.kang.util.CommonUtils;
import com.lanmei.kang.util.FormatTime;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.StringUtils;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import org.greenrobot.eventbus.Subscribe;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DateTimePicker;

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
    private SwipeRefreshController<NoPageListBean<ChuKuListBean>> controller;
    private boolean isChuKu;
    private DateTimePicker picker;
    private FormatTime time;
    private  KangQiMeiApi api;

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
        initPicker();
    }

    private void initPicker() {
        picker = new DateTimePicker(this, DateTimePicker.YEAR_MONTH,DateTimePicker.NONE);
        time = new FormatTime(this);
        int year = time.getYear();
        int month = time.getMonth();
        picker.setDateRangeStart(2017, 1);
        picker.setDateRangeEnd(year, month);
        picker.setSelectedItem(year, month,0,0);
        picker.setLabel("-","","","","");
        picker.setOnDateTimePickListener(new DateTimePicker.OnYearMonthTimePickListener() {
            @Override
            public void onDateTimePicked(String year, String month, String hour, String minute) {
                timeTv.setText(String.format(getString(R.string.year_month),year,month));
                int days = CommonUtils.getMonthDays(Integer.valueOf(year), Integer.valueOf(month));
                api.addParams("starttime",year+"-"+month+"-"+1);
                api.addParams("endtime",year+"-"+month+"-"+days);
                controller.loadFirstPage();
            }
        });

    }


    private void initSwipeRefreshLayout() {
        api = new KangQiMeiApi(isChuKu?"app/good_cstoreroom_list":"app/good_rstoreroom_list");
        ChuKuListAdapter adapter = new ChuKuListAdapter(this);
        smartSwipeRefreshLayout.initWithLinearLayout();
        smartSwipeRefreshLayout.setAdapter(adapter);
        controller = new SwipeRefreshController<NoPageListBean<ChuKuListBean>>(this, smartSwipeRefreshLayout, api, adapter) {
        };
        controller.loadFirstPage();
    }

    //添加出库或入库成功后调用
    @Subscribe
    public void chuKuEvent(ChuKuEvent event){
        controller.loadFirstPage();
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
        if (picker != null){
            picker.show();
        }
    }
}
