package com.lanmei.kang.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.lanmei.kang.KangApp;
import com.lanmei.kang.R;
import com.lanmei.kang.adapter.GuideViewPagerAdapter;
import com.lanmei.kang.util.CommonUtils;
import com.lanmei.kang.util.SharedAccount;
import com.xson.common.helper.UserHelper;
import com.xson.common.utils.IntentUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 引导页、启动也
 */
public class SplashActivity extends AppCompatActivity{

    private ImageView mSkipIv;//立即体验
    int[] guide = new int[]{R.mipmap.guidance1, R.mipmap.guidance2, R.mipmap.guidance3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏
        setContentView(R.layout.activity_splash);
        boolean isFirstLogin = SharedAccount.getInstance(SplashActivity.this).isFirstLogin();
        if (!isFirstLogin){//第一次进入该应用
            initViewPager();
        }else {
            ImageView mLaunchIv = (ImageView) findViewById(R.id.launch_iv);
            mLaunchIv.setVisibility(View.VISIBLE);
            // 如果不是第一次启动app，则正常显示启动屏
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    enterHomeActivity();
                }
            }, 1000);
        }
        if (UserHelper.getInstance(this).hasLogin()){
            CommonUtils.loadUserInfo(KangApp.applicationContext,null);
        }
    }

    private void enterHomeActivity() {
        IntentUtil.startActivity(this, MainActivity.class);
        finish();
    }

    private void initViewPager() {
        ViewPager vp = (ViewPager) findViewById(R.id.vp_guide);
        mSkipIv = (ImageView) findViewById(R.id.skip_tv);
        mSkipIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedAccount.getInstance(SplashActivity.this).setNoFirstLogin(true);
                enterHomeActivity();
            }
        });
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 2){
                    mSkipIv.setVisibility(View.VISIBLE);
                }else {
                    mSkipIv.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        // 初始化引导页视图列表
        List<View> views = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.guid_view, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
            imageView.setImageResource(guide[i]);
            views.add(view);
        }
        vp = (ViewPager) findViewById(R.id.vp_guide);
        // 初始化adapter
        GuideViewPagerAdapter adapter = new GuideViewPagerAdapter(views);
        vp.setAdapter(adapter);
    }
}
