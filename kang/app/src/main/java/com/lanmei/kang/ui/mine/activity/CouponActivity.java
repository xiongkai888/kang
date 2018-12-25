package com.lanmei.kang.ui.mine.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.CouponAdapter;
import com.xson.common.app.BaseActivity;
import com.xson.common.widget.CenterTitleToolbar;

import butterknife.InjectView;


/**
 * 我的优惠卷
 */
public class CouponActivity extends BaseActivity{


    @InjectView(com.lanmei.kang.R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.viewPager)
    ViewPager mViewPager;
    @InjectView(R.id.tabLayout)
    TabLayout mTabLayout;

    @Override
    public int getContentViewId() {
        return R.layout.activity_my_collect;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.my_coupon);
        actionbar.setHomeAsUpIndicator(R.mipmap.back_g);

        mViewPager.setAdapter(new CouponAdapter(getSupportFragmentManager(),this));
        mTabLayout.setupWithViewPager(mViewPager);
    }

}
