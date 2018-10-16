package com.lanmei.kang.ui.merchant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.bean.OrderListMerchantBean;
import com.lanmei.kang.event.OrderOperationMerchantEvent;
import com.lanmei.kang.qrcode.ScanActivity;
import com.lanmei.kang.util.AKDialog;
import com.lanmei.kang.util.CommonUtils;
import com.lanmei.kang.util.FormatTime;
import com.xson.common.api.AbstractApi;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.bean.DataBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;

import org.greenrobot.eventbus.EventBus;

import butterknife.InjectView;

/**
 * 商家订单详情
 */
public class MerchantOrderDetailsActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.name_tv)
    TextView nameTv;
    @InjectView(R.id.tel_tv)
    TextView telTv;
    @InjectView(R.id.address_tv)
    TextView addressTv;
    @InjectView(R.id.item_name_tv)
    TextView itemNameTv;
    @InjectView(R.id.time_tv)
    TextView timeTv;
    @InjectView(R.id.num_tv)
    TextView numTv;
    @InjectView(R.id.order_no_tv)
    TextView orderNoTv;
    @InjectView(R.id.order_time_tv)
    TextView orderTimeTv;
    @InjectView(R.id.total_price_tv)
    TextView totalPriceTv;
    @InjectView(R.id.pay_way_tv)
    TextView payWayTv;

    @InjectView(R.id.order_1)
    TextView order1;
    @InjectView(R.id.order_2)
    TextView order2;
    @InjectView(R.id.order_3)
    TextView order3;

    OrderListMerchantBean bean;//订单信息
    String id;

    @Override
    public int getContentViewId() {
        return R.layout.activity_merchant_order_details;
    }

    @Override
    public void initIntent(Intent intent) {
        super.initIntent(intent);
        Bundle bundle = intent.getBundleExtra("bundle");
        if (!StringUtils.isEmpty(bundle)){
            bean = (OrderListMerchantBean)bundle.getSerializable("bean");
            if (bean != null){
                id = bean.getId();
            }
        }
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.order_details);
        actionbar.setHomeAsUpIndicator(R.mipmap.back_g);
