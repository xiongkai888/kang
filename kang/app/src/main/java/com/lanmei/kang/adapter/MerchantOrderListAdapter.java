package com.lanmei.kang.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lanmei.kang.R;
import com.lanmei.kang.bean.OrderListBean;
import com.lanmei.kang.ui.merchant.activity.MerchantOrderDetailsActivity;
import com.lanmei.kang.util.Constant;
import com.lanmei.kang.util.FormatTime;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.utils.IntentUtil;

import butterknife.ButterKnife;


/**
 * 商家订单列表
 */
public class MerchantOrderListAdapter extends SwipeRefreshAdapter<OrderListBean> {

    private Context context;
    private FormatTime time;

    public MerchantOrderListAdapter(Context context) {
        super(context);
        this.context = context;
        time = new FormatTime(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_order, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
//        final OrderListBean bean = getItem(position);
//        if (bean == null) {
//            return;
//        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtil.startActivity(context, MerchantOrderDetailsActivity.class);
//                MerchantOrderDetailsActivity.startActivityTo(context,bean);
            }
        });
    }

    @Override
    public int getCount() {
        return Constant.quantity;
    }

    MerchantOrderListener mMerchantOrderListener;

    public interface MerchantOrderListener {

        void delOrder(String id);//删除订单监听

        void cancelOrder(String id);//取消订单监听

        void receivingOrder(String id);//确认接单
    }

    public void setMerchantOrderListener(MerchantOrderListener l) {
        mMerchantOrderListener = l;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {



        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }
}
