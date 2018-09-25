package com.lanmei.kang.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.lanmei.kang.ui.user.setting.fragment.ConsumeDetailsFragment;
import com.lanmei.kang.ui.user.setting.fragment.TopUpFragment;
import com.lanmei.kang.ui.user.setting.fragment.WithdrawFragment;

/**
 * 会员卡（消费记录等信息）
 */
public class ClubAdapter extends FragmentPagerAdapter {

    public ClubAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ConsumeDetailsFragment.newInstance();
            case 1:
                return TopUpFragment.newInstance();
            case 2:
                return WithdrawFragment.newInstance();

        }
        return null;
    }

    @Override
    public int getCount() {

        return 3;
    }


}
