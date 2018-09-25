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
import com.lanmei.kang.api.OrderCancelApi;
import com.lanmei.kang.api.OrderDelApi;
import com.lanmei.kang.api.OrderRefundApi;
import com.lanmei.kang.bean.OrderListBean;
import com.lanmei.kang.event.OrderOperationEvent;
import com.lanmei.kang.ui.details.OrderDetailsActivity;
import com.lanmei.kang.ui.home.activity.OrderEvaluationActivity;
import com.lanmei.kang.util.AKDialog;
import com.lanmei.kang.util.CommonUtils;
import com.lanmei.kang.util.FormatTime;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.bean.BaseBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.helper.ImageHelper;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 订单列表
 */
public class OrderListAdapter extends SwipeRefreshAdapter<OrderListBean> {


    FormatTime time;
    public OrderListAdapter(Context context) {
        super(context);
        time = new FormatTime();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_order, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        final OrderListBean bean = getItem(position);
        if (bean == null) {
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setParameter(bean, position);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("bean",bean);
                IntentUtil.startActivity(context, OrderDetailsActivity.class,bean.getId());
//                IntentUtil.startActivity(context, OrderDetailsActivity.class,bundle);
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

        public void setParameter(final OrderListBean bean, final int position) {
            order1.setVisibility(View.VISIBLE);
            //            order2.setVisibility(View.VISIBLE);
            order3.setVisibility(View.VISIBLE);
            payNoTv.setText(String.format(context.getString(R.string.order_no), bean.getPay_no()));
            nameTv.setText(bean.getName());
            serviceItemTv.setText(String.format(context.getString(R.string.service_item), bean.getGoodsName()));
            numPeopleTv.setText(String.format(context.getString(R.string.people_num), bean.getGuest()));
            totalPriceTv.setText(String.format(context.getString(R.string.price), bean.getAmount()));
            time.setTime(bean.getAddtime());
            reserveTimeTv.setText(String.format(context.getString(R.string.reserve_time), time.formatterTime()));
            ImageHelper.load(context, bean.getFee_introduction(), itemsIconIv, null, true, R.mipmap.default_pic, R.mipmap.default_pic);
            final String status = bean.getStatus();
            String payStatus = "";
            switch (status) {//1下单(待付款)2、3未消费4已完成5取消订单6申请退款7退款完成
                case "1":
                    payStatus = context.getString(R.string.wait_pay);
                    order1.setText(context.getString(R.string.cancel_order));//取消订单订单
                    //                    order2.setText(context.getString(R.string.contact_merchant));
                    order3.setText(context.getString(R.string.go_pay));//去付款
                    break;
                case "2":
                case "3":
                    payStatus = context.getString(R.string.no_consumption);
                    order1.setText(context.getString(R.string.refund));//退款
                    //                    order2.setText(context.getString(R.string.contact_merchant));
                    order3.setText(context.getString(R.string.go_consumption));//去消费
                    break;
                case "4":
                    payStatus = context.getString(R.string.doned);
                    order1.setText(context.getString(R.string.delete_order));//删除订单
                    //                    order2.setText(context.getString(R.string.contact_merchant));
                    if (StringUtils.isSame(CommonUtils.isZero,bean.getIs_reviews())){//为评价
//                        order3.setVisibility(View.VISIBLE);
                        order3.setText(context.getString(R.string.evaluate));//晒单评价
                    }else {
                        order3.setVisibility(View.GONE);
                    }
                    break;
                case "5":
                    payStatus = context.getString(R.string.order_cancel);
                    order1.setText(context.getString(R.string.delete_order));//删除订单
                    //                    order2.setText(context.getString(R.string.contact_merchant));
                    order3.setVisibility(View.GONE);
                    break;
                case "6":
                    payStatus = context.getString(R.string.refund_apply);
                    order1.setVisibility(View.GONE);
                    //                    order2.setText(context.getString(R.string.contact_merchant));
                    order3.setText(context.getString(R.string.refund_apply));
                    break;
                case "7":
                    payStatus = context.getString(R.string.refund_ok);
                    order1.setVisibility(View.GONE);
                    //                    order2.setText(context.getString(R.string.contact_merchant));
                    order3.setVisibility(View.GONE);
                    break;
            }
            payStatusTv.setText(payStatus);
            order1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (status) {//1下单(待付款)2、3未消费4已完成5取消订单6申请退款7退款完成
                        case "1"://取消订单
                            AKDialog.getAlertDialog(context, context.getString(R.string.order_affirm_cancel), new AKDialog.AlertDialogListener() {
                                @Override
                                public void yes() {
                                    orderCancel(bean,position);
                                }
                            });
                            break;
                        case "2":
                        case "3"://退款
                            AKDialog.getAlertDialog(context, context.getString(R.string.order_affirm_refund), new AKDialog.AlertDialogListener() {
                                @Override
                                public void yes() {
                                    orderRefund(bean,position);
                                }
                            });
                            break;
                        case "4"://删除订单
                        case "5":
                            AKDialog.getAlertDialog(context, context.getString(R.string.order_affirm_del), new AKDialog.AlertDialogListener() {
                                @Override
                                public void yes() {
                                    orderDel(bean, position);
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
                    CommonUtils.startChatActivity(context, bean.getUid(), false);
                }
            });
            order3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (status) {//1下单(待付款)2、3未消费4已完成5取消订单6申请退款7退款完成
                        case "1"://去付款
                            IntentUtil.startActivity(context, OrderDetailsActivity.class,bean.getId());
                            break;
                        case "2":
                        case "3"://去消费
//                            IntentUtil.startActivity(context,NCodeActivity.class);
                            IntentUtil.startActivity(context, OrderDetailsActivity.class,bean.getId());
                            break;
                        case "4"://晒单评价
                            IntentUtil.startActivity(context,OrderEvaluationActivity.class,bean.getId());
                            break;
                        case "5"://无操作
                            break;
                        case "6"://无操作
                            UIHelper.ToastMessage(context, R.string.refund_apply);//退款中
                            break;
                        case "7"://无操作
                            break;
                    }
                }
            });
        }
    }

    //取消订单
    private void orderCancel(final OrderListBean bean, final int position) {
        OrderCancelApi api = new OrderCancelApi();
        api.id = bean.getId();
        api.uid = api.getUserId(context);
        HttpClient.newInstance(context).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (time == null) {
                    return;
                }
                EventBus.getDefault().post(new OrderOperationEvent());
                UIHelper.ToastMessage(context, response.getInfo());
            }
        });
    }

    //申请退款
    private void orderRefund(final OrderListBean bean,final int position) {
        OrderRefundApi api = new OrderRefundApi();
        api.id = bean.getId();
        api.uid = api.getUserId(context);
        HttpClient.newInstance(context).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (time == null) {
                    return;
                }
                UIHelper.ToastMessage(context, response.getInfo());
                EventBus.getDefault().post(new OrderOperationEvent());
            }
        });
    }

    //删除订单
    private void orderDel(final OrderListBean bean, final int position) {
        OrderDelApi api = new OrderDelApi();
        api.id = bean.getId();
        api.uid = api.getUserId(context);
        HttpClient.newInstance(context).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (time == null) {
                    return;
                }
                EventBus.getDefault().post(new OrderOperationEvent());
                UIHelper.ToastMessage(context, response.getInfo());
            }
        });
    }

}
