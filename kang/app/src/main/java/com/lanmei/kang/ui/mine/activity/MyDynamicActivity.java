package com.lanmei.kang.ui.mine.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.MyDynamicAllAdapter;
import com.lanmei.kang.ui.dynamic.activity.PublishDynamicActivity;
import com.xson.common.app.BaseActivity;
import com.xson.common.utils.IntentUtil;

import butterknife.InjectView;

/**
 * 我的动态
 */
public class MyDynamicActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.tabLayout)
    TabLayout mTabLayout;
    @InjectView(R.id.viewPager)
    ViewPager mViewPager;
    MyDynamicAllAdapter mClubAdapter;


    @Override
    public int getContentViewId() {
        return R.layout.activity_good_friends;
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
        mClubAdapter = new MyDynamicAllAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mClubAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dynamic, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_publish:
                IntentUtil.startActivity(getContext(), PublishDynamicActivity.class);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}
