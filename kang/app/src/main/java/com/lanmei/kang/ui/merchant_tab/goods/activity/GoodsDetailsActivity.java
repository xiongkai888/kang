package com.lanmei.kang.ui.merchant_tab.goods.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.bean.GoodsDetailsBean;
import com.lanmei.kang.bean.GoodsSpecificationsBean;
import com.lanmei.kang.event.LoginQuitEvent;
import com.lanmei.kang.event.PaySucceedEvent;
import com.lanmei.kang.ui.merchant_tab.goods.GoodsDetailsPagerAdapter;
import com.lanmei.kang.ui.merchant_tab.goods.fragment.AddShopCarDialogFragment;
import com.lanmei.kang.ui.merchant_tab.goods.shop.DBShopCartHelper;
import com.lanmei.kang.ui.merchant_tab.goods.shop.ShopCarActivity;
import com.lanmei.kang.ui.merchant_tab.goods.shop.ShowShopCountEvent;
import com.lanmei.kang.util.CommonUtils;
import com.lanmei.kang.widget.NoScrollViewPager;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.bean.DataBean;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.helper.UserHelper;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 商品详情（商品、详情、评论）
 */
public class GoodsDetailsActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar toolbar;
    @InjectView(R.id.tabLayout)
    TabLayout tabLayout;
    @InjectView(R.id.viewPager)
    NoScrollViewPager viewPager;
    @InjectView(R.id.collect_iv)
    ImageView collectIv;
    @InjectView(R.id.num_tv)
    TextView shopNumTv;//购物车数量
    GoodsDetailsBean bean;//商品详情信息
    String id;//商品详情ID
    AddShopCarDialogFragment mDialog;
    private static final String DIALOG = "dialog_fragment_kang";

    @Override
    public void initIntent(Intent intent) {
        super.initIntent(intent);
        id = intent.getStringExtra("value");
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_goods_details;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle("");
        actionbar.setHomeAsUpIndicator(R.mipmap.back_g);
        EventBus.getDefault().register(this);

        loadSpecifications();

        KangQiMeiApi api = new KangQiMeiApi("app/goodsdetail");
        api.addParams("id",id);
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<DataBean<GoodsDetailsBean>>() {
            @Override
            public void onResponse(DataBean<GoodsDetailsBean> response) {
                if (isFinishing()) {
                    return;
                }
                bean = response.data;
                if (!StringUtils.isEmpty(bean)) {
                    init(bean);
                }
            }
        });
    }


//    int favorite;//是否收藏了该商品

    private void init(GoodsDetailsBean bean) {
//        favorite = bean.getFavorite();
//        if (favorite == 1) {
//            collectIv.setImageResource(R.mipmap.icon_collect_on);
//        } else {
//            collectIv.setImageResource(R.mipmap.icon_collect_off);
//        }
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(new GoodsDetailsPagerAdapter(getSupportFragmentManager(), bean));
        tabLayout.setupWithViewPager(viewPager);

        if (UserHelper.getInstance(this).hasLogin()){
            showShopCount();
//            loadCollect(false);
        }

    }

    public void operaTitleBar(boolean scroAble, boolean titleVisiable) {
        viewPager.setNoScroll(scroAble);
        toolbar.setTitle(titleVisiable?getString(R.string.graphic_details):"");
        tabLayout.setVisibility(titleVisiable ? View.GONE : View.VISIBLE);
    }

    //到评论
    public void toCommentPager() {
        viewPager.setCurrentItem(2);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (!CommonUtils.isLogin(this)) {
            return super.onOptionsItemSelected(item);
        }
        switch (item.getItemId()) {
            case R.id.action_share:
                CommonUtils.developing(this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @OnClick({R.id.ll_collect, R.id.ll_shop, R.id.add_shop_car_tv, R.id.pay_now_tv})
    public void onViewClicked(View view) {
        if (!CommonUtils.isLogin(this)) {
            return;
        }
        if (StringUtils.isEmpty(bean)) {
            return;
        }
        switch (view.getId()) {
            case R.id.ll_collect://收藏
                CommonUtils.developing(this);
//                loadCollect(true);
                break;
            case R.id.ll_shop://购物车
                IntentUtil.startActivity(this, ShopCarActivity.class);
                break;
            case R.id.add_shop_car_tv://加入购物车
            case R.id.pay_now_tv://立即购买
                addShopCar();
                break;
        }
    }

    private void addShopCar() {
        if (!isSpecifications){
            return;
        }
        if (mDialog == null) {
            mDialog = new AddShopCarDialogFragment();
            mDialog.setData(bean,specificationsBeans);
        }
        mDialog.show(getSupportFragmentManager(), DIALOG);
    }

    private void loadCollect(boolean isClick) {
        if (isClick){
            CommonUtils.developing(this);
        }
        if (StringUtils.isEmpty(id)) {
            return;
        }
        KangQiMeiApi api = new KangQiMeiApi("app/collection");
        api.addParams("userid",api.getUserId(this)).addParams("goodsid",id);
        if (isClick){

        }
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()) {
                    return;
                }
//                String collect = "";
//                if (favorite == 1) {
//                    favorite = 0;
//                    collect = getString(R.string.collect);
//                    collectIv.setImageResource(R.mipmap.goods_collect_off);
//                } else {
//                    collect = getString(R.string.collected);
//                    favorite = 1;
//                    collectIv.setImageResource(R.mipmap.goods_collect_on);
//                }
                UIHelper.ToastMessage(GoodsDetailsActivity.this, response.getInfo());
            }
        });
    }

    private boolean isSpecifications = false;
    private List<GoodsSpecificationsBean> specificationsBeans;

    private void loadSpecifications(){
        KangQiMeiApi api = new KangQiMeiApi("app/good_specifications");
        api.addParams("gid",id);
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<NoPageListBean<GoodsSpecificationsBean>>() {
            @Override
            public void onResponse(NoPageListBean<GoodsSpecificationsBean> response) {
                if (isFinishing()) {
                    return;
                }
                isSpecifications = true;
                specificationsBeans = response.data;
            }
        });
    }

    //支付成功调用
    @Subscribe
    public void paySucceedEvent(PaySucceedEvent event) {
        finish();
    }

    //(加入购物车、删除购物车)显示购物车数量事件
    @Subscribe
    public void showShopCarCountEvent(ShowShopCountEvent event) {
        showShopCount();
    }

    //显示购物车数量
    private void showShopCount() {
        int count = DBShopCartHelper.getInstance(getApplicationContext()).getShopCarListCount();
        shopNumTv.setText(String.valueOf(count));
        shopNumTv.setVisibility(count == 0?View.GONE:View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
//        mShareHelper.onDestroy();
    }

    /**
     * 结果返回
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        mShareHelper.onActivityResult(requestCode, resultCode, data);
    }


    //登录或退出时候调用（在此页面跳转到登陆界面时候）
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginQuitEvent(LoginQuitEvent event) {
        finish();
    }
}
