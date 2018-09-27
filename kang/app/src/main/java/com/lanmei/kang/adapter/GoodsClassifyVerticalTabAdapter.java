package com.lanmei.kang.adapter;

import android.content.Context;

import com.lanmei.kang.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import q.rorbin.verticaltablayout.adapter.TabAdapter;
import q.rorbin.verticaltablayout.widget.TabView;

/**
 * Created by xkai on 2017/6/9.
 * 订单大模块的垂直tab
 */

public class GoodsClassifyVerticalTabAdapter implements TabAdapter {

    List<String> menus;
    Context mContext;

    public GoodsClassifyVerticalTabAdapter(Context context) {
        menus = new ArrayList<>();
        Collections.addAll(menus, "面膜"//0
                , "爽肤水"//2
                , "乳液"//3
        );
        mContext = context;
    }

    @Override
    public int getCount() {
        return menus.size();
    }

    //    @Override
//    public TabView.TabBadge getBadge(int position) {
//        return new TabView.TabBadge.Builder().setBadgeNumber(2).setBackgroundColor(0xff2faae5)
//                .setOnDragStateChangedListener(new Badge.OnDragStateChangedListener() {
//                    @Override
//                    public void onDragStateChanged(int dragState, Badge badge, View targetView) {
//                    }
//                }).build();
//    }
    @Override
    public TabView.TabBadge getBadge(int position) {
        return null;
    }

    @Override
    public TabView.TabIcon getIcon(int position) {//没有图片
//        MenuBean menu = menus.get(position);
//        return new TabView.TabIcon.Builder()
//                .setIcon(menu.mSelectIcon, menu.mNormalIcon)
//                .setIconGravity(Gravity.START)
//                .setIconMargin(dp2px(5))
//                .setIconSize(dp2px(20), dp2px(20))
//                .build();
        return null;
    }
//    @Override
//    public TabView.TabIcon getIcon(int position) {
//        MenuBean menu = menus.get(position);
//        return new TabView.TabIcon.Builder()
//                .setIcon(menu.mSelectIcon, menu.mNormalIcon)
//                .setIconGravity(Gravity.START)
//                .setIconMargin(dp2px(5))
//                .setIconSize(dp2px(20), dp2px(20))
//                .build();
//    }

    @Override
    public TabView.TabTitle getTitle(int position) {
        return new TabView.TabTitle.Builder()
                .setContent(menus.get(position))
                .setTextColor(mContext.getResources().getColor(R.color.colorPrimaryDark), mContext.getResources().getColor(R.color.black))
                .build();
    }

    @Override
    public int getBackground(int position) {
        return -1;
    }

    protected int dp2px(float dp) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

}
