package com.lanmei.kang.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.lanmei.kang.R;
import com.lanmei.kang.ui.mine.fragment.CouponFragment;

/**
 *
 */
public class CouponAdapter extends FragmentPagerAdapter {

    private Context context;
    
    public CouponAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        CouponFragment fragment = new CouponFragment();
        bundle.putInt("order",position+1);
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
                return context.getString(R.string.unused);
            case 1:
                return context.getString(R.string.have_been_used);
            case 2:
                return context.getString(R.string.have_expired);
        }

        return null;
    }

}
