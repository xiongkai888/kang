package com.lanmei.kang.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hyphenate.chatuidemo.ui.ConversationListFragment;
import com.lanmei.kang.ui.mine.fragment.GoodFriendsFragment;

/**
 * 达人  好友  私信和好友
 */
public class GoodFriendsAdapter extends FragmentPagerAdapter {


    public GoodFriendsAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ConversationListFragment fragment = new ConversationListFragment();
                Bundle bundle = new Bundle();
                bundle.putString("hide","hide");//隐藏titleBar
                fragment.setArguments(bundle);
               return fragment;
            case 1:
                GoodFriendsFragment friendsFragment = GoodFriendsFragment.newInstance();
                Bundle friendsBundle = new Bundle();
//                friendsBundle.putString("uid",uid);//
                friendsFragment.setArguments(friendsBundle);
               return friendsFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position){
            case 0:
                title = "私信";
                break;
            case 1:
                title = "好友";
                break;
        }
        return title;
    }
}
