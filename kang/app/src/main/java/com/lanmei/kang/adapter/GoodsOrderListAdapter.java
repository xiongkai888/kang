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
import com.lanmei.kang.util.FormatTime;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.widget.FormatTextView;

import butterknife.ButterKnife;
import butterknife.InjectView;


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
        GoodsOrderListBean bean = getItem(position);
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

        public void setParameter(GoodsOrderListBean bean) {
            GoodsOrderListSubAdapter adapter = new GoodsOrderListSubAdapter(context);
            adapter.setData(bean.getGoods());
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(adapter);
            payNoTv.setText(String.format(context.getString(R.string.order_no), bean.getPay_no()));
            switch (bean.getState()) {
                case "0":
                    payStatusTv.setText("待支付");
                    break;
                case "1":
                    payStatusTv.setText("已支付");
                    break;
                case "2":
                    payStatusTv.setText("待收货");
                    break;
                case "3":
                    payStatusTv.setText("已完成");
                    break;
                case "4":
                    payStatusTv.setText("退款中");
                    break;
                case "5":
                    payStatusTv.setText("订单已取消");
                    break;
            }
            numTv.setText(String.format(context.getString(R.string.goods_num), String.valueOf(bean.getNum())));
            totalPriceTv.setTextValue(bean.getTotal_price());
            order1.setVisibility(View.GONE);
            order2.setVisibility(View.GONE);
            order3.setVisibility(View.GONE);
        }
    }

}
