package com.lanmei.kang.ui.merchant_tab.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.GoodsOrderListSubAdapter;
import com.lanmei.kang.alipay.AlipayHelper;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.bean.GoodsOrderListBean;
import com.lanmei.kang.bean.WeiXinBean;
import com.lanmei.kang.event.OrderOperationEvent;
import com.lanmei.kang.event.PaySucceedEvent;
import com.lanmei.kang.helper.WXPayHelper;
import com.lanmei.kang.ui.mine.activity.MyGoodsOrderActivity;
import com.lanmei.kang.util.AKDialog;
import com.lanmei.kang.util.CommonUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.bean.DataBean;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
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
 * 订单详情
 */
public class OrderDetailsGoodsActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.name_tv)
    TextView nameTv;
    @InjectView(R.id.address_tv)
    TextView addressTv;
    @InjectView(R.id.num_tv)
    TextView numTv;
    @InjectView(R.id.fee_tv)
    TextView feeTv;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
    @InjectView(R.id.goods_price_tv)
    TextView goodsPriceTv;
    @InjectView(R.id.total_price_tv)
    TextView totalPriceTv;
    @InjectView(R.id.pay_way_tv)
    TextView payWayTv;//支付方式
    GoodsOrderListBean bean;//我的订单item信息
    @InjectView(R.id.order_1)
    TextView order_1;
    @InjectView(R.id.order_2)
    TextView order_2;
    @InjectView(R.id.order_3)
    TextView order_3;
    @InjectView(R.id.scrollView)
    NestedScrollView scrollView;
    @InjectView(R.id.order_no_tv)
    TextView orderNoTv;//订单号
    @InjectView(R.id.courier_tv)
    TextView courierTv;//物流单号
    private String id;

    @Override
    public int getContentViewId() {
        return R.layout.activity_order_details_goods;
    }

    @Override
    public void initIntent(Intent intent) {
        super.initIntent(intent);
        id = intent.getStringExtra("value");
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        scrollView.setVisibility(View.GONE);
        order_1.setVisibility(View.GONE);
        order_2.setVisibility(View.GONE);
        order_3.setVisibility(View.GONE);

        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.order_details);
        actionbar.setHomeAsUpIndicator(R.mipmap.back_g);
