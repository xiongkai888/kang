package com.lanmei.kang.ui.details;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.lanmei.kang.R;
import com.lanmei.kang.alipay.AlipayHelper;
import com.lanmei.kang.api.ConsumptionUserApi;
import com.lanmei.kang.api.OrderCancelApi;
import com.lanmei.kang.api.OrderDelApi;
import com.lanmei.kang.api.OrderDetailsApi;
import com.lanmei.kang.api.OrderRefundApi;
import com.lanmei.kang.api.PayMentApi;
import com.lanmei.kang.api.ReserveOrderApi;
import com.lanmei.kang.bean.OrderDetailsBean;
import com.lanmei.kang.bean.WeiXinBean;
import com.lanmei.kang.bean.ZhiFuBaoBean;
import com.lanmei.kang.event.OrderOperationEvent;
import com.lanmei.kang.event.PaySucceedEvent;
import com.lanmei.kang.helper.UserHelper;
import com.lanmei.kang.helper.WXPayHelper;
import com.lanmei.kang.qrcode.NCodeActivity;
import com.lanmei.kang.ui.home.activity.OrderEvaluationActivity;
import com.lanmei.kang.util.AKDialog;
import com.lanmei.kang.util.CommonUtils;
import com.lanmei.kang.util.FormatTime;
import com.lanmei.kang.util.RandomUtil;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.bean.DataBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.L;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.InjectView;
import butterknife.OnClick;


/**
 * 订单详情
 */
public class OrderDetailsActivity extends BaseActivity {

    int type = 6;//支付类型  7：微信支付 7：支付宝 6：余额支付

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;

    @InjectView(R.id.cb_zhifubao)
    CheckBox mZhifubaoCB;//支付宝
    @InjectView(R.id.cb_weixin)
    CheckBox mWeixinCB;//微信
    @InjectView(R.id.cb_balance)
    CheckBox mBalanceCB;//余额

    @InjectView(R.id.ll_pay)
    LinearLayout mLLpay;//支付布局
    @InjectView(R.id.ll_pay_type)
    LinearLayout llPayType;//支付方式布局
    @InjectView(R.id.pay_type_tv)
    TextView payTypeTv;//支付方式

    String id;//订单id
    @InjectView(R.id.name_tv)
    TextView nameTv;
    @InjectView(R.id.status_tv)
    TextView statusTv;
    @InjectView(R.id.project_tv)
    TextView projectTv;
    @InjectView(R.id.subscribe_time_tv)
    TextView subscribeTimeTv;
    @InjectView(R.id.num_tv)
    TextView numTv;
    @InjectView(R.id.order_no_tv)
    TextView orderNoTv;
    @InjectView(R.id.order_time_tv)
    TextView orderTimeTv;
    @InjectView(R.id.total_price_tv)
    TextView totalPriceTv;
    @InjectView(R.id.order_1)
    TextView order1;
    @InjectView(R.id.order_2)
    TextView order2;
    @InjectView(R.id.order_3)
    TextView order3;

    @Override
    public int getContentViewId() {
        return R.layout.activity_order_details;
    }

    @Override
    public void initIntent(Intent intent) {
        super.initIntent(intent);
        id = intent.getStringExtra("value");
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.order_details);
        actionbar.setHomeAsUpIndicator(R.mipmap.back);

