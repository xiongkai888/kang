package com.lanmei.kang.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.lanmei.kang.ui.mine.fragment.CollectGoodsFragment;
import com.lanmei.kang.ui.mine.fragment.CollectItemsFragment;
import com.lanmei.kang.ui.mine.fragment.CollectMerchantFragment;
import com.lanmei.kang.ui.mine.fragment.CollectNewsFragment;

/**
 * 我的收藏
 */
public class MyCollectAdapter extends FragmentPagerAdapter {


    public MyCollectAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new CollectMerchantFragment();//商家
            case 1:
                return new CollectItemsFragment();//服务
            case 2:
                return new CollectNewsFragment();//资讯
            case 3:
                return new CollectGoodsFragment();//商品
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "商家";
            case 1:
                return "服务";
            case 2:
                return "资讯";
            case 3:
                return "商品";
        }
        return null;
    }

}
