package com.lanmei.kang.ui.dynamic;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.DynamicAdapter;
import com.lanmei.kang.ui.dynamic.activity.PublishDynamicActivity;
import com.xson.common.app.BaseFragment;
import com.xson.common.utils.IntentUtil;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/4/25.
 * 动态(即帖子)
 */

public class DynamicFragment extends BaseFragment {


    @InjectView(R.id.tabLayout)
    TabLayout mTabLayout;
    @InjectView(R.id.viewPager)
    ViewPager mViewPager;



    @Override
    public int getContentViewId() {
        return R.layout.fragment_dynamic;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initSwipeRefreshLayout();
    }


    private void initSwipeRefreshLayout() {
        DynamicAdapter adapter = new DynamicAdapter(getChildFragmentManager());
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }


    @OnClick(R.id.publish_iv)
    public void onViewClicked() {
        IntentUtil.startActivity(context, PublishDynamicActivity.class);
    }
}
