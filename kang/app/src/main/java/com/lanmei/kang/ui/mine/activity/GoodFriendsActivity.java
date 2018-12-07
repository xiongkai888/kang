package com.lanmei.kang.ui.mine.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.GoodFriendsAdapter;
import com.lanmei.kang.util.CommonUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.utils.IntentUtil;

import butterknife.InjectView;
import butterknife.OnClick;


/**
 * 我的好友
 */
public class GoodFriendsActivity extends BaseActivity {


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
        CommonUtils.setCompoundDrawables(this,menuTv,R.mipmap.dr_haoyou2,0,2);
        initViewPager();
    }

    public void initViewPager() {
        mViewPager.setAdapter(new GoodFriendsAdapter(getSupportFragmentManager()));
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @OnClick({R.id.back_iv, R.id.menu_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                onBackPressed();
                break;
            case R.id.menu_tv:
                IntentUtil.startActivity(this, SearchUserActivity.class, "user");
                break;
        }
    }

}
