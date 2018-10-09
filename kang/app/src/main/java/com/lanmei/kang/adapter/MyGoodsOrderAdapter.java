package com.lanmei.kang.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.lanmei.kang.R;
import com.lanmei.kang.ui.mine.fragment.GoodsOrderListFragment;

/**
 * 我的商品订单
 */
public class MyGoodsOrderAdapter extends FragmentPagerAdapter {

    private Context context;

    public MyGoodsOrderAdapter(FragmentManager fm,Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        GoodsOrderListFragment fragment = new GoodsOrderListFragment();
        Bundle bundle = new Bundle();
        switch (position) {
            case 0:
                bundle.putString("status","0");//0全部1待付款2已付款3未消费4已完成
                break;
            case 1:
                bundle.putString("status","1");
                break;
            case 2:
                bundle.putString("status","3");
                break;
            case 3:
                bundle.putString("status","4");
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
        switch (position) {
            case 0:
                return context.getString(R.string.all);
            case 1:
                return context.getString(R.string.wait_receiving);
            case 2:
                return context.getString(R.string.wait_pay);
            case 3:
                return context.getString(R.string.doned);
        }
        return "";
    }
}
