package com.lanmei.kang.ui.merchant.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.lanmei.kang.R;
import com.lanmei.kang.adapter.MerchantIntroduceAdapter;
import com.lanmei.kang.api.CollectMerchantApi;
import com.lanmei.kang.api.MerchantDetailsApi;
import com.lanmei.kang.bean.MerchantDetailsBean;
import com.lanmei.kang.event.CollectItemsEvent;
import com.lanmei.kang.event.CollectMerchantEvent;
import com.lanmei.kang.ui.mine.activity.AlbumOtherActivity;
import com.lanmei.kang.util.CommonUtils;
import com.lanmei.kang.util.SharedAccount;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.bean.DataBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.DoubleUtil;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 商家介绍(商家详情)
 */
public class MerchantIntroduceActivity extends BaseActivity {


    @InjectView(R.id.banner)
    ConvenientBanner banner;
    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.collect_iv)
    ImageView collectIv;//是否收藏
    MerchantDetailsBean detailsBean;//商家详情信息
    String introduction;//商家介绍
    String phone;//电话
    @InjectView(R.id.name_tv)
    TextView nameTv;
    @InjectView(R.id.business_time_tv)
    TextView businessTimeTv;
    @InjectView(R.id.distance_tv)
    TextView distanceTv;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
    String favoured = "0";
    String merchantId;


    @Override
    public int getContentViewId() {
        return R.layout.activity_merchant_introduce;
    }

    @Override
    public void initIntent(Intent intent) {
        super.initIntent(intent);
        merchantId = intent.getStringExtra("value");
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle("商家详情");
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.mipmap.back);


        MerchantDetailsApi api = new MerchantDetailsApi();
        api.id = api.getUserId(this);
        api.uid = merchantId;
        api.lat = SharedAccount.getInstance(this).getLat();
        api.lon = SharedAccount.getInstance(this).getLon();
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<DataBean<MerchantDetailsBean>>() {
            @Override
            public void onResponse(DataBean<MerchantDetailsBean> response) {
                if (isFinishing()) {
                    return;
                }
                detailsBean = response.data;
                setParameter(detailsBean);
            }
        });
    }

    MerchantIntroduceAdapter adapter;//服务项目

    private void setParameter(MerchantDetailsBean detailsBean) {
        if (StringUtils.isEmpty(detailsBean)) {
            return;
        }

        CommonUtils.setBanner(banner, detailsBean.getPics(), true);//轮播图

        nameTv.setText(detailsBean.getName());
        businessTimeTv.setText(detailsBean.getStime() + " - " + detailsBean.getEtime());
        distanceTv.setText(DoubleUtil.formatDistance(detailsBean.getDistance()) + "  " + detailsBean.getAddress());
        introduction = detailsBean.getPlace_introduction();
        phone = detailsBean.getTel();
        favoured = detailsBean.getFavoured();

        if (StringUtils.isEmpty(detailsBean.getGoods())) {
            return;
        }
        List<MerchantDetailsBean.GoodsBean> list = detailsBean.getGoods();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            MerchantDetailsBean.GoodsBean bean = list.get(i);
            bean.setPlace_name(detailsBean.getName());
            bean.setMid(detailsBean.getId());//要是后台直接在GoodsBean里面加这两个字段就不用赋值了
        }
        adapter = new MerchantIntroduceAdapter(this, detailsBean.getTel());
        adapter.setData(list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);
        collectIv.setImageResource(StringUtils.isSame(detailsBean.getFavoured(), CommonUtils.isOne) ? R.mipmap.icon_collect_on : R.mipmap.icon_collect_off);
        EventBus.getDefault().register(this);
    }

    @OnClick({R.id.merchant_introduce_iv, R.id.collect_iv, R.id.phone_iv, R.id.ll_location,R.id.ll_merchant_album})
    public void showIntroduce(View view) {//商家介绍、收藏、联系电话
        if (!CommonUtils.isLogin(this)) {
            return;
        }
        if (StringUtils.isEmpty(detailsBean)) {
            return;
        }
        switch (view.getId()) {
            case R.id.merchant_introduce_iv://商家介绍
                if (StringUtils.isEmpty(introduction)) {
                    UIHelper.ToastMessage(this, "暂无介绍");
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable("bean", detailsBean);
                IntentUtil.startActivity(this, MerchantIntroduceSubActivity.class, bundle);
                break;
            case R.id.collect_iv://收藏
                collect();
                break;
            case R.id.phone_iv://联系电话
                if (StringUtils.isEmpty(phone)) {
                    UIHelper.ToastMessage(this, "暂无联系电话");
                    return;
                }
                UIHelper.callPhone(this, phone);
                break;
            case R.id.ll_location://定位
//                Uri location = Uri.parse("geo:0,0?q=1600+Amphitheatre+Parkway,+Mountain+View,+California");
                Uri location = Uri.parse("geo:" + detailsBean.getLat() + "," + detailsBean.getLon());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
                PackageManager packageManager = getPackageManager();
                List<ResolveInfo> activities = packageManager.queryIntentActivities(mapIntent, 0);
                boolean isIntentSafe = activities.size() > 0;
                if (isIntentSafe) {
                    startActivity(mapIntent);
                }else {
                    UIHelper.ToastMessage(this,"你尚未安装任何的地图应用");
                }
                break;
            case R.id.ll_merchant_album://商家相册
                IntentUtil.startActivity(this, AlbumOtherActivity.class,merchantId);
                break;
        }
    }

    private void collect() {
        CollectMerchantApi api = new CollectMerchantApi();
        api.id = merchantId;
        api.uid = api.getUserId(this);
        if (StringUtils.isSame(favoured, CommonUtils.isOne)) {
            api.del = favoured;
        }
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (StringUtils.isSame(favoured, CommonUtils.isOne)) {
                    favoured = CommonUtils.isZero;
                } else {
                    favoured = CommonUtils.isOne;
                }
                collectIv.setImageResource(StringUtils.isSame(favoured, CommonUtils.isOne) ? R.mipmap.icon_collect_on : R.mipmap.icon_collect_off);
                EventBus.getDefault().post(new CollectMerchantEvent());//从收藏列表点击进来的就通知刷新列表
                UIHelper.ToastMessage(MerchantIntroduceActivity.this, response.getInfo());
            }
        });
    }

    //收藏或取消收藏服务是调用
    @Subscribe
    public void collectItemsEvent(CollectItemsEvent event) {
        String id = event.getId();
        String favoured = event.getFavoured();
        if (adapter != null) {
            List<MerchantDetailsBean.GoodsBean> list = adapter.getData();
            if (StringUtils.isEmpty(list)) {
                return;
            }
            int size = list.size();
            for (int i = 0; i < size; i++) {
                MerchantDetailsBean.GoodsBean bean = list.get(i);
                if (StringUtils.isSame(id, bean.getId())) {
                    bean.setGood_favoured(favoured);
                    adapter.notifyDataSetChanged();
                    return;
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_message:
                if (!CommonUtils.isLogin(this)) {
                    break;
                }
                if (StringUtils.isEmpty(merchantId)) {
                    break;
                }
                CommonUtils.startChatActivity(this,merchantId,false);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
