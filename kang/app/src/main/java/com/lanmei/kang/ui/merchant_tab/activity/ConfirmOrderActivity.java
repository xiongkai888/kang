package com.lanmei.kang.ui.merchant_tab.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.PayWayAdapter;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.bean.DistributionBean;
import com.lanmei.kang.bean.GoodsDetailsBean;
import com.lanmei.kang.bean.PayWayBean;
import com.lanmei.kang.ui.merchant_tab.goods.activity.AddressListActivity;
import com.lanmei.kang.util.CommonUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.helper.ImageHelper;
import com.xson.common.utils.DoubleUtil;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.StringUtils;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.FormatTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.OptionPicker;

/**
 * 确认订单
 */
public class ConfirmOrderActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
    @InjectView(R.id.name_tv)
    TextView nameTv;
    @InjectView(R.id.phone_tv)
    TextView phoneTv;
    @InjectView(R.id.address_tv)
    TextView addressTv;
    @InjectView(R.id.items_icon_iv)
    ImageView itemsIconIv;
    @InjectView(R.id.title_tv)
    TextView titleTv;
    @InjectView(R.id.price_num_tv)
    TextView priceNumTv;
    @InjectView(R.id.distribution_tv)
    TextView distributionTv;
    @InjectView(R.id.price_tv)
    FormatTextView priceTv;
    private GoodsDetailsBean detailsBean;
    private int number;
    private List<DistributionBean> distributionBeanList;
    private OptionPicker picker;//

    @Override
    public int getContentViewId() {
        return R.layout.activity_confirm_order;
    }

    @Override
    public void initIntent(Intent intent) {
        super.initIntent(intent);
        Bundle bundle = intent.getBundleExtra("bundle");
        if (bundle != null) {
            detailsBean = (GoodsDetailsBean) bundle.getSerializable("bean");
            number = bundle.getInt("num");
        }
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

        if (detailsBean == null) {
            return;
        }
        ImageHelper.load(this, detailsBean.getCover(), itemsIconIv, null, true, R.mipmap.default_pic, R.mipmap.default_pic);
        titleTv.setText(detailsBean.getGoodsname());
        priceNumTv.setText(String.format(getString(R.string.goods_price_and_num), detailsBean.getPrice(), String.valueOf(number)));
        priceTv.setTextValue(DoubleUtil.mulToString(Double.valueOf(detailsBean.getPrice()), Double.valueOf(number)));

        loadDistribution();//配送方式

    }

    private void initPicker() {
        picker = new OptionPicker(this, toStringList());
        picker.setOffset(2);
        picker.setSelectedIndex(1);
        picker.setTextSize(16);
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                distributionTv.setText(item);
            }
        });
    }

    private List<String> toStringList() {
        List<String> list = new ArrayList<>();
        for (DistributionBean bean : distributionBeanList) {
            list.add(bean.getClassname());
        }
        return list;
    }

    private void loadDistribution() {
        KangQiMeiApi api = new KangQiMeiApi("app/good_distribution_list");
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<NoPageListBean<DistributionBean>>() {
            @Override
            public void onResponse(NoPageListBean<DistributionBean> response) {
                if (isFinishing()) {
                    return;
                }
                distributionBeanList = response.data;
                if (StringUtils.isEmpty(distributionBeanList)) {
                    return;
                }
                initPicker();
            }
        });
    }

    private List<PayWayBean> getList() {
        List<PayWayBean> wayBeans = new ArrayList<>();
        PayWayBean bean1 = new PayWayBean();
        bean1.setC_name("支付宝");
        PayWayBean bean2 = new PayWayBean();
        bean2.setC_name("微信");
        wayBeans.add(bean1);
        wayBeans.add(bean2);
        return wayBeans;
    }

    @OnClick({R.id.ll_address, R.id.submit_order_tv, R.id.ll_distribution, R.id.ll_billing_details})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_address:
                IntentUtil.startActivity(this, AddressListActivity.class);
                break;
            case R.id.submit_order_tv://提交订单
                CommonUtils.developing(this);
                break;
            case R.id.ll_distribution://配送方式
                if (picker != null) {
                    picker.show();
                }
                break;
            case R.id.ll_billing_details://结算详情
                CommonUtils.developing(this);
                break;
        }
    }

}
