package com.lanmei.kang.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.lanmei.kang.ui.merchant.fragment.MerchantOrderListFragment;

/**
 * 我的订单
 */
public class MerchantOrderAdapter extends FragmentPagerAdapter {


    public MerchantOrderAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        MerchantOrderListFragment fragment = new MerchantOrderListFragment();
        Bundle bundle = new Bundle();
        switch (position) {
            case 0:
                bundle.putString("status","0");//全部
                break;
            case 1:
                bundle.putString("status","1");//待付款
                break;
            case 2:
                bundle.putString("status","2");//已支付
                break;
            case 3:
                bundle.putString("status","4");//已完成
                break;
        }
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "全部";
            case 1:
                return "待付款";
            case 2:
                return "已付款";
            case 3:
                return "已完成";
        }
        return "";
    }

}
