package com.lanmei.kang.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.lanmei.kang.ui.dynamic.DynamicFragment;
import com.lanmei.kang.ui.home.HomeFragment;
import com.lanmei.kang.ui.mine.MineFragment;
import com.lanmei.kang.ui.news.NewsFragment;

/**
 * 主页面
 */
public class MainPagerAdapter extends FragmentPagerAdapter {

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return HomeFragment.newInstance();
            case 1:
                return NewsFragment.newInstance();
            case 2:
                return DynamicFragment.newInstance();
            case 3:
                return MineFragment.newInstance();

        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }

}
