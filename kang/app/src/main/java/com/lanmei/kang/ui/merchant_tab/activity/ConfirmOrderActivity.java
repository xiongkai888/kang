package com.lanmei.kang.ui.merchant_tab.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.data.volley.Response;
import com.data.volley.error.VolleyError;
import com.lanmei.kang.R;
import com.lanmei.kang.adapter.PayWayAdapter;
import com.lanmei.kang.alipay.AlipayHelper;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.bean.AddressListBean;
import com.lanmei.kang.bean.DistributionBean;
import com.lanmei.kang.bean.PayWayBean;
import com.lanmei.kang.bean.WeiXinBean;
import com.lanmei.kang.event.AddressListEvent;
import com.lanmei.kang.event.ChooseAddressEvent;
import com.lanmei.kang.event.PaySucceedEvent;
import com.lanmei.kang.helper.CouponListDialogFragment;
import com.lanmei.kang.helper.WXPayHelper;
import com.lanmei.kang.helper.coupon.BeanCoupon;
import com.lanmei.kang.helper.coupon.GoodsCoupons;
import com.lanmei.kang.ui.merchant_tab.goods.activity.AddressListActivity;
import com.lanmei.kang.ui.merchant_tab.goods.shop.ShopCarBean;
import com.lanmei.kang.ui.mine.activity.MyGoodsOrderActivity;
import com.lanmei.kang.util.CommonUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.bean.DataBean;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.L;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.FormatTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.OptionPicker;

/**
 * 确认订单
 */
