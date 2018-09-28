package com.lanmei.kang.ui.merchant.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.qrcode.ScanActivity;
import com.lanmei.kang.util.CommonUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.StringUtils;
import com.xson.common.widget.CenterTitleToolbar;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 出库(入库)
 */
public class GoodsChuKuActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.merchant_name_tv)
    TextView merchantNameTv;
    @InjectView(R.id.number_et)
    EditText numberEt;
    @InjectView(R.id.num_tv)
    EditText numTv;
    @InjectView(R.id.price_et)
    EditText priceEt;
    @InjectView(R.id.unit_et)
    EditText unitEt;
    @InjectView(R.id.ll_merchant)
    LinearLayout llMerchant;
    @InjectView(R.id.ll_price)
    LinearLayout llPrice;
    private boolean isChuKu;

    @Override
    public int getContentViewId() {
        return R.layout.activity_goods_chu_ku;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {

        isChuKu = StringUtils.isSame(getIntent().getStringExtra("value"),CommonUtils.isZero);

        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(isChuKu ?R.string.chu_ku:R.string.ru_ku);
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.mipmap.back_g);

        if (!isChuKu){//入库
            llMerchant.setVisibility(View.GONE);
            llPrice.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.ll_merchant, R.id.qr_code_iv,R.id.submit_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_merchant://选择商家
                CommonUtils.developing(this);
                break;
            case R.id.qr_code_iv://条形码扫描
                IntentUtil.startActivity(this, ScanActivity.class,CommonUtils.isZero);
                break;
            case R.id.submit_bt://提交
                CommonUtils.developing(this);
                break;
        }
    }
}
