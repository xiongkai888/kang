package com.lanmei.kang.ui.merchant;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.MerchantOrderAdapter;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.event.ScanEvent;
import com.lanmei.kang.event.ScanSucceedEvent;
import com.lanmei.kang.qrcode.Des;
import com.lanmei.kang.qrcode.ScanActivity;
import com.lanmei.kang.updateversion.JsonUtil;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.L;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import butterknife.InjectView;

/**
 * 商家订单
 */
public class MerchantOrderActivity extends BaseActivity implements TabLayout.OnTabSelectedListener{


    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;

    @InjectView(R.id.viewPager)
    ViewPager mViewPager;
    @InjectView(R.id.tabLayout)
    TabLayout mTabLayout;
    MerchantOrderAdapter mAdapter;

    @Override
    public int getContentViewId() {
        return R.layout.activity_merchant_order;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle("订单");
        actionbar.setHomeAsUpIndicator(R.mipmap.back_g);

        mAdapter = new MerchantOrderAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mTabLayout.addOnTabSelectedListener(this);
        mTabLayout.setupWithViewPager(mViewPager);

        EventBus.getDefault().register(this);
    }

    //扫码成功时调用
    @Subscribe
    public void scanEvent(ScanEvent event){
        String result = event.getResult();
        try {
            Des des = new Des();
            L.d("onScanQRCodeSuccess", "加密后的数据=" + result);
            L.d("onScanQRCodeSuccess", "解码后=" + des.decode(result));
            JSONObject json = JsonUtil.stringToJson(des.decode(result));
            if (json == null){
                return;
            }
            KangQiMeiApi api = new KangQiMeiApi("Reservation/service");

            api.addParams("id",json.getString("id"));
            api.addParams("code",json.getString("code"));
            api.addParams("time",json.getString("time"));

            HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
                @Override
                public void onResponse(BaseBean response) {
                    UIHelper.ToastMessage(MerchantOrderActivity.this,response.getInfo());
                    EventBus.getDefault().post(new ScanSucceedEvent());//通知商家列表刷新
                    finish();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            UIHelper.ToastMessage(this,"消费失败");
            L.d("onScanQRCodeSuccess", "异常=" + e.getMessage());
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_qr_code,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_scan_qr_code:
                Bundle bundle = new Bundle();
                bundle.putInt("type",3);//商家订单
                bundle.putBoolean("isQR",false);//二维码
                IntentUtil.startActivity(this, ScanActivity.class, bundle);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
