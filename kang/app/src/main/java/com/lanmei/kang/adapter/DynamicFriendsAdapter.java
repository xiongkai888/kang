package com.lanmei.kang.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.lanmei.kang.ui.dynamic.fragment.FriendsDynamicFragment;
import com.lanmei.kang.ui.dynamic.fragment.FriendsFriendsFragment;
import com.lanmei.kang.ui.dynamic.fragment.FriendsRankingFragment;


/**
 * 动态朋友信息
 */
public class DynamicFriendsAdapter extends FragmentPagerAdapter {

    String uid;//

    public DynamicFriendsAdapter(FragmentManager fm, String uid) {
        super(fm);
        this.uid = uid;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        Bundle bundle = new Bundle();
        bundle.putString("uid",uid);
        switch (position) {
            case 0:
                fragment = new FriendsDynamicFragment();//动态
                break;
            case 1:
                fragment = new FriendsFriendsFragment();//好友
                break;
            case 2:
                fragment = new FriendsRankingFragment();//排名
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
                return "动态";
            case 1:
                return "好友";
            case 2:
                return "排名";
        }
        return super.getPageTitle(position);
    }
}