        loadOrderDetails(id);
        EventBus.getDefault().register(this);
    }

    private void loadOrderDetails(String id) {
        OrderDetailsApi api = new OrderDetailsApi();
        api.id = id;
        api.uid = api.getUserId(this);
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<DataBean<OrderDetailsBean>>() {
            @Override
            public void onResponse(DataBean<OrderDetailsBean> response) {
                if (isFinishing()) {
                    return;
                }
                setOrderInfo(response.data);
            }
        });
    }

    String mid = "";
    private void setOrderInfo(final OrderDetailsBean bean) {
        if (StringUtils.isEmpty(bean)) {
            return;
        }
        mid = bean.getUid();//商家id
        String pay_type = bean.getPay_type();
        switch (pay_type) {
            case "1":
                payTypeTv.setText(R.string.pay_zhifubao);
                break;
            case "7":
                payTypeTv.setText(R.string.pay_weixin);
                break;
            case "6":
                payTypeTv.setText(R.string.pay_balance);
                break;
        }
        order1.setVisibility(View.VISIBLE);
        //            order2.setVisibility(View.VISIBLE);
        order3.setVisibility(View.VISIBLE);
        final String status = bean.getStatus();
        String payStatus = "";
        switch (status) {//1下单(待付款)2、3未消费4已完成5取消订单6申请退款7退款完成
            case "1":
                payStatus = getString(R.string.wait_pay);
                order1.setText(getString(R.string.cancel_order));//取消订单订单
                //                    order2.setText(context.getString(R.string.contact_merchant));
                order3.setText(getString(R.string.go_pay));//去付款
                break;
            case "2":
            case "3":
                payStatus = getString(R.string.no_consumption);
                order1.setText(getString(R.string.refund));//退款
                //                    order2.setText(context.getString(R.string.contact_merchant));
                order3.setText(getString(R.string.go_consumption));//去消费
                break;
            case "4":
                payStatus = getString(R.string.doned);
                order1.setText(getString(R.string.delete_order));//删除订单
                //                    order2.setText(context.getString(R.string.contact_merchant));
                if (StringUtils.isSame(CommonUtils.isZero, bean.getIs_reviews())) {//为评价
//                        order3.setVisibility(View.VISIBLE);
                    order3.setText(getString(R.string.evaluate));//晒单评价
                } else {
                    order3.setVisibility(View.GONE);
                }
                break;
            case "5":
                payStatus = getString(R.string.order_cancel);
                order1.setText(getString(R.string.delete_order));//删除订单
                //                    order2.setText(context.getString(R.string.contact_merchant));
                order3.setVisibility(View.GONE);
                break;
            case "6":
                payStatus = getString(R.string.refund_apply);
                order1.setVisibility(View.GONE);
                //                    order2.setText(context.getString(R.string.contact_merchant));
                order3.setText(getString(R.string.refund_apply));
                break;
            case "7":
                payStatus = getString(R.string.refund_ok);
                order1.setVisibility(View.GONE);
                //                    order2.setText(context.getString(R.string.contact_merchant));
                order3.setVisibility(View.GONE);
                break;
        }

        if (StringUtils.isSame(status, CommonUtils.isOne)) {
            mLLpay.setVisibility(View.VISIBLE);
            llPayType.setVisibility(View.GONE);
        } else {
            mLLpay.setVisibility(View.GONE);
            llPayType.setVisibility(View.VISIBLE);
        }
        order1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (status) {//1下单(待付款)2、3未消费4已完成5取消订单6申请退款7退款完成
                    case "1"://取消订单
                        AKDialog.getAlertDialog(OrderDetailsActivity.this, getString(R.string.order_affirm_cancel), new AKDialog.AlertDialogListener() {
                            @Override
                            public void yes() {
                                orderCancel();
                            }
                        });
                        break;
                    case "2":
                    case "3"://退款
                        AKDialog.getAlertDialog(OrderDetailsActivity.this, getString(R.string.order_affirm_refund), new AKDialog.AlertDialogListener() {
                            @Override
                            public void yes() {
                                orderRefund();
                            }
                        });
                        break;
                    case "4"://删除订单
                    case "5":
                        AKDialog.getAlertDialog(OrderDetailsActivity.this, getString(R.string.order_affirm_del), new AKDialog.AlertDialogListener() {
                            @Override
                            public void yes() {
                                orderDel();
                            }
                        });
                        break;
                    case "6"://无操作
                        break;
                    case "7"://无操作
                        break;
                }
            }
        });
        order2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.startChatActivity(OrderDetailsActivity.this, bean.getUid(), false);
            }
        });
        order3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (status) {//1下单(待付款)2、3未消费4已完成5取消订单6申请退款7退款完成
                    case "1":
                        pay();
//                        if (type == 6){
//                            pay();
//                        }else {
//                            UIHelper.ToastMessage(OrderDetailsActivity.this, R.string.developing);
//                        }
                        break;
                    case "2":
                    case "3"://去消费
                        AKDialog.getAlertDialog(OrderDetailsActivity.this, getString(R.string.order_affirm_consumption), new AKDialog.AlertDialogListener() {
                            @Override
                            public void yes() {
                                loadDes();
                            }
                        });
                        break;
                    case "4"://晒单评价
                        IntentUtil.startActivity(OrderDetailsActivity.this, OrderEvaluationActivity.class, bean.getId());
                        break;
                    case "5"://无操作
                        break;
                    case "6"://无操作
                        UIHelper.ToastMessage(OrderDetailsActivity.this, R.string.refund_apply);//退款中
                        break;
                    case "7"://无操作
                        break;
                }
            }
        });
        statusTv.setText(payStatus);
        nameTv.setText(bean.getName());
        projectTv.setText(bean.getGoodsName());
        FormatTime time = new FormatTime(bean.getAddtime());
        subscribeTimeTv.setText(time.formatterTime());
        orderTimeTv.setText(time.formatterTime());
        numTv.setText(bean.getGuest());
        orderNoTv.setText(bean.getPay_no());
        totalPriceTv.setText(String.format(getString(R.string.price), bean.getAmount()));
    }

    String des3Code = "";

    private void loadDes() {
        des3Code = RandomUtil.generateMixString(11);
        L.d("des3Code",des3Code);
        ConsumptionUserApi api = new ConsumptionUserApi();
        api.code = des3Code;
        api.id = id;
        api.time = System.currentTimeMillis();
        api.token = api.getToken(this);
        api.uid = api.getUserId(this);
        final JSONObject json = new JSONObject();
        json.put("code",des3Code);
        json.put("Id",id);
        json.put("mid",mid);
        json.put("time",api.time);
        L.d("JSONObject",json.toJSONString());
        L.d("JSONObject",json.toString());
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                IntentUtil.startActivity(OrderDetailsActivity.this,NCodeActivity.class, json.toJSONString());
            }
        });
