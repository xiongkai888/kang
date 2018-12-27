package com.lanmei.kang.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.MainPagerAdapter;
import com.lanmei.kang.event.LoginQuitEvent;
import com.lanmei.kang.helper.TabHelper;
import com.lanmei.kang.ui.login.LoginActivity;
import com.lanmei.kang.update.UpdateAppConfig;
import com.xson.common.utils.IntentUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

public class MainActivity extends BaseHxActivity implements TabLayout.OnTabSelectedListener {

    public static String MSG_COUNT = "MSG_COUNT";//收到的好友消息数通知

    @InjectView(R.id.viewPager)
    ViewPager mViewPager;
    @InjectView(R.id.tabLayout)
    TabLayout mTabLayout;

    private MainPagerAdapter mainPagerAdapter;

    @Override
    public int getContentViewId() {
        return R.layout.activity_main;
    }


    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        UpdateAppConfig.requestStoragePermission(this);

        TabHelper  tabHelper = new TabHelper(this, mTabLayout, R.color.colorPrimaryDark);
//        tabHelper.setParameter(getTitleListU(), new int[]{R.mipmap.home_on, R.mipmap.home_off, R.mipmap.location_on, R.mipmap.location_off, R.mipmap.news_on, R.mipmap.news_off, R.mipmap.dynamic_on, R.mipmap.dynamic_off, R.mipmap.mine_on, R.mipmap.mine_off});
        tabHelper.setParameter(getTitleListU(), new int[]{R.mipmap.home_on, R.mipmap.home_off, R.mipmap.news_on, R.mipmap.news_off, R.mipmap.dynamic_on, R.mipmap.dynamic_off, R.mipmap.mine_on, R.mipmap.mine_off});
        mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        tabHelper.setupTabIcons();
        initViewPager();
//        AKDialog.showCouponDialog(this);
    }

    public void initViewPager() {
        mViewPager.setAdapter(mainPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);
        mTabLayout.addOnTabSelectedListener(this);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //登录或退出时候调用
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginQuitEvent(LoginQuitEvent event) {
        finish();
    }

    @Override
    public void login() {
        IntentUtil.startActivity(this, LoginActivity.class);
    }

    @Override
    public void updateUnreadMessageCount(int msgCount) {//接收到信息、从我的信息退出后才调用
        Intent intent = new Intent(MSG_COUNT);
        intent.putExtra("msgCount", msgCount);
        sendBroadcast(intent);
    }

    @Override
    public void updateTotalUnreadCount(int totalCount) {//从我的信息退出后才调用
//        Log.d("mianac","totalCount = "+totalCount);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int position = tab.getPosition();
        mViewPager.setCurrentItem(position);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    //用户
    private List<String> getTitleListU() {
        List<String> titles = new ArrayList<>();
        titles.add(getString(R.string.mall));
//        titles.add(getString(R.string.nearby));
        titles.add(getString(R.string.news));
        titles.add(getString(R.string.dynamic));
        titles.add(getString(R.string.mine));
        return titles;
    }

    public static final String ACTION_SHOW_HOME = "android.intent.action.SHOW_HOME";

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (ACTION_SHOW_HOME.equals(intent.getAction())) {
            mViewPager.setCurrentItem(0);
        }
    }

    public static void showHome(Context context) {//至首页
        Intent intent = new Intent(ACTION_SHOW_HOME);
        intent.setClass(context, MainActivity.class);
        context.startActivity(intent);
    }

}
