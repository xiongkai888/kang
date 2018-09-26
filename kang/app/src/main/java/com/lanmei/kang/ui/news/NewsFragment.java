package com.lanmei.kang.ui.news;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.NewsTabAdapter;
import com.xson.common.app.BaseFragment;
import com.xson.common.utils.UIHelper;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/4/25.
 * 资讯
 */

public class NewsFragment extends BaseFragment {

    @InjectView(R.id.viewPager)
    ViewPager mViewPager;
    @InjectView(R.id.tabLayout)
    TabLayout mTabLayout;

    NewsTabAdapter mTabAdapter;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_news;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        mTabAdapter = new NewsTabAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mTabAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }


    @OnClick(R.id.ll_search)
    public void onViewClicked() {//搜索
        UIHelper.ToastMessage(context, R.string.developing);
    }
}
