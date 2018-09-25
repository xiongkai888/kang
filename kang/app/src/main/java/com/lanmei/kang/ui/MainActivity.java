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
import com.baidu.location.BDLocationListener;
import com.lanmei.kang.R;
import com.lanmei.kang.adapter.MainPagerAdapter;
import com.lanmei.kang.event.LocationEvent;
import com.lanmei.kang.helper.TabHelper;
import com.lanmei.kang.loader.DataLoader;
import com.lanmei.kang.ui.login.LoginActivity;
import com.lanmei.kang.util.SharedAccount;
import com.lanmei.kang.util.loc.LocationService;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

public class MainActivity extends BaseHxActivity implements TabLayout.OnTabSelectedListener {

    public static String MSG_COUNT = "MSG_COUNT";//收到的好友消息数通知

    @InjectView(R.id.viewPager)
    ViewPager mViewPager;
    @InjectView(R.id.tabLayout)
    TabLayout mTabLayout;
    TabHelper tabHelper;

    private MainPagerAdapter mMainPagerAdpter;

    @Override
    public int getContentViewId() {
        return R.layout.activity_main;
    }

    private static final int PERMISSION_LOCATION = 100;

    private void initPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_LOCATION);
            } else {
                initBaiDu();
            }
        } else {
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


    /*****
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     */
    private BDLocationListener mBDListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                double longitude = location.getLongitude();//经度
                double latitude = location.getLatitude();//纬度
//                L.d("HomeFragment", "经度: " + longitude + "  纬度 : " + latitude +"  定位所在城市：" + location.getCity());
                if (!StringUtils.isEmpty(location.getCity())) {
                    EventBus.getDefault().post(new LocationEvent(location.getCity(), longitude + "", latitude + ""));
                    SharedAccount.getInstance(MainActivity.this).saveCity(location.getCity());
                    SharedAccount.getInstance(MainActivity.this).saveLat(latitude + "");
                    SharedAccount.getInstance(MainActivity.this).saveLon(longitude + "");

                    mTabLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            stopLocationService();
                        }
                    }, 500);
                }
            }
        }

        public void onConnectHotSpotMessage(String s, int i) {
        }
    };

    private LocationService locationService;


    private void stopLocationService() {
        if (locationService != null) {
            locationService.unregisterListener(mBDListener);
            locationService.stop();
            locationService = null;
        }
    }

    private void initBaiDu() {
        // -----------location config ------------
        locationService = new LocationService(getApplicationContext());//放在SattingApp里面有问题
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mBDListener);
        //注册监听
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        locationService.start();// 定位SDK
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        int[] imageArray = {R.mipmap.home_on, R.mipmap.home_off, R.mipmap.news_on, R.mipmap.news_off, R.mipmap.dynamic_on, R.mipmap.dynamic_off, R.mipmap.mine_on, R.mipmap.mine_off};
        tabHelper = new TabHelper(this, getTitleList(), imageArray, mTabLayout, R.color.colorPrimaryDark);
        initViewPager();
        initPermission();//百度定位
    }


    public void initViewPager() {
        mMainPagerAdpter = new MainPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mMainPagerAdpter);
        mViewPager.setOffscreenPageLimit(3);
        mTabLayout.addOnTabSelectedListener(this);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLocationService();
        DataLoader.getInstance().clear();
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


    private List<String> getTitleList() {
        List<String> titles = new ArrayList<>();
        titles.add("商家");
        titles.add("资讯");
        titles.add("动态");
        titles.add("我的");
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