public class ConfirmOrderActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
    @InjectView(R.id.name_tv)
    TextView nameTv;
    @InjectView(R.id.address_tv)
    TextView addressTv;
    @InjectView(R.id.distribution_tv)
    TextView distributionTv;
    @InjectView(R.id.coupon_name_tv)
    TextView couponNameTv;//选择的优惠券
    @InjectView(R.id.goods_num_tv)
    TextView goodsNumTv;//商品个数
    @InjectView(R.id.goods_price_tv)
    TextView goodsPriceTv;//总的商品价格（扣除优惠券前）
    @InjectView(R.id.price_tv)
    FormatTextView priceTv;//总的商品价格（扣除优惠券后）
    @InjectView(R.id.recyclerViewShop)
    RecyclerView recyclerViewShop;//商品列表
    private List<ShopCarBean> list;//提交的商品列表
    private List<DistributionBean> distributionBeanList;//配送列表
    private OptionPicker picker;//
    private AddressListBean addressBean;//地址信息
    private List<AddressListBean> addressListBeans;
    private int type;
    private String distributionID;//配送id
    private int goodsNum;
    private GoodsCoupons goodsCoupons;//优惠券工具类
    private BeanCoupon coupon;//用户选择的优惠券
    private List<BeanCoupon> couponList;//获取的符合条件的优惠券列表
    private double price;//
    private CouponListDialogFragment couponListDialogFragment;


    @Override
    public int getContentViewId() {
        return R.layout.activity_confirm_order;
    }

    @Override
    public void initIntent(Intent intent) {
        super.initIntent(intent);
        Bundle bundle = intent.getBundleExtra("bundle");
        if (bundle != null) {
            list = (List<ShopCarBean>) bundle.getSerializable("list");
        }
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);

        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.confirm_order);
        actionbar.setHomeAsUpIndicator(R.mipmap.back_g);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        checkAddress();

        if (StringUtils.isEmpty(list)) {
            UIHelper.ToastMessage(this, "获取商品异常");
            finish();
            return;
        }
        goodsCoupons = new GoodsCoupons(this, new GoodsCoupons.OnGouponsListener() {
            @Override
            public void onChangeCoupon(List<BeanCoupon> result) {
                if (!StringUtils.isEmpty(result)) {
                    L.d("ConfirmOrderActivity", "result = "+result.size());
                    couponList = result;
                    BeanCoupon couponNew = new BeanCoupon();
                    couponNew.setLname("不使用优惠券");
                    couponNew.setId("");
                    couponNew.setMoney(Double.valueOf(0));
                    couponList.add(couponNew);
                    coupon = result.get(0);//最大面值优惠券
                    coupon.setChoose(true);//第一个默认选中
                    couponNameTv.setText(coupon.getLname());
                    String s = String.format("%.1f", price - coupon.getMoney());
                    priceTv.setTextValue(s);//
                    for (int i = 0; i < result.size(); i++) {
                        L.d("ConfirmOrderActivity", "result = " + i + " = " + result.get(i).toString());
                    }
                }
            }
        });

        price = getPrice();

        String s = String.format("%.1f", price);

        priceTv.setTextValue(s);
        goodsPriceTv.setText(String.format(getString(R.string.price), s));
        goodsNumTv.setText(goodsNum + "");
        ConfirmOrderAdapter adapter = new ConfirmOrderAdapter(this);
        adapter.setData(list);
        recyclerViewShop.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewShop.setNestedScrollingEnabled(false);
        recyclerViewShop.setAdapter(adapter);

        loadDistribution();//配送方式
        loadPayment();//支付方式
        loadAddressList();
        loadCoupon();//优惠券
    }

    public void setCoupon(BeanCoupon coupon) {
        this.coupon = coupon;
        couponNameTv.setText(coupon.getLname());
        String s = String.format("%.1f", price - coupon.getMoney());
        priceTv.setTextValue(s);//
    }

    //优惠券
    private void loadCoupon() {
        KangQiMeiApi api = new KangQiMeiApi("app/coupon");
        api.add("order", 1);
        api.add("uid", api.getUserId(this));
        HttpClient.newInstance(this).request(api, new BeanRequest.SuccessListener<NoPageListBean<BeanCoupon>>() {
            @Override
            public void onResponse(NoPageListBean<BeanCoupon> response) {
                if (isFinishing()) {
                    return;
                }
                goodsCoupons.setCouponsList(response.data);
                goodsCoupons.setGoodsTypes(list);
            }
        });
    }

    private double getPrice() {
        goodsNum = 0;
        double price = 0;
        for (ShopCarBean bean : list) {
            price += Double.valueOf(CommonUtils.getRatioPrice(this, String.valueOf(bean.getSell_price()), new DecimalFormat(CommonUtils.ratioStr))) * bean.getGoodsCount();
            goodsNum += bean.getGoodsCount();
        }
        return price;
    }

    private void initPicker() {
        picker = new OptionPicker(this, toStringList());
        picker.setOffset(2);
        picker.setSelectedIndex(1);
        picker.setTextSize(16);
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                distributionID = distributionBeanList.get(index).getId();
                distributionTv.setText(item);
            }
        });
    }


    private List<String> toStringList() {
        List<String> list = new ArrayList<>();
        for (DistributionBean bean : distributionBeanList) {
            list.add(bean.getClassname());
        }
        return list;
    }

    //配送列表
    private void loadDistribution() {
        KangQiMeiApi api = new KangQiMeiApi("app/distribution_list");
        api.add("tablename", "distribution");
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<NoPageListBean<DistributionBean>>() {
            @Override
            public void onResponse(NoPageListBean<DistributionBean> response) {
                if (isFinishing()) {
                    return;
                }
                distributionBeanList = response.data;
                if (StringUtils.isEmpty(distributionBeanList)) {
                    return;
                }
                initPicker();
            }
        });
    }

    //支付方式
    private void loadPayment() {
        KangQiMeiApi api = new KangQiMeiApi("app/payment");
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<NoPageListBean<PayWayBean>>() {
            @Override
            public void onResponse(NoPageListBean<PayWayBean> response) {
                if (isFinishing()) {
                    return;
                }
                PayWayAdapter adapter = new PayWayAdapter(getContext());
                adapter.setData(response.data);
                recyclerView.setAdapter(adapter);
                adapter.setPayWayListener(new PayWayAdapter.PayWayListener() {
                    @Override
                    public void payId(String id) {
                        type = Integer.valueOf(id);
                    }
                });
            }
        });
    }


    @OnClick({R.id.ll_address, R.id.submit_order_tv, R.id.ll_distribution, R.id.ll_coupon})
    public void onViewClicked(View view) {
        if (StringUtils.isEmpty(list)) {
            return;
        }
        switch (view.getId()) {
            case R.id.ll_address:
                IntentUtil.startActivity(this, AddressListActivity.class);
                break;
            case R.id.submit_order_tv://提交订单
                submitOrder();
                break;
            case R.id.ll_distribution://配送方式
                if (picker != null) {
                    picker.show();
                }
                break;
            case R.id.ll_coupon://优惠券
                if (StringUtils.isEmpty(couponList)) {
                    UIHelper.ToastMessage(this, "没有可用优惠券");
                    return;
                }
//                couponListDialogFragment
                if (couponListDialogFragment == null) {
                    couponListDialogFragment = new CouponListDialogFragment();
                    couponListDialogFragment.setCouponBeanList(couponList);
                }
                couponListDialogFragment.show(getSupportFragmentManager(), "111");
                break;
        }
    }

    private void submitOrder() {
        if (StringUtils.isEmpty(addressBean)) {
            UIHelper.ToastMessage(this, R.string.choose_address);
            return;
        }
        if (StringUtils.isEmpty(distributionID)) {
            UIHelper.ToastMessage(this, "请选择配送方式");
            return;
        }
        if (StringUtils.isEmpty(type)) {
            UIHelper.ToastMessage(this, getString(R.string.pay_type));
            return;
        }
        StringBuilder goodsidBuilder = new StringBuilder();
        StringBuilder goodsnameBuilder = new StringBuilder();
        StringBuilder numBuilder = new StringBuilder();
        StringBuilder gidBuilder = new StringBuilder();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            ShopCarBean bean = list.get(i);
            goodsidBuilder.append(bean.getGoods_id()).append(((size - 1) != i) ? L.cornet : "");
            goodsnameBuilder.append(bean.getGoodsName()).append(((size - 1) != i) ? L.cornet : "");
            numBuilder.append(String.valueOf(bean.getGoodsCount())).append(((size - 1) != i) ? L.cornet : "");
            gidBuilder.append(!StringUtils.isEmpty(bean.getGid()) ? bean.getGid() : CommonUtils.isZero).append(((size - 1) != i) ? L.cornet : "");
        }
        KangQiMeiApi api = new KangQiMeiApi("app/createorder");
        api.add("pay_type", type).add("uid", api.getUserId(this)).add("goodsid", goodsidBuilder.toString())
                .add("goodsname", goodsnameBuilder.toString()).add("num", numBuilder.toString()).add("username", addressBean.getAccept_name())
                .add("phone", addressBean.getMobile()).add("address", addressBean.getAddress()).add("lid", StringUtils.isEmpty(coupon) ? "" : coupon.getId())//优惠券id
                .add("dis_type", distributionID).add("gid", gidBuilder.toString());
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<DataBean<Integer>>() {
            @Override
            public void onResponse(DataBean<Integer> response) {
                if (isFinishing()) {
                    return;
                }
                loadPayMent(response.data);
            }
        });

    }


    private void loadPayMent(int order_id) {
        KangQiMeiApi api = new KangQiMeiApi("app/pay");
        api.add("order_id", order_id).add("uid", api.getUserId(this)).add("id", order_id);
        HttpClient httpClient = HttpClient.newInstance(this);
        if (type == 1) {//支付宝支付
            httpClient.loadingRequest(api, new BeanRequest.SuccessListener<DataBean<String>>() {
                @Override
                public void onResponse(DataBean<String> response) {
                    if (isFinishing()) {
                        return;
                    }
                    AlipayHelper alipayHelper = new AlipayHelper(getContext());
                    alipayHelper.setPayParam(response.data);
                    alipayHelper.payNow();
                }
            });
        } else if (type == 7) {//微信支付
            httpClient.loadingRequest(api, new BeanRequest.SuccessListener<DataBean<WeiXinBean>>() {
                @Override
                public void onResponse(DataBean<WeiXinBean> response) {
                    if (isFinishing()) {
                        return;
                    }
                    WeiXinBean bean = response.data;
                    WXPayHelper payHelper = new WXPayHelper(getContext());
                    payHelper.setPayParam(bean);
                    payHelper.orderNow();
                }
            });
        } else if (type == 6) {//余额
            httpClient.loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
                @Override
                public void onResponse(BaseBean response) {
                    if (isFinishing()) {
                        return;
                    }
                    UIHelper.ToastMessage(getContext(), response.getInfo());
                    IntentUtil.startActivity(getContext(), MyGoodsOrderActivity.class);
                    EventBus.getDefault().post(new PaySucceedEvent());
                    finish();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (isFinishing()) {
                        return;
                    }
                    UIHelper.ToastMessage(getContext(), error.getMessage());
                }
            });
        }
    }

    private void loadAddressList() {
        KangQiMeiApi api = new KangQiMeiApi("app/address");
        api.add("uid", api.getUserId(this));
//        api.add("uid",46);
        api.add("operation", 4);
        HttpClient.newInstance(this).request(api, new BeanRequest.SuccessListener<NoPageListBean<AddressListBean>>() {
            @Override
            public void onResponse(NoPageListBean<AddressListBean> response) {
                if (isFinishing()) {
                    return;
                }
                addressListBeans = response.data;
                if (!StringUtils.isEmpty(addressListBeans)) {
                    for (AddressListBean bean : addressListBeans) {
                        if (!StringUtils.isEmpty(bean) && StringUtils.isSame(CommonUtils.isOne, bean.getDefaultX())) {
                            chooseAddress(bean);
                            return;
                        }
                    }
                }
                checkAddress();
            }
        });
    }

    private void chooseAddress(AddressListBean bean) {
        addressBean = bean;
        nameTv.setVisibility(View.VISIBLE);
        nameTv.setText(addressBean.getAccept_name() + "\u3000\u3000" + addressBean.getMobile());
        addressTv.setText(bean.getAddress());
    }

    //支付宝微信支付成功调用
    @Subscribe
    public void chooseAddressEvent(ChooseAddressEvent event) {
        chooseAddress(event.getBean());
    }

    //选择地址的时候调用
    @Subscribe
    public void paySucceedEvent(PaySucceedEvent event) {
        IntentUtil.startActivity(getContext(), MyGoodsOrderActivity.class);
        finish();
    }

    //选择地址列表获取地址列表的时候调用
    @Subscribe
    public void addressListEvent(AddressListEvent event) {
        addressListBeans = event.getList();
        if (StringUtils.isEmpty(addressListBeans)) {
            checkAddress();
        } else {
            if (!StringUtils.isEmpty(addressBean)) {
                for (AddressListBean bean : addressListBeans) {
                    if (StringUtils.isSame(bean.getId(), addressBean.getId())) {
                        chooseAddress(bean);
                        return;
                    }
                }
                checkAddress();
            }
        }
    }

    private void checkAddress() {
        addressBean = null;
        nameTv.setVisibility(View.GONE);
        addressTv.setText(R.string.choose_address);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
