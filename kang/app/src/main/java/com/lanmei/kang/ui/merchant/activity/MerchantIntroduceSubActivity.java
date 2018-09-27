package com.lanmei.kang.ui.merchant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.bean.MerchantDetailsBean;
import com.xson.common.app.BaseActivity;
import com.xson.common.widget.CenterTitleToolbar;

import butterknife.InjectView;

/**
 * 商家介绍（文字介绍）
 */
public class MerchantIntroduceSubActivity extends BaseActivity {


    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.content_tv)
    TextView contentTv;
    MerchantDetailsBean bean;//商家详情信息

    @Override
    public int getContentViewId() {
        return R.layout.activity_merchant_introduce_sub;
    }

    @Override
    public void initIntent(Intent intent) {
        super.initIntent(intent);
        Bundle bundle = intent.getBundleExtra("bundle");
        bean = (MerchantDetailsBean)bundle.getSerializable("bean");
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(bean.getName());
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.mipmap.back_g);

        contentTv.setText("\u3000\u3000"+bean.getPlace_introduction());
    }
}
