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
import com.lanmei.kang.adapter.PayWayAdapter;
import com.lanmei.kang.alipay.AlipayHelper;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.bean.GoodsOrderListBean;
import com.lanmei.kang.bean.PayWayBean;
import com.lanmei.kang.bean.WeiXinBean;
import com.lanmei.kang.event.OrderOperationEvent;
import com.lanmei.kang.event.PaySucceedEvent;
import com.lanmei.kang.helper.WXPayHelper;
import com.lanmei.kang.util.AKDialog;
import com.lanmei.kang.util.CommonUtils;
import com.lanmei.kang.util.FormatTime;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.bean.DataBean;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
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
    @InjectView(R.id.recyclerView_pay)
    RecyclerView recyclerViewPay;//支付方式列表
    @InjectView(R.id.goods_price_tv)
    TextView goodsPriceTv;
    @InjectView(R.id.total_price_tv)
    TextView totalPriceTv;
    @InjectView(R.id.pay_way_tv)
    TextView payWayTv;//支付方式
    GoodsOrderListBean bean;//我的订单item信息
    @InjectView(R.id.order_1)
    TextView order1;
    @InjectView(R.id.order_2)
    TextView order2;
    @InjectView(R.id.order_3)
    TextView order3;
    @InjectView(R.id.scrollView)
    NestedScrollView scrollView;
    @InjectView(R.id.order_no_tv)
    TextView orderNoTv;//订单号
    @InjectView(R.id.state_tv)
    TextView stateTv;//订单状态
    @InjectView(R.id.courier_tv)
    TextView courierTv;//物流单号
    @InjectView(R.id.order_time_tv)
    TextView orderTimeTv;//下单时间
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
        order1.setVisibility(View.GONE);
        order2.setVisibility(View.GONE);
        order3.setVisibility(View.GONE);

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

        recyclerViewPay.setNestedScrollingEnabled(false);
        recyclerViewPay.setLayoutManager(new LinearLayoutManager(this));
    }

    private int type;

    //支付方式
    private void loadPayment() {
        KangQiMeiApi api = new KangQiMeiApi("app/payment");
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<NoPageListBean<PayWayBean>>() {
            @Override
            public void onResponse(NoPageListBean<PayWayBean> response) {
                if (isFinishing()) {
                    return;
                }
                type = 0;
                PayWayAdapter adapter = new PayWayAdapter(getContext());
                adapter.setData(response.data);
                recyclerViewPay.setAdapter(adapter);
                adapter.setPayWayListener(new PayWayAdapter.PayWayListener() {
                    @Override
                    public void payId(String id) {
                        type = Integer.valueOf(id);
                    }
                });
            }
        });
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

    private String state;
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
        FormatTime time = new FormatTime(this);
        time.setTime(bean.getAddtime());
        orderTimeTv.setText(String.format(getString(R.string.order_time), time.formatterTime()));

        GoodsOrderListSubAdapter adapter = new GoodsOrderListSubAdapter(this);
        adapter.setData(bean.getGoods());
        adapter.setOrder_no(bean.getOrder_no());
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        numTv.setText(bean.getNum() + "");
        String sellPrice = bean.getTotal_price();
        totalPriceTv.setText(sellPrice);
        String payType = "";
        String pay_status = StringUtils.isSame(bean.getPay_status(), CommonUtils.isOne) ? "已支付" : "未支付";
        if (!StringUtils.isSame(bean.getPay_status(), CommonUtils.isOne)) {
            loadPayment();
        } else {
            recyclerViewPay.setVisibility(View.GONE);
        }
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

        order1.setVisibility(View.VISIBLE);
        order2.setVisibility(View.GONE);
        order3.setVisibility(View.VISIBLE);
        state = bean.getState();// 1|2|3|4|5|6 => 1生成订单|2确认订单|3取消订单|4作废订单|5完成订单|6申请退款
        String stateStr = "";
        switch (state) {//0|1|2|3|9|4|5=>待支付|已支付|待收货|已完成|全部|退款|取消订单
            case "0":
                stateStr = getString(R.string.wait_pay);//待付款
                order1.setVisibility(View.VISIBLE);
                order1.setText(getString(R.string.cancel_order));//取消订单
                order3.setVisibility(View.VISIBLE);
                order3.setText(getString(R.string.go_pay));//去付款
                break;
            case "1":
                stateStr = getString(R.string.payed);//已支付
                order1.setVisibility(View.VISIBLE);
                order1.setText(getString(R.string.refund));//退款
                order3.setVisibility(View.GONE);
                break;
            case "2":
                stateStr = getString(R.string.wait_receiving);//待收货
                order1.setVisibility(View.VISIBLE);
                order1.setText(getString(R.string.confirm_receipt));//确认收货
                order3.setVisibility(View.GONE);
                break;
            case "3":
                stateStr = getString(R.string.doned);//已完成
                order1.setVisibility(View.GONE);
                order3.setText(getString(R.string.delete_order));//删除订单
                order3.setVisibility(View.VISIBLE);
                break;
            case "4":
                stateStr = getString(R.string.refund_apply);//退款中
                order1.setVisibility(View.GONE);
                order3.setVisibility(View.GONE);
                break;
            case "5":
                stateStr = getString(R.string.order_cancel);//order_cancel
                order1.setVisibility(View.GONE);
                order3.setText(getString(R.string.delete_order));//删除订单
                order3.setVisibility(View.VISIBLE);
                break;
        }
        stateTv.setText(stateStr);

    }

    @OnClick({R.id.order_1, R.id.order_2, R.id.order_3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.order_1://
                switch (state) {//0|1|2|3|9|4|5=>待支付|已支付|待收货|已完成|全部|退款|取消订单
                    case "0"://(待付款)取消订单
                        getAlertDialog("确定取消订单？", "5");
                        break;
                    case "1"://(已支付)退款
                        getAlertDialog("确定退款？", "4");
                        break;
                    case "2"://(待收货)确定收货
                        getAlertDialog("确定收货？", "3");
                        break;
                    case "3":
                        break;
                    case "4":
                        break;
                    case "5":
                        break;
                }
                break;
            case R.id.order_2://
                break;
            case R.id.order_3://
                switch (state) {//1下单(待付款)2、3未消费4已完成5取消订单6申请退款7退款完成
                    case "0"://去付款
                        goPay();
                        break;
                    case "1"://
                        break;
                    case "2":
                        break;
                    case "3"://
                        //删除订单
                        deleteOrderDialog();
                        break;
                    case "4"://
                        break;
                    case "5"://
                        //删除订单
                        deleteOrderDialog();
                        break;
                }
                break;
        }
    }


    private void deleteOrderDialog() {
        AKDialog.getAlertDialog(this, "确定删除该订单？", new AKDialog.AlertDialogListener() {
            @Override
            public void yes() {
                deleteOrder();
            }
        });
    }

    private void getAlertDialog(String content, final String state) {
        AKDialog.getAlertDialog(this, content, new AKDialog.AlertDialogListener() {
            @Override
            public void yes() {
                alterState(state);
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
        if (type == 0) {
            UIHelper.ToastMessage(this, getString(R.string.pay_type));
            return;
        }
        KangQiMeiApi api = new KangQiMeiApi("app/pay");
        api.add("order_id", oid).add("uid", api.getUserId(this)).add("id", oid).add("pay_type", type);
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
                    EventBus.getDefault().post(new PaySucceedEvent());
//                    IntentUtil.startActivity(getContext(), MyGoodsOrderActivity.class);
                    finish();
                }
            });
        }
    }

    //删除订单
    private void deleteOrder() {
        KangQiMeiApi api = new KangQiMeiApi("app/delorder");
        api.add("uid", api.getUserId(this)).add("id", oid);
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()) {
                    return;
                }
                UIHelper.ToastMessage(getContext(), response.getInfo());
                EventBus.getDefault().post(new OrderOperationEvent());//刷新订单列表
                finish();
            }
        });
    }

    //修改订单状态
    private void alterState(String state) {
        KangQiMeiApi api = new KangQiMeiApi("app/status_save");
        api.add("uid", api.getUserId(this)).add("id", oid).add("status", state);
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()) {
                    return;
                }
                UIHelper.ToastMessage(getContext(), response.getInfo());
                EventBus.getDefault().post(new OrderOperationEvent());//刷新订单列表
                loadOrderDetails();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
