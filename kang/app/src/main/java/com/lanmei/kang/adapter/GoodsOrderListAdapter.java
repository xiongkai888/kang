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
import com.lanmei.kang.util.CommonUtils;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.StringUtils;
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
//        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                IntentUtil.startActivity(context,OrderDetailsGoodsActivity.class,bean.getId());
//            }
//        });
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
            order2.setVisibility(View.VISIBLE);
            order3.setVisibility(View.VISIBLE);


            GoodsOrderListSubAdapter adapter = new GoodsOrderListSubAdapter(context);
            adapter.setData(bean.getGoods());
            adapter.setOnItemClickListener(new GoodsOrderListSubAdapter.OnItemClickListener() {
                @Override
                public void onClick() {
                    IntentUtil.startActivity(context,OrderDetailsGoodsActivity.class,bean.getId());
                }
            });
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(adapter);
            payNoTv.setText(String.format(context.getString(R.string.order_no), bean.getPay_no()));
            totalPriceTv.setTextValue(bean.getTotal_price());

            final String state = bean.getState();
            String payStatus = "";
            switch (state) {//0|1|2|3|9|4|5=>待支付|已支付|待收货|已完成|全部|退款|取消订单
                case "0":
                    payStatus = context.getString(R.string.wait_pay);//待付款
                    order1.setText(context.getString(R.string.cancel_order));//取消订单
                    order2.setText(context.getString(R.string.contact_merchant));
                    order3.setText(context.getString(R.string.go_pay));//去付款
                    break;
                case "1":
                    payStatus = context.getString(R.string.payed);//已支付
                    order3.setVisibility(View.GONE);
                    order1.setText(context.getString(R.string.cancel_order));//删除订单
                    order2.setText(context.getString(R.string.contact_merchant));//联系商家
                    order3.setVisibility(View.GONE);
                    break;
                case "2":
                    payStatus = context.getString(R.string.wait_receiving);//待收货
                    order1.setText(context.getString(R.string.refund));//退款
                    order2.setText(context.getString(R.string.contact_merchant));//联系商家
                    order3.setVisibility(View.GONE);
                    break;
                case "3":
                    payStatus = context.getString(R.string.doned);//已完成
                    order1.setText(context.getString(R.string.delete_order));//删除订单
                    order2.setText(context.getString(R.string.contact_merchant));
                    if (StringUtils.isSame(CommonUtils.isZero, bean.getPay_status())) {//为评价
                        order3.setVisibility(View.VISIBLE);
                        order3.setText(context.getString(R.string.bask_in_a_single_comment));//晒单评价
                    } else {
                        order3.setVisibility(View.GONE);
                    }
                    break;
                case "4":
                    payStatus = context.getString(R.string.refund_apply);//退款中
                    order1.setText(context.getString(R.string.refund_apply));
                    order2.setText(context.getString(R.string.contact_merchant));
                    order3.setVisibility(View.GONE);
                    break;
                case "5":
                    payStatus = context.getString(R.string.order_cancel);//order_cancel
                    order1.setText(context.getString(R.string.delete_order));//删除订单
                    order2.setText(context.getString(R.string.contact_merchant));
                    order3.setVisibility(View.GONE);
                    break;
            }
            payStatusTv.setText(payStatus);

            order1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (state) {//0|1|2|3|9|4|5=>待支付|已支付|待收货|已完成|全部|退款|取消订单
                        case "0"://取消订单
                            getAlertDialog("确定取消订单？","5",bean.getId());
                            break;
                        case "1"://取消订单
                            CommonUtils.developing(context);
                            break;
                        case "2":
                            CommonUtils.developing(context);
                            break;
                        case "3"://退款
                            CommonUtils.developing(context);
                            break;
                        case "4"://删除订单
                            CommonUtils.developing(context);
                            break;
                        case "5":
                            CommonUtils.developing(context);
                            break;
                    }
                }
            });
            order2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (StringUtils.isSame(KangApp.HX_USER_Head+bean.getUid(),EMClient.getInstance().getCurrentUser())){
//                        Toast.makeText(context, R.string.Cant_chat_with_yourself, Toast.LENGTH_SHORT).show();
//                    }
//                    L.d(L.TAG,EMClient.getInstance().getCurrentUser());
//                    CommonUtils.startChatActivity(context, bean.getUid(), false);
                    CommonUtils.developing(context);
                }
            });
            order3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (state) {//1下单(待付款)2、3未消费4已完成5取消订单6申请退款7退款完成
                        case "0"://
                            break;
                        case "1"://
                            break;
                        case "2":
                            break;
                        case "3"://
                            break;
                        case "4"://
                            break;
                        case "5"://
                            break;
                    }
                    CommonUtils.developing(context);
                }
            });

        }
    }


    private void getAlertDialog(String content,final String status,final String oid){
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
    }
    public void setOrderAlterListener(OrderAlterListener l) {
        this.l = l;
    }

}
