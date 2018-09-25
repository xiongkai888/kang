package com.lanmei.kang.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.lanmei.kang.ui.user.setting.fragment.MyCourseTabFragment;

/**
 * 我的课程
 */
public class MyCourseAdapter extends FragmentPagerAdapter {


    public MyCourseAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        MyCourseTabFragment fragment = MyCourseTabFragment.newInstance();
        Bundle bundle = new Bundle();
        switch (position) {
            case 0:
//                bundle.putString("status","");//全部
                break;
            case 1:
                bundle.putString("status","1");//已报名
                break;
            case 2:
                bundle.putString("status","2");//已参加
                break;

        }
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "全部";
            case 1:
                return "已报名";
            case 2:
                return "已参加";
        }

        return null;
    }

}
