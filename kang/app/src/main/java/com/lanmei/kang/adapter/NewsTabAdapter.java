package com.lanmei.kang.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.lanmei.kang.ui.news.fragment.NewsListFragment;

/**
 * Created by xkai on 2017/4/25.
 */

public class NewsTabAdapter extends FragmentStatePagerAdapter {


    public NewsTabAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        NewsListFragment  fragment = new NewsListFragment();
        Bundle bundle = new Bundle();
        switch (position){
            case 0:
                bundle.putString("cid","11");
                break;
            case 1:
                bundle.putString("cid","12");
                break;
            case 2:
                bundle.putString("cid","13");
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
        switch (position) {
            case 0:
                return "热门资讯";
            case 1:
                return "健康资讯";
            case 2:
                return "中医资讯";
        }

        return null;
    }

}