//订单详情的接口数据和列表项的数据一样
//        MerchantOrderDetailsApi api = new MerchantOrderDetailsApi();
//        api.id = getIntent().getStringExtra("value");
//        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
//            @Override
//            public void onResponse(BaseBean response) {
//
//            }
//        });

        setMerchantOrderDetails(bean);
    }

    private void loadOrderDetails(){
        KangQiMeiApi api = new KangQiMeiApi("Reservation/sellerDetail");
        api.addParams("id",id);
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<DataBean<OrderListMerchantBean>>() {
            @Override
            public void onResponse(DataBean<OrderListMerchantBean> response) {
                OrderListMerchantBean merchantBean = response.data;
                if (merchantBean != null){
                    bean = merchantBean;
                    setMerchantOrderDetails(bean);
                }
            }
        });
    }


    private void setMerchantOrderDetails(final OrderListMerchantBean bean) {
        if (StringUtils.isEmpty(bean)){
            return;
        }
        nameTv.setText("姓名：" + bean.getNickname());
        telTv.setText("电话：" + bean.getPhone());
//                mAddressTv.setText("地址：" + bean.getAddress());
        itemNameTv.setText(bean.getName());
        FormatTime time = new FormatTime();
        time.setTime(bean.getStime());
        timeTv.setText(time.formatterTimeToDay());
        numTv.setText(bean.getGuest());
        orderNoTv.setText(bean.getPay_no());
//        String sTime = time.formatterTimeNoSeconds();
        time.setTime(bean.getAddtime());
        orderTimeTv.setText(time.formatterTime());
        totalPriceTv.setText(String.format(getString(R.string.price),bean.getAmount()));
        order1.setVisibility(View.VISIBLE);//
        order2.setVisibility(View.VISIBLE);
        order3.setVisibility(View.VISIBLE);

        final String status = bean.getStatus();
        String payStatus = "";
        switch (status) {//1下单(待付款)2、3未消费4已完成5取消订单6申请退款7退款完成
            case "1":
                payStatus = getString(R.string.wait_pay);
                order1.setVisibility(View.GONE);//
                order2.setVisibility(View.GONE);//
                order3.setText(R.string.contact_user);//联系用户
                break;
            case "2":
            case "3":
                payStatus = getString(R.string.no_consumption);
                order1.setVisibility(View.GONE);//
                order2.setText(getString(R.string.contact_user));//联系用户
                order3.setText(getString(R.string.consumption));//消费
                break;
            case "4":
                payStatus = getString(R.string.doned);
                order1.setVisibility(View.GONE);//
                order2.setText(getString(R.string.delete_order));//删除订单
                order3.setText(getString(R.string.contact_user));//联系用户
                break;
            case "5":
                payStatus = getString(R.string.deal_close);//交易关闭
                order1.setVisibility(View.GONE);
                order2.setVisibility(View.GONE);
                order3.setText(getString(R.string.delete_order));//删除订单
                break;
            case "6":
                payStatus = getString(R.string.refund_apply);
                order1.setText(R.string.reject_refund);//拒绝退款
                order2.setText(getString(R.string.agree_refund));//同意退款
                order3.setText(getString(R.string.contact_user));//联系用户
                break;
            case "7":
                payStatus = getString(R.string.refund_ok);
                order1.setVisibility(View.GONE);
                order2.setText(getString(R.string.delete_order));//删除订单
                order3.setText(getString(R.string.contact_user));//联系用户
                break;
        }
        payWayTv.setText(payStatus);
        order1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (status) {//1下单(待付款)2、3未消费4已完成5取消订单6申请退款7退款完成
                    case "1"://无操作
                        break;
                    case "2":
                    case "3"://无操作
                        break;
                    case "4"://无操作
                    case "5"://无操作
                        break;
                    case "6"://拒绝退款
                        AKDialog.getAlertDialog(MerchantOrderDetailsActivity.this, MerchantOrderDetailsActivity.this.getString(R.string.reject_refund_), new AKDialog.AlertDialogListener() {
                            @Override
                            public void yes() {
                                agreeRefund(bean,"3");
                            }
                        });

                        break;
                    case "7"://无操作
                        break;
                }
            }
        });
        order2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (status) {//1下单(待付款)2、3未消费4已完成5取消订单6申请退款7退款完成
                    case "1"://无操作
                        break;
                    case "2":
                    case "3"://联系用户
                        CommonUtils.startChatActivity(MerchantOrderDetailsActivity.this, bean.getUid(), false);
                        break;
                    case "4"://删除订单
                        AKDialog.getAlertDialog(MerchantOrderDetailsActivity.this, MerchantOrderDetailsActivity.this.getString(R.string.order_affirm_del), new AKDialog.AlertDialogListener() {
                            @Override
                            public void yes() {
                                orderDel(bean, 0);
                            }
                        });
                    case "5"://无操作
                        break;
                    case "6"://同意退款
                        AKDialog.getAlertDialog(MerchantOrderDetailsActivity.this, MerchantOrderDetailsActivity.this.getString(R.string.agree_refund_), new AKDialog.AlertDialogListener() {
                            @Override
                            public void yes() {
                                agreeRefund(bean,"7");
                            }
                        });
                        break;
                    case "7"://删除订单
                        AKDialog.getAlertDialog(MerchantOrderDetailsActivity.this, MerchantOrderDetailsActivity.this.getString(R.string.order_affirm_del), new AKDialog.AlertDialogListener() {
                            @Override
                            public void yes() {
                                orderDel(bean, 0);
                            }
                        });
                        break;
                }
            }
        });
        order3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (status) {//1下单(待付款)2、3未消费4已完成5取消订单6申请退款7退款完成
                    case "2":
                    case "3"://消费
                        Bundle bundle = new Bundle();
                        bundle.putInt("type",2);//消费
                        bundle.putBoolean("isQR",false);//二维码
                        IntentUtil.startActivity(getContext(), ScanActivity.class,bundle);
                        break;
                    case "5"://删除订单
                        AKDialog.getAlertDialog(getContext(), MerchantOrderDetailsActivity.this.getString(R.string.order_affirm_del), new AKDialog.AlertDialogListener() {
                            @Override
                            public void yes() {
                                orderDel(bean, 0);
                            }
                        });
                        break;
                    case "1"://联系用户
                    case "4"://联系用户
                    case "6"://联系用户
                    case "7"://联系用户
                        CommonUtils.startChatActivity(MerchantOrderDetailsActivity.this, bean.getUid(), false);
                        break;
                }
            }
        });
    }


    //3拒绝退款和7同意退款
    private void agreeRefund(OrderListMerchantBean bean, String type) {
        KangQiMeiApi api = new KangQiMeiApi("reservation/save");
        api.addParams("id",bean.getId());
        api.addParams("status",type);
        api.addParams("uid",api.getUserId(this));
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()) {
                    return;
                }
                UIHelper.ToastMessage(MerchantOrderDetailsActivity.this, response.getInfo());
                EventBus.getDefault().post(new OrderOperationMerchantEvent());
                loadOrderDetails();
            }
        });
    }

    //删除订单
    private void orderDel(OrderListMerchantBean bean, final int position) {
        KangQiMeiApi api = new KangQiMeiApi("reservation/del");
        api.addParams("id",bean.getId());
        api.addParams("uid",api.getUserId(this));
        api.setMethod(AbstractApi.Method.GET);

        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()) {
                    return;
                }
                EventBus.getDefault().post(new OrderOperationMerchantEvent());
                UIHelper.ToastMessage(MerchantOrderDetailsActivity.this, response.getInfo());
                finish();
            }
        });
    }

}
