package com.lanmei.kang.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.bean.OrderListMerchantBean;
import com.lanmei.kang.event.OrderOperationMerchantEvent;
import com.lanmei.kang.qrcode.ScanActivity;
import com.lanmei.kang.ui.merchant.activity.MerchantOrderDetailsActivity;
import com.lanmei.kang.util.AKDialog;
import com.lanmei.kang.util.CommonUtils;
import com.lanmei.kang.util.FormatTime;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.api.AbstractApi;
import com.xson.common.bean.BaseBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.helper.ImageHelper;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.UIHelper;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 商家订单列表
 */
public class OrderListMerchantAdapter extends SwipeRefreshAdapter<OrderListMerchantBean> {


    private FormatTime time;
//    String status;//0全部1待付款2已付款3未消费4已完成
    public OrderListMerchantAdapter(Context context) {
        super(context);
        time = new FormatTime(context);
//        this.status = status;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_order, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        final OrderListMerchantBean bean = getItem(position);
        if (bean == null) {
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setParameter(bean);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                UIHelper.ToastMessage(context,R.string.developing);
                Bundle bundle = new Bundle();
                bundle.putSerializable("bean",bean);
                IntentUtil.startActivity(context, MerchantOrderDetailsActivity.class,bundle);
//                IntentUtil.startActivity(context, MerchantOrderDetailsActivity.class,bean.getId());
            }
        });
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.pay_no_tv)
        TextView payNoTv;
        @InjectView(R.id.pay_status_tv)
        TextView payStatusTv;
        @InjectView(R.id.items_icon_iv)
        ImageView itemsIconIv;
        @InjectView(R.id.name_tv)
        TextView nameTv;
        @InjectView(R.id.service_item_tv)
        TextView serviceItemTv;
        @InjectView(R.id.reserve_time_tv)
        TextView reserveTimeTv;
        @InjectView(R.id.num_people_tv)
        TextView numPeopleTv;
        @InjectView(R.id.total_price_tv)
        TextView totalPriceTv;
        @InjectView(R.id.order_1)
        TextView order1;
        @InjectView(R.id.order_2)
        TextView order2;
        @InjectView(R.id.order_3)
        TextView order3;


        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }

        public void setParameter(final OrderListMerchantBean bean) {
            order1.setVisibility(View.VISIBLE);//
            order2.setVisibility(View.VISIBLE);
            order3.setVisibility(View.VISIBLE);
            payNoTv.setText(String.format(context.getString(R.string.order_no), bean.getPay_no()));
//            nameTv.setText(bean.getName());
            nameTv.setVisibility(View.GONE);
            serviceItemTv.setText(String.format(context.getString(R.string.service_item), bean.getName()));
            numPeopleTv.setText(String.format(context.getString(R.string.people_num), bean.getGuest()));
            totalPriceTv.setText(String.format(context.getString(R.string.price), bean.getAmount()));
            time.setTime(bean.getAddtime());
            reserveTimeTv.setText(String.format(context.getString(R.string.reserve_time), time.formatterTime()));
            ImageHelper.load(context, bean.getPic(), itemsIconIv, null, true, R.mipmap.default_pic, R.mipmap.default_pic);
            final String status = bean.getStatus();
            String payStatus = "";
            switch (status) {//1下单(待付款)2、3未消费4已完成5取消订单6申请退款7退款完成
                case "1":
                    payStatus = context.getString(R.string.wait_pay);
                    order1.setVisibility(View.GONE);//
                    order2.setVisibility(View.GONE);//
                    order3.setText(R.string.contact_user);//联系用户
                    break;
                case "2":
                case "3":
                    payStatus = context.getString(R.string.no_consumption);
                    order1.setVisibility(View.GONE);//
                    order2.setText(context.getString(R.string.contact_user));//联系用户
                    order3.setText(context.getString(R.string.consumption));//消费
                    break;
                case "4":
                    payStatus = context.getString(R.string.doned);
                    order1.setVisibility(View.GONE);//
                    order2.setText(context.getString(R.string.delete_order));//删除订单
                    order3.setText(context.getString(R.string.contact_user));//联系用户
                    break;
                case "5":
                    payStatus = context.getString(R.string.deal_close);//交易关闭
                    order1.setVisibility(View.GONE);
                    order2.setVisibility(View.GONE);
                    order3.setText(context.getString(R.string.delete_order));//删除订单
                    break;
                case "6":
                    payStatus = context.getString(R.string.refund_apply);
                    order1.setText(R.string.reject_refund);//拒绝退款
                    order2.setText(context.getString(R.string.agree_refund));//同意退款
                    order3.setText(context.getString(R.string.contact_user));//联系用户
                    break;
                case "7":
                    payStatus = context.getString(R.string.refund_ok);
                    order1.setVisibility(View.GONE);
                    order2.setText(context.getString(R.string.delete_order));//删除订单
                    order3.setText(context.getString(R.string.contact_user));//联系用户
                    break;
            }
            payStatusTv.setText(payStatus);
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
                            AKDialog.getAlertDialog(context, context.getString(R.string.reject_refund_), new AKDialog.AlertDialogListener() {
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
                            CommonUtils.startChatActivity(context, bean.getUid(), false);
                            break;
                        case "4"://删除订单
                            AKDialog.getAlertDialog(context, context.getString(R.string.order_affirm_del), new AKDialog.AlertDialogListener() {
                                @Override
                                public void yes() {
                                    orderDel(bean);
                                }
                            });
                        case "5"://无操作
                            break;
                        case "6"://同意退款
                            AKDialog.getAlertDialog(context, context.getString(R.string.agree_refund_), new AKDialog.AlertDialogListener() {
                                @Override
                                public void yes() {
                                    agreeRefund(bean,"7");
                                }
                            });
                            break;
                        case "7"://删除订单
                            AKDialog.getAlertDialog(context, context.getString(R.string.order_affirm_del), new AKDialog.AlertDialogListener() {
                                @Override
                                public void yes() {
                                    orderDel(bean);
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
                           IntentUtil.startActivity(context, ScanActivity.class,bundle);
                            break;
                        case "5"://删除订单
                            AKDialog.getAlertDialog(context, context.getString(R.string.order_affirm_del), new AKDialog.AlertDialogListener() {
                                @Override
                                public void yes() {
                                    orderDel(bean);
                                }
                            });
                            break;
                        case "1"://联系用户
                        case "4"://联系用户
                        case "6"://联系用户
                        case "7"://联系用户
                            CommonUtils.startChatActivity(context, bean.getUid(), false);
                            break;
                    }
                }
            });
        }
    }


    //3拒绝退款和7同意退款
    private void agreeRefund(OrderListMerchantBean bean, String type) {
        KangQiMeiApi api = new KangQiMeiApi("reservation/save");
        api.addParams("id",bean.getId());
        api.addParams("status",type);
        api.addParams("uid",api.getUserId(context));
        HttpClient.newInstance(context).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (time == null) {
                    return;
                }
                UIHelper.ToastMessage(context, response.getInfo());
                EventBus.getDefault().post(new OrderOperationMerchantEvent());
            }
        });
    }

    //删除订单
    private void orderDel(OrderListMerchantBean bean) {
        KangQiMeiApi api = new KangQiMeiApi("reservation/del");
        api.addParams("id",bean.getId());
        api.addParams("uid",api.getUserId(context));
        api.setMethod(AbstractApi.Method.GET);

        HttpClient.newInstance(context).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (time == null) {
                    return;
                }
                EventBus.getDefault().post(new OrderOperationMerchantEvent());
                UIHelper.ToastMessage(context, response.getInfo());
            }
        });
    }

}