//        setData();
        if (StringUtils.isEmpty(id)) {
            return;
        }
        loadOrderDetails();
    }

    private void loadOrderDetails() {
        KangQiMeiApi api = new KangQiMeiApi("app/order_details");
        api.add("uid", api.getUserId(this)).add("id", id);
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<NoPageListBean<GoodsOrderListBean>>() {
            @Override
            public void onResponse(NoPageListBean<GoodsOrderListBean> response) {
                if (isFinishing()) {
                    return;
                }
                List<GoodsOrderListBean> list = response.data;
                if (StringUtils.isEmpty(list)) {
                    return;
                }
                bean = list.get(0);
                setData();
            }
        });
    }

    private String status;
    private String paysStatus;
    private String oid;

    private void setData() {
        if (StringUtils.isEmpty(bean)) {
            return;
        }
        scrollView.setVisibility(View.VISIBLE);
        oid = bean.getId();
        nameTv.setText(bean.getUsername() + "\u3000" + bean.getPhone());
        addressTv.setText(bean.getAddress());
        orderNoTv.setText(String.format(getString(R.string.order_no), bean.getOrder_no()));
        courierTv.setText(String.format(getString(R.string.courier_no), bean.getCourier()));

        GoodsOrderListSubAdapter adapter = new GoodsOrderListSubAdapter(this);
        adapter.setData(bean.getGoods());
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        numTv.setText(bean.getNum() + "");
        //        feeTv.setText(String.format(getString(R.string.price_sub), bean.getFee()));
        String sellPrice = bean.getTotal_price();
//        goodsPriceTv.setText(sellPrice);
        totalPriceTv.setText(sellPrice);
        String payType = "";
        String pay_status = StringUtils.isSame(bean.getPay_status(), CommonUtils.isOne) ? "已支付" : "未支付";
        switch (bean.getPay_type()) {
            case "1":
                payType = "支付宝支付" + "(" + pay_status + ")";
                break;
            case "6":
                payType = "余额支付" + "(" + pay_status + ")";
                break;
            case "7":
                payType = "微信支付" + "(" + pay_status + ")";
                break;
        }
        payWayTv.setText(payType);

        order_1.setVisibility(View.VISIBLE);
        order_2.setVisibility(View.VISIBLE);
        order_3.setVisibility(View.VISIBLE);
        order_1.setText("联系卖家");
//        final String oid = bean.getId();//订单编号
        status = bean.getState();// 1|2|3|4|5|6 => 1生成订单|2确认订单|3取消订单|4作废订单|5完成订单|6申请退款
        paysStatus = bean.getPay_status();//支付状态 0：未支付，1：已支付，2：退款',

        switch (paysStatus) {
            case "0"://未支付
                order_2.setText("取消订单");
                order_3.setText("去付款");
                break;
            case "1"://已支付
                switch (status) {
                    case "1":
                    case "2":
                        order_2.setText("退款");
                        order_3.setText("确认收货");
                        break;
                    case "3":
                        order_2.setVisibility(View.GONE);
                        order_3.setVisibility(View.GONE);
                        break;
                    case "4":
                        order_2.setVisibility(View.GONE);
                        order_3.setVisibility(View.GONE);
                        order_1.setText("删除订单");
                        break;
                    case "5":
                        order_2.setText("删除订单");
//                        if (StringUtils.isSame(CommonUtils.isZero,bean.getReviews())){
//                            order_2.setText("晒单评价");
//                        }else {
//                            order_2.setVisibility(View.GONE);
//                        }
                        break;
                    case "6":// 1|2|3|4|5|6 => 1生成订单|2确认订单|3取消订单|4作废订单|5完成订单|6申请退款
                        order_2.setVisibility(View.GONE);
                        order_3.setText("退款中");
                        break;
                }

                break;
            case "2"://退款
                order_2.setVisibility(View.GONE);
                order_3.setText("退款中");
                break;
        }
    }

    @OnClick({R.id.order_1, R.id.order_2, R.id.order_3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.order_1://
                switch (paysStatus) {
                    case "1"://联系商家
                        if (StringUtils.isSame(status, "4")) {
                            getAlertDialog("确认要删除该订单？", "4", oid);
                            break;
                        }
                    case "0"://联系商家
                    case "2"://联系商家
                        CommonUtils.developing(getContext());
//                        CommonUtils.startChatActivity(getContext(), SharedAccount.getInstance(getContext()).getServiceId(), false);
                        break;
                }
                break;
            case R.id.order_2://
                switch (paysStatus) {
                    case "0"://
                        CommonUtils.developing(getContext());
//                        getAlertDialog("确认要取消订单？","3", oid);
                        break;
                    case "1"://
                        CommonUtils.developing(getContext());
//                        switch (status) {//状态值 1|2|3|4|5|6 =>生成订单|确认订单|取消订单|作废订单|完成订单|申请退款
//                            case "1":
//                            case "2":
//                                getAlertDialog("确认要退款？","6", oid);
//                                break;
//                            case "5":
//                                getAlertDialog("确认要删除该订单？","4", oid);
//                                break;
//                        }
                        break;
                }
                break;
            case R.id.order_3://
                switch (paysStatus) {
                    case "0"://去支付
                        goPay();
                        break;
                    case "1"://
                        switch (status) {
                            case "1":
                            case "2":
                                CommonUtils.developing(getContext());
//                                getAlertDialog("确认要收货？","5", oid);
                                break;
                            case "3":
                                break;
                            case "4":
                                break;
                            case "5"://去评论
                                CommonUtils.developing(getContext());
//                                Bundle bundle = new Bundle();
//                                bundle.putSerializable("bean", bean);
//                                IntentUtil.startActivity(getContext(), PublishCommentActivity.class,bundle);
                                break;
                        }
                        break;
                }
                break;
        }
    }

    private void getAlertDialog(String content, final String status, final String oid) {
        AKDialog.getAlertDialog(this, content, new AKDialog.AlertDialogListener() {
            @Override
            public void yes() {
                orderAffirm(status, oid);
            }
        });
    }

    //支付宝微信支付成功时候调用
    @Subscribe
    public void paySucceedEvent(PaySucceedEvent event) {
        loadOrderDetails();
        EventBus.getDefault().post(new OrderOperationEvent());//
    }

    private void goPay() {
        KangQiMeiApi api = new KangQiMeiApi("app/pay");
        api.add("order_id", bean.getId()).add("uid", api.getUserId(this)).add("id", bean.getId());
        HttpClient httpClient = HttpClient.newInstance(this);
        int type = Integer.valueOf(bean.getPay_type());
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
                    EventBus.getDefault().post(new OrderOperationEvent());//
                    IntentUtil.startActivity(getContext(), MyGoodsOrderActivity.class);
                    finish();
                }
            });
        }
    }

    private void orderAffirm(final String status, String oid) {
        KangQiMeiApi api = new KangQiMeiApi("app/delorder");
        api.add("uid", api.getUserId(this)).add("id", oid);
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()) {
                    return;
                }
                UIHelper.ToastMessage(getContext(), response.getInfo());
                if (StringUtils.isSame(status, "4")) {
                    EventBus.getDefault().post(new OrderOperationEvent());//刷新订单列表
                    finish();
                    return;
                }
                loadOrderDetails();
            }
        });
    }

    //订单详情的所有操作完成后调用(评论成功)
//    @Subscribe
//    public void commentEvent(CommentEvent event){
//        bean.setReviews("1");
//        setData();
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
