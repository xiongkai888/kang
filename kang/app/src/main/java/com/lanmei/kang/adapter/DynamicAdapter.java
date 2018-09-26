package com.lanmei.kang.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.lanmei.kang.ui.dynamic.fragment.DynamicListFragment;
import com.lanmei.kang.util.CommonUtils;

/**
 * （动态）
 */
public class DynamicAdapter extends FragmentPagerAdapter {


    public DynamicAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        DynamicListFragment fragment = new DynamicListFragment();
        Bundle bundle = new Bundle();
        switch (position) {
            case 0:
                bundle.putString("type", "3");//3、最新
                break;
            case 1:
                bundle.putString("type", CommonUtils.isOne);//1、精选列表
                break;

        }
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return position == 0 ? "最新" : "精选";
    }
}
