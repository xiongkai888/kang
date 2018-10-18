package com.lanmei.kang.ui.merchant.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.GoodsSellListAdapter;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.bean.GoodsSellListBean;
import com.lanmei.kang.util.CommonUtils;
import com.lanmei.kang.util.FormatTime;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.utils.IntentUtil;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DateTimePicker;

/**
 * 销售列表
 */
public class GoodsSellListActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    @InjectView(R.id.time_tv)
    TextView timeTv;
    private SwipeRefreshController<NoPageListBean<GoodsSellListBean>> controller;
    private DateTimePicker picker;
    private FormatTime time;
    private KangQiMeiApi api;

    @Override
    public int getContentViewId() {
        return R.layout.activity_chu_ku_list;
    }


    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {


        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.sell_list);
        actionbar.setHomeAsUpIndicator(R.mipmap.back_g);

        initSwipeRefreshLayout();
        initPicker();
    }


    private void initPicker() {
        picker = new DateTimePicker(this, DateTimePicker.YEAR_MONTH, DateTimePicker.NONE);
        time = new FormatTime(this);
        int year = time.getYear();
        int month = time.getMonth();
        picker.setDateRangeStart(2017, 1);
        picker.setDateRangeEnd(year, month);
        picker.setSelectedItem(year, month, 0, 0);
        picker.setLabel("-", "", "", "", "");
        picker.setOnDateTimePickListener(new DateTimePicker.OnYearMonthTimePickListener() {
            @Override
            public void onDateTimePicked(String year, String month, String hour, String minute) {
                timeTv.setText(String.format(getString(R.string.year_month), year, month));
                int days = CommonUtils.getMonthDays(Integer.valueOf(year), Integer.valueOf(month));
                api.addParams("starttime",year+"-"+month+"-"+1);
                api.addParams("endtime",year+"-"+month+"-"+days);
                controller.loadFirstPage();
            }
        });

    }

    private void initSwipeRefreshLayout() {
        api = new KangQiMeiApi("app/goods_sale_list");
        api.addParams("sellerid",api.getUserId(this));
        GoodsSellListAdapter adapter = new GoodsSellListAdapter(this);
        smartSwipeRefreshLayout.initWithLinearLayout();
        smartSwipeRefreshLayout.setAdapter(adapter);
        controller = new SwipeRefreshController<NoPageListBean<GoodsSellListBean>>(this, smartSwipeRefreshLayout, api, adapter) {
        };
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
                IntentUtil.startActivity(this, GoodsSellActivity.class);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @OnClick(R.id.filter_tv)
    public void onViewClicked() {
        if (picker != null) {
            picker.show();
        }
    }
}
