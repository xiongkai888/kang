package com.lanmei.kang.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.lanmei.kang.R;
import com.lanmei.kang.adapter.MainPagerAdapter;
import com.lanmei.kang.event.LocationEvent;
import com.lanmei.kang.event.LoginQuitEvent;
import com.lanmei.kang.helper.TabHelper;
import com.lanmei.kang.loader.DataLoader;
import com.lanmei.kang.ui.login.LoginActivity;
import com.lanmei.kang.util.BaiduLocation;
import com.lanmei.kang.util.CommonUtils;
import com.lanmei.kang.util.SharedAccount;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.L;
import com.xson.common.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

public class MainActivity extends BaseHxActivity implements TabLayout.OnTabSelectedListener {

    public static String MSG_COUNT = "MSG_COUNT";//收到的好友消息数通知
    private static final int PERMISSION_LOCATION = 100;

    @InjectView(R.id.viewPager)
    ViewPager mViewPager;
    @InjectView(R.id.tabLayout)
    TabLayout mTabLayout;
    TabHelper tabHelper;

    private MainPagerAdapter mainPagerAdapter;

    @Override
    public int getContentViewId() {
        return R.layout.activity_main;
    }


    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);

        tabHelper = new TabHelper(this, mTabLayout, R.color.colorPrimaryDark);
        if (!StringUtils.isSame(CommonUtils.getUserType(this), CommonUtils.isZero)) {//用户
            tabHelper.setParameter(getTitleListU(), new int[]{R.mipmap.home_on, R.mipmap.home_off, R.mipmap.location_on, R.mipmap.location_off, R.mipmap.news_on, R.mipmap.news_off, R.mipmap.dynamic_on, R.mipmap.dynamic_off, R.mipmap.mine_on, R.mipmap.mine_off});
            mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager(), 5);
        } else {//商家
            tabHelper.setParameter(getTitleListM(), new int[]{R.mipmap.home_on, R.mipmap.home_off, R.mipmap.news_on, R.mipmap.news_off, R.mipmap.dynamic_on, R.mipmap.dynamic_off, R.mipmap.mine_on, R.mipmap.mine_off});
            mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager(), 4);
        }
        tabHelper.setupTabIcons();
        initViewPager();
        initPermission();//百度定位
    }

    private void initBaiDu() {
        new BaiduLocation(this, new BaiduLocation.WHbdLocationListener() {
            @Override
            public void bdLocationListener(LocationClient locationClient, BDLocation location) {
                if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                    double longitude = location.getLongitude();//经度
                    double latitude = location.getLatitude();//纬度
                    L.d("BaiduLocation", "经度: " + longitude + "  纬度 : " + latitude + "  定位所在城市：" + location.getCity());
                    if (!StringUtils.isEmpty(location.getCity())) {
                        EventBus.getDefault().post(new LocationEvent(location.getCity(), longitude + "", latitude + ""));
                        SharedAccount.getInstance(MainActivity.this).saveCity(location.getCity());
                        SharedAccount.getInstance(MainActivity.this).saveLat(latitude + "");
                        SharedAccount.getInstance(MainActivity.this).saveLon(longitude + "");
                    }
                    locationClient.stop();
                }
            }
        });
    }

    private void initPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_LOCATION);
            }else {
                initBaiDu();
            }
        }else {
            initBaiDu();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_LOCATION) {
            initBaiDu();
        }
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
        DataLoader.getInstance().clear();
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
        titles.add(getString(R.string.merchant));
        titles.add(getString(R.string.nearby));
        titles.add(getString(R.string.news));
        titles.add(getString(R.string.dynamic));
        titles.add(getString(R.string.mine));
        return titles;
    }

    //商家
    private List<String> getTitleListM() {
        List<String> titles = new ArrayList<>();
        titles.add(getString(R.string.merchant));
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
