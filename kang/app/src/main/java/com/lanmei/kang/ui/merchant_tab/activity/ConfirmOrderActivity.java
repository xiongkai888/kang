package com.lanmei.kang.ui.merchant_tab.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.PayWayAdapter;
import com.lanmei.kang.bean.PayWayBean;
import com.lanmei.kang.ui.merchant_tab.goods.activity.AddressListActivity;
import com.lanmei.kang.util.CommonUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.utils.IntentUtil;
import com.xson.common.widget.CenterTitleToolbar;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 确认订单
 */
public class ConfirmOrderActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    public int getContentViewId() {
        return R.layout.activity_confirm_order;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.confirm_order);
        actionbar.setHomeAsUpIndicator(R.mipmap.back_g);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        PayWayAdapter adapter = new PayWayAdapter(this);
        adapter.setData(getList());
        recyclerView.setAdapter(adapter);
    }

    private List<PayWayBean> getList(){
        List<PayWayBean> payMentBeans = new ArrayList<>();
        PayWayBean bean1 = new PayWayBean();
        bean1.setC_name("支付宝");
        PayWayBean bean2 = new PayWayBean();
        bean2.setC_name("微信");
        payMentBeans.add(bean1);
        payMentBeans.add(bean2);
        return payMentBeans;
    }

    @OnClick({R.id.ll_address, R.id.submit_order_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_address:
                IntentUtil.startActivity(this, AddressListActivity.class);
                break;
            case R.id.submit_order_tv:
                CommonUtils.developing(this);
                break;
        }
    }

}
