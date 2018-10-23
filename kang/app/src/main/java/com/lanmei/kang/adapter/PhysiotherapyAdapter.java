package com.lanmei.kang.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.lanmei.kang.bean.HomeBean;
import com.lanmei.kang.ui.home.fragment.PhysiotherapyFragment;

/**
 * 理疗界面（中医、西医、中西、高端理疗）
 */
public class PhysiotherapyAdapter extends FragmentPagerAdapter {

    HomeBean.CategoryBean bean;

    public PhysiotherapyAdapter(FragmentManager fm, HomeBean.CategoryBean bean) {
        super(fm);
        this.bean = bean;
    }

    @Override
    public Fragment getItem(int position) {
        PhysiotherapyFragment fragment = new PhysiotherapyFragment();
        Bundle bundle = new Bundle();
        bundle.putString("cid", bean.getId());
        switch (position) {
            case 0:
                break;
            case 1:
            case 2:
                bundle.putString("order", position+"");
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
        switch (position) {
            case 0:
                return "推荐排序";
            case 1:
                return "价格最低";
            case 2:
                return "距离最近";
        }

        return null;
    }

}
