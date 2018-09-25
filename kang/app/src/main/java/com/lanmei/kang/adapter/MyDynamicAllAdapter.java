package com.lanmei.kang.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.lanmei.kang.ui.mine.fragment.MyDynamicFragment;
import com.lanmei.kang.util.CommonUtils;

/**
 * 我的动态
 */
public class MyDynamicAllAdapter extends FragmentPagerAdapter {


    public MyDynamicAllAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment =  MyDynamicFragment.newInstance();
        Bundle bundle = new Bundle();
        switch (position) {
            case 0:
                bundle.putString("status", CommonUtils.isZero);
                break;
            case 1:
                bundle.putString("status","2");
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
        switch (position) {
            case 0:
                return "我的";
            case 1:
                return "朋友圈";
        }
        return null;
    }
}
