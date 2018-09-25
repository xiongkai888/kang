package com.lanmei.kang.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.lanmei.kang.ui.user.setting.fragment.ConsumeDetailsFragment;
import com.lanmei.kang.ui.user.setting.fragment.WithdrawFragment;

/**
 * 商家账户
 */
public class MerchantAccountAdapter extends FragmentPagerAdapter {

    public MerchantAccountAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ConsumeDetailsFragment.newInstance();
            case 1:
                return WithdrawFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }


}