//
    }

    //取消订单
    private void orderCancel() {
        OrderCancelApi api = new OrderCancelApi();
        api.id = id;
        api.uid = api.getUserId(this);
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()) {
                    return;
                }
                EventBus.getDefault().post(new OrderOperationEvent());
                UIHelper.ToastMessage(OrderDetailsActivity.this, response.getInfo());
                loadOrderDetails(id);
            }
        });
    }

    //申请退款
    private void orderRefund() {
        OrderRefundApi api = new OrderRefundApi();
        api.id = id;
        api.uid = api.getUserId(this);
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()) {
                    return;
                }
                UIHelper.ToastMessage(OrderDetailsActivity.this, response.getInfo());
                EventBus.getDefault().post(new OrderOperationEvent());
                loadOrderDetails(id);
            }
        });
    }

    //删除订单
    private void orderDel() {
        OrderDelApi api = new OrderDelApi();
        api.id = id;
        api.uid = api.getUserId(this);
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()) {
                    return;
                }
                EventBus.getDefault().post(new OrderOperationEvent());
                UIHelper.ToastMessage(OrderDetailsActivity.this, response.getInfo());
                finish();
            }
        });
    }


    private void pay() {
        HttpClient httpClient = HttpClient.newInstance(this);
        ReserveOrderApi api = new ReserveOrderApi();
        api.pay_type = type + "";
        api.id = id;
        api.uid = api.getUserId(this);
        api.token = api.getToken(this);
        httpClient.loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()) {
                    return;
                }
                loadPayMent();
            }
        });
    }

    @Subscribe
    public void paySucceedEvent(PaySucceedEvent event){
        finish();
    }


    private void loadPayMent() {
        HttpClient httpClient = HttpClient.newInstance(this);
        PayMentApi api = new PayMentApi();
        api.order_id = id;
        api.uid = api.getUserId(this);
        api.token = UserHelper.getInstance(this).getToken();
        if (type == 1) {//支付宝支付
            httpClient.loadingRequest(api, new BeanRequest.SuccessListener<DataBean<String>>() {
                @Override
                public void onResponse(DataBean<String> response) {
                    if (isFinishing()) {
                        return;
                    }
                    String bean = response.data;
                    AlipayHelper alipayHelper = new AlipayHelper(OrderDetailsActivity.this);
                    alipayHelper.setPayParam(bean);
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
                    WXPayHelper payHelper = new WXPayHelper(OrderDetailsActivity.this);
                    payHelper.setPayParam(bean);
                    payHelper.orderNow();
                }
            });
        } else {
            httpClient.loadingRequest(api, new BeanRequest.SuccessListener<DataBean<ZhiFuBaoBean>>() {
                @Override
                public void onResponse(DataBean<ZhiFuBaoBean> response) {
                    if (isFinishing()) {
                        return;
                    }
                    loadOrderDetails(id);
                }
            });
        }
    }


    @OnClick({R.id.ll_zhifubao_pay, R.id.ll_weixin_pay, R.id.ll_balance_pay,
            R.id.cb_zhifubao, R.id.cb_weixin, R.id.cb_balance})
    public void selectPayMethod(View view) {//选择支付方式
        switch (view.getId()) {

            case R.id.ll_weixin_pay:
            case R.id.cb_weixin://微信
                selectCheckBox(mWeixinCB, mZhifubaoCB, mBalanceCB, 7);
                break;
            case R.id.ll_zhifubao_pay:
            case R.id.cb_zhifubao://支付宝
                selectCheckBox(mZhifubaoCB, mWeixinCB, mBalanceCB, 1);
                break;
            case R.id.ll_balance_pay:
            case R.id.cb_balance://余额支付
                selectCheckBox(mBalanceCB, mWeixinCB, mZhifubaoCB, 6);
                break;
        }

    }


    //选择支付方式
    private void selectCheckBox(CheckBox cb1, CheckBox cb2, CheckBox cb3, int type) {
        this.type = type;
        cb1.setChecked(true);
        if (cb2.isChecked() || cb3.isChecked()) {
            cb2.setChecked(false);
            cb3.setChecked(false);
        }
    }

    //评论后刷订单详情
    @Subscribe
    public void evaluation(OrderOperationEvent event) {
        loadOrderDetails(id);//简单暴力
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
