package com.lanmei.kang.ui.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.PhysiotherapyAdapter;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.HomeListBean;
import com.xson.common.widget.CenterTitleToolbar;

import butterknife.InjectView;

/**
 * 理疗界面（中医、西医、中西、高端理疗）
 */
public class PhysiotherapyActivity extends BaseActivity{

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.viewPager)
    ViewPager mViewPager;
    @InjectView(R.id.tabLayout)
    TabLayout mTabLayout;
    PhysiotherapyAdapter mAdapter;
    HomeListBean.CategoryBean bean;

    @Override
    public int getContentViewId() {
        return R.layout.activity_my_collect;
    }

    @Override
    public void initIntent(Intent intent) {
        super.initIntent(intent);
        Bundle bundle = intent.getBundleExtra("bundle");
        bean = (HomeListBean.CategoryBean) bundle.getSerializable("bean");
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {

        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(bean.getName());
        actionbar.setHomeAsUpIndicator(R.mipmap.back);

        mAdapter = new PhysiotherapyAdapter(getSupportFragmentManager(),bean);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(2);
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
