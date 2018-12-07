package com.lanmei.kang.ui.mine.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.MyDynamicAllAdapter;
import com.lanmei.kang.ui.dynamic.activity.PublishDynamicActivity;
import com.lanmei.kang.util.CommonUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.utils.IntentUtil;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 我的动态
 */
public class MyDynamicActivity extends BaseActivity {

    @InjectView(R.id.tabLayout)
    TabLayout mTabLayout;
    @InjectView(R.id.viewPager)
    ViewPager mViewPager;
    @InjectView(R.id.menu_tv)
    TextView menuTv;


    @Override
    public int getContentViewId() {
        return R.layout.activity_good_friends;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        CommonUtils.setCompoundDrawables(this,menuTv,R.mipmap.details_write,0,2);
        mViewPager.setAdapter(new MyDynamicAllAdapter(getSupportFragmentManager()));
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @OnClick({R.id.back_iv, R.id.menu_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                onBackPressed();
                break;
            case R.id.menu_tv:
                IntentUtil.startActivity(getContext(), PublishDynamicActivity.class);
                break;
        }
    }

}
