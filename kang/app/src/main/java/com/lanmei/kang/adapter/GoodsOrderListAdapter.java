package com.lanmei.kang.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.bean.GoodsOrderListBean;
import com.lanmei.kang.ui.merchant_tab.activity.OrderDetailsGoodsActivity;
import com.lanmei.kang.util.AKDialog;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.utils.IntentUtil;
import com.xson.common.widget.FormatTextView;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 商品订单列表
 */
public class GoodsOrderListAdapter extends SwipeRefreshAdapter<GoodsOrderListBean> {


    public GoodsOrderListAdapter(Context context) {
        super(context);
//        FormatTime time = new FormatTime(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_goods_order, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        final GoodsOrderListBean bean = getItem(position);
        if (bean == null) {
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setParameter(bean);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.pay_no_tv)
        TextView payNoTv;
        @InjectView(R.id.pay_status_tv)
        TextView payStatusTv;
        @InjectView(R.id.recyclerView)
        RecyclerView recyclerView;
        @InjectView(R.id.num_tv)
        TextView numTv;
        @InjectView(R.id.total_price_tv)
        FormatTextView totalPriceTv;

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

        public void setParameter(final GoodsOrderListBean bean) {
            order1.setVisibility(View.VISIBLE);
            order2.setVisibility(View.GONE);
            order3.setVisibility(View.VISIBLE);


            GoodsOrderListSubAdapter adapter = new GoodsOrderListSubAdapter(context);
            adapter.setData(bean.getGoods());
            adapter.setOrder_no(bean.getOrder_no());
            adapter.setOnItemClickListener(new GoodsOrderListSubAdapter.OnItemClickListener() {
                @Override
                public void onClick() {
                    IntentUtil.startActivity(context, OrderDetailsGoodsActivity.class, bean.getId());
                }
            });
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(adapter);
            payNoTv.setText(String.format(context.getString(R.string.order_no), bean.getOrder_no()));
            totalPriceTv.setTextValue(bean.getTotal_price());

            final String state = bean.getState();
            String payStatus = "";
            switch (state) {//0|1|2|3|9|4|5=>待支付|已支付|待收货|已完成|全部|退款|取消订单
                case "0":
                    payStatus = context.getString(R.string.wait_pay);//待付款
                    order1.setVisibility(View.VISIBLE);
                    order1.setText(context.getString(R.string.cancel_order));//取消订单
                    order3.setVisibility(View.VISIBLE);
                    order3.setText(context.getString(R.string.go_pay));//去付款
                    break;
                case "1":
                    payStatus = context.getString(R.string.payed);//已支付
                    order1.setVisibility(View.VISIBLE);
                    order1.setText(context.getString(R.string.refund));//退款
                    order3.setVisibility(View.GONE);
                    break;
                case "2":
                    payStatus = context.getString(R.string.wait_receiving);//待收货
                    order1.setVisibility(View.VISIBLE);
                    order1.setText(context.getString(R.string.confirm_receipt));//确认收货
                    order3.setVisibility(View.GONE);
                    break;
                case "3":
                    payStatus = context.getString(R.string.doned);//已完成
                    order1.setVisibility(View.GONE);
                    order3.setText(context.getString(R.string.delete_order));//删除订单
                    order3.setVisibility(View.VISIBLE);
                    break;
                case "4":
                    payStatus = context.getString(R.string.refund_apply);//退款中
                    order1.setVisibility(View.GONE);
                    order3.setVisibility(View.GONE);
                    break;
                case "5":
                    payStatus = context.getString(R.string.order_cancel);//order_cancel
                    order1.setVisibility(View.GONE);
                    order3.setText(context.getString(R.string.delete_order));//删除订单
                    order3.setVisibility(View.VISIBLE);
                    break;
            }

            payStatusTv.setText(payStatus);

            order1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (state) {//0|1|2|3|9|4|5=>待支付|已支付|待收货|已完成|全部|退款|取消订单
                        case "0"://(待付款)取消订单
                            getAlertDialog("确定取消订单？", "5", bean.getId());
                            break;
                        case "1"://(已支付)退款
                            getAlertDialog("确定退款？", "4", bean.getId());
                            break;
                        case "2"://(待收货)确定收货
                            getAlertDialog("确定收货？", "3", bean.getId());
                            break;
                        case "3":
                            break;
                        case "4":
                            break;
                        case "5":
                            break;
                    }
                }
            });
            order3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (state) {//1下单(待付款)2、3未消费4已完成5取消订单6申请退款7退款完成
                        case "0"://去付款
                            IntentUtil.startActivity(context, OrderDetailsGoodsActivity.class, bean.getId());
                            break;
                        case "1"://
                            break;
                        case "2":
                            break;
                        case "3"://
                            //删除订单
                            AKDialog.getAlertDialog(context, "确定删除该订单？", new AKDialog.AlertDialogListener() {
                                @Override
                                public void yes() {
                                    if (l != null) {
                                        l.deleteOrder(bean.getId());
                                    }
                                }
                            });
                            break;
                        case "4"://
                            break;
                        case "5"://
                            AKDialog.getAlertDialog(context, "确定删除该订单？", new AKDialog.AlertDialogListener() {
                                @Override
                                public void yes() {
                                    if (l != null) {
                                        l.deleteOrder(bean.getId());
                                    }
                                }
                            });
                            break;
                    }
                }
            });

        }
    }


    private void getAlertDialog(String content, final String status, final String oid) {
        AKDialog.getAlertDialog(context, content, new AKDialog.AlertDialogListener() {
            @Override
            public void yes() {
                if (l != null) {
                    l.affirm(status, oid);
                }
            }
        });
    }

    private OrderAlterListener l;

    public interface OrderAlterListener {
        void affirm(String status, String oid);

        void deleteOrder(String oid);
    }

    public void setOrderAlterListener(OrderAlterListener l) {
        this.l = l;
    }

}
