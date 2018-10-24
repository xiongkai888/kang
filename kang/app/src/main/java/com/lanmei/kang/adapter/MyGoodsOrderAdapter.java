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
                bundle.putInt("state",9);//9全部0待支付2待收货3已完成
                break;
            case 1:
                bundle.putInt("state",0);
                break;
            case 2:
                bundle.putInt("state",2);
                break;
            case 3:
                bundle.putInt("state",3);
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
                return context.getString(R.string.wait_pay);
            case 2:
                return context.getString(R.string.wait_receiving);//
            case 3:
                return context.getString(R.string.doned);
        }
        return "";
    }
}
