package com.lanmei.kang.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.lanmei.kang.loader.DataLoader;
import com.lanmei.kang.ui.home.fragment.HuoDongFragment;
import com.lanmei.kang.util.Constant;
import com.xson.common.utils.StringUtils;

/**
 *  首页高端游学、商务会议、聚会联谊
 */
public class HuoDongAdapter extends FragmentPagerAdapter {


    public HuoDongAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        HuoDongFragment fragment = HuoDongFragment.newInstance();
        Bundle bundle = new Bundle();
        String cid = "";
        switch (position) {
            case 0:
                cid = DataLoader.getInstance().getCid(Constant.GAO_DUAN);
                if (StringUtils.isEmpty(cid)){
                    cid = "14";
                }
                break;
            case 1:
                cid = DataLoader.getInstance().getCid(Constant.SHANG_WU);
                if (StringUtils.isEmpty(cid)){
                    cid = "15";
                }
                break;
            case 2:
                cid = DataLoader.getInstance().getCid(Constant.JU_HUI);
                if (StringUtils.isEmpty(cid)){
                    cid = "16";
                }
                break;

        }
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
                return Constant.GAO_DUAN;
            case 1:
                return Constant.SHANG_WU;
            case 2:
                return Constant.JU_HUI;
        }

        return null;
    }

}
