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
import com.lanmei.kang.util.FormatTime;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.StringUtils;
import com.xson.common.widget.FormatTextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * 商品订单列表
 */
public class GoodsOrderListAdapter extends SwipeRefreshAdapter<GoodsOrderListBean> {


    public GoodsOrderListAdapter(Context context) {
        super(context);
        FormatTime time = new FormatTime(context);
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
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtil.startActivity(context,OrderDetailsGoodsActivity.class,bean.getId());
            }
        });
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

        private String oid;
        private String status;
        private String paysStatus;

        @OnClick({R.id.order_1, R.id.order_2,R.id.order_3})
        public void onViewClicked(View view) {
            switch (view.getId()) {
                case R.id.order_1://
                    switch (paysStatus) {
                        case "1"://联系商家
                            if (StringUtils.isSame(status,"4")){
                                CommonUtils.developing(context);
                                getAlertDialog("确认要删除该订单？","4", oid);
                                break;
                            }
                        case "0"://联系商家
                        case "2"://联系商家
                            CommonUtils.developing(context);
//                            CommonUtils.startChatActivity(context, SharedAccount.getInstance(context).getServiceId(), false);
                            break;
                    }
                    break;
                case R.id.order_2://
                    switch (paysStatus) {
                        case "0"://
                            CommonUtils.developing(context);
//                            getAlertDialog("确认要取消订单？","3", oid);
                            break;
                        case "1"://
                            switch (status) {//状态值 1|2|3|4|5|6 =>生成订单|确认订单|取消订单|作废订单|完成订单|申请退款
                                case "1":
                                case "2":
//                                    getAlertDialog("确认要退款？","6", oid);
                                    CommonUtils.developing(context);
                                    break;
                                case "5":
                                    CommonUtils.developing(context);
//                                    getAlertDialog("确认要删除该订单？","4", oid);
                                    break;
                            }
                            break;
                    }
                    break;
                case R.id.order_3://
                    switch (paysStatus) {
                        case "0"://
                            IntentUtil.startActivity(context, OrderDetailsGoodsActivity.class, oid);//去付款
                            break;
                        case "1"://
                            switch (status) {
                                case "1":
                                case "2":
//                                    getAlertDialog("确认要收货？","5", oid);
                                    CommonUtils.developing(context);
                                    break;
                                case "3":
                                    break;
                                case "4":
                                    break;
                                case "5"://去评论
                                    CommonUtils.developing(context);
//                                    Bundle bundle1 = new Bundle();
//                                    bundle1.putSerializable("bean", bean);
//                                    IntentUtil.startActivity(context, PublishCommentActivity.class,bundle1);
                                    break;
                            }
                            break;
                    }
                    break;
            }
        }

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }

        public void setParameter(final GoodsOrderListBean bean) {
            oid = bean.getId();

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
            switch (bean.getState()) {
                case "0":
                    payStatusTv.setText(R.string.wait_pay);
                    break;
                case "1":
                    payStatusTv.setText(R.string.payed);
                    break;
                case "2":
                    payStatusTv.setText(R.string.wait_receiving);
                    break;
                case "3":
                    payStatusTv.setText(R.string.doned);
                    break;
                case "4":
                    payStatusTv.setText(R.string.refund_apply);
                    break;
                case "5":
                    payStatusTv.setText(R.string.order_cancel);
                    break;
            }
            numTv.setText(String.format(context.getString(R.string.goods_num), String.valueOf(bean.getNum())));
            totalPriceTv.setTextValue(bean.getTotal_price());

            status = bean.getState();// 1|2|3|4|5|6 => 1生成订单|2确认订单|3取消订单|4作废订单|5完成订单|6申请退款
            paysStatus = bean.getPay_status();//支付状态 0：未支付，1：已支付，2：退款',
//            L.d("MineOrderSubAdapter", "position" + position + ": status = " + status + ",paysStatus = " + paysStatus);

            switch (paysStatus) {
                case "0"://未支付
                    order2.setText(context.getString(R.string.cancel_order));
                    order3.setText(context.getString(R.string.go_pay));

                    payStatusTv.setText(R.string.wait_pay);
                    break;
                case "1"://已支付
                    payStatusTv.setText(R.string.wait_receiving);
                    switch (status) {
                        case "1":
                        case "2":
                            payStatusTv.setText(R.string.wait_receiving);
                            order2.setText(R.string.refund);
                            order3.setText(R.string.confirm_receipt);
                            break;
                        case "3":
                            payStatusTv.setText(R.string.deal_close);
                            order2.setVisibility(View.GONE);
                            order3.setVisibility(View.GONE);
                            break;
                        case "4":
                            payStatusTv.setText(R.string.deal_close);
                            order2.setVisibility(View.GONE);
                            order3.setVisibility(View.GONE);
                            order1.setText(R.string.delete_order);
                            break;
                        case "5":
                            payStatusTv.setText(R.string.complete_transaction);
                            order2.setText(R.string.delete_order);
//                            if (StringUtils.isSame(CommonUtils.isZero,bean.getReviews())){
//                                order3.setText("晒单评价");
//                            }else {
//                                order3.setVisibility(View.GONE);
//                            }
                            break;
                        case "6":// 1|2|3|4|5|6 => 1生成订单|2确认订单|3取消订单|4作废订单|5完成订单|6申请退款
                            payStatusTv.setText(R.string.refund_apply);
                            order2.setVisibility(View.GONE);
                            order3.setText(R.string.refund_apply);
                            break;
                    }

                    break;
                case "2"://退款
                    order2.setVisibility(View.GONE);
                    order3.setText(R.string.refund_apply);
                    payStatusTv.setText(R.string.refund_apply);
                    break;
            }

        }
    }

    private void getAlertDialog(String content,String status,final String oid){
        AKDialog.getAlertDialog(context, content, new AKDialog.AlertDialogListener() {
            @Override
            public void yes() {
                if (l != null) {
                    l.affirm("4", oid);
                }
            }
        });
    }

    OrderAlterListener l;

    public interface OrderAlterListener {
        void affirm(String status, String oid);
    }
    public void setOrderAlterListener(OrderAlterListener l) {
        this.l = l;
    }

}
