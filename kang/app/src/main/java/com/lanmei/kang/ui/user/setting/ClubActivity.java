package com.lanmei.kang.ui.user.setting;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.ClubAdapter;
import com.xson.common.app.BaseActivity;

import butterknife.InjectView;

/**
 * 会员卡（消费明细、充值记录）
 */
public class ClubActivity extends BaseActivity implements TabLayout.OnTabSelectedListener{

    public static boolean no_bound_card = true;//只弹框一次（是否绑定银行卡）

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.tabLayout)
    TabLayout mTabLayout;
    @InjectView(R.id.viewPager)
    ViewPager mViewPager;
    ClubAdapter mClubAdapter;

    @Override
    public int getContentViewId() {
        return R.layout.activity_club;
    }


    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle("");
        actionbar.setHomeAsUpIndicator(R.mipmap.back_g);

        initViewPager();
    }

    public void initViewPager() {

        mClubAdapter = new ClubAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mClubAdapter);
        mViewPager.setOffscreenPageLimit(0);
        mTabLayout.addOnTabSelectedListener(this);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int position = tab.getPosition();
        mViewPager.setCurrentItem(position);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ClubActivity.no_bound_card = true;
    }
}
