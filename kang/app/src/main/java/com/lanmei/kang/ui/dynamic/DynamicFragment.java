package com.lanmei.kang.ui.dynamic;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.DynamicAdapter;
import com.lanmei.kang.ui.dynamic.activity.PublishDynamicActivity;
import com.lanmei.kang.util.CommonUtils;
import com.xson.common.app.BaseFragment;
import com.xson.common.utils.IntentUtil;

import butterknife.InjectView;

/**
 * Created by Administrator on 2017/4/25.
 * 动态(即帖子)
 */

public class DynamicFragment extends BaseFragment implements Toolbar.OnMenuItemClickListener,TabLayout.OnTabSelectedListener{



    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.tabLayout)
    TabLayout mTabLayout;
    @InjectView(R.id.viewPager)
    ViewPager mViewPager;
    DynamicAdapter mAdapter;


    public static DynamicFragment newInstance() {
        Bundle args = new Bundle();
        DynamicFragment fragment = new DynamicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_dynamic;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        mToolbar.setTitle("");

        initSwipeRefreshLayout();
    }


    private void initSwipeRefreshLayout() {

        mToolbar.getMenu().clear();
        mToolbar.inflateMenu(R.menu.menu_dynamic);
        mToolbar.setOnMenuItemClickListener(this);

        mAdapter = new DynamicAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mTabLayout.addOnTabSelectedListener(this);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

    }



    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_publish:
                if (!CommonUtils.isLogin(context)){
                    break;
                }
                IntentUtil.startActivity(context, PublishDynamicActivity.class);
                break;
        }
        return true;
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
}
