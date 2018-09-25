package com.lanmei.kang.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.lanmei.kang.loader.DataLoader;
import com.lanmei.kang.ui.mine.fragment.CouponFragment;
import com.xson.common.utils.StringUtils;

/**
 * 课程：团体课程、私人课程、在线课程
 */
public class CouponAdapter extends FragmentPagerAdapter {


    public CouponAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        String cid = DataLoader.getInstance().getCid("团体课程");
        if (StringUtils.isEmpty(cid)){
            cid = "12";
        }
        Fragment fragment = new CouponFragment();
        bundle.putString("cid",cid);
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
                return "未使用";
            case 1:
                return "已使用";
            case 2:
                return "已过期";
        }

        return null;
    }

}
