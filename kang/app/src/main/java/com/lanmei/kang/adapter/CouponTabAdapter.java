package com.lanmei.kang.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lanmei.kang.R;
import com.lanmei.kang.bean.NewsCategoryListBean;
import com.lanmei.kang.util.Constant;
import com.xson.common.adapter.SwipeRefreshAdapter;

import butterknife.ButterKnife;


/**
 * 优惠券
 */
public class CouponTabAdapter extends SwipeRefreshAdapter<NewsCategoryListBean> {

    public CouponTabAdapter(Context context) {
        super(context);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_coupon, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
    }

    @Override
    public int getCount() {
        return Constant.quantity;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }
}
