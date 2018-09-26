package com.lanmei.kang.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.lanmei.kang.ui.dynamic.DynamicFragment;
import com.lanmei.kang.ui.home.HomeFragment;
import com.lanmei.kang.ui.merchant_tab.MerchantTabFragment;
import com.lanmei.kang.ui.mine.MineMerchantFragment;
import com.lanmei.kang.ui.mine.MineUserFragment;
import com.lanmei.kang.ui.news.NewsFragment;

/**
 * 主页面
 */
public class MainPagerAdapter extends FragmentPagerAdapter {

    private int count;//tabItem个数

    public MainPagerAdapter(FragmentManager fm,int count) {
        super(fm);
        this.count = count;
    }

    @Override
    public Fragment getItem(int position) {
        if (count == 4){//用户
            switch (position) {
                case 0:
                    return new HomeFragment();
                case 1:
                    return new NewsFragment();
                case 2:
                    return new DynamicFragment();
                case 3:
                    return MineUserFragment.newInstance();
            }
        }else {//商家
            switch (position) {
                case 0:
                    return new MerchantTabFragment();
                case 1:
                    return new HomeFragment();
                case 2:
                    return new NewsFragment();
                case 3:
                    return new DynamicFragment();
                case 4:
                    return MineMerchantFragment.newInstance();
            }
        }
        return null;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "000";
    }
}
