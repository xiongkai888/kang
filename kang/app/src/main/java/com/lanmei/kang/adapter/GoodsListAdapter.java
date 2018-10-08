package com.lanmei.kang.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.bean.MerchantTabGoodsBean;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.helper.ImageHelper;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 商品列表
 */
public class GoodsListAdapter extends SwipeRefreshAdapter<MerchantTabGoodsBean> {


    public GoodsListAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_merchant_tab, parent, false));
    }


    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        MerchantTabGoodsBean bean = getItem(position);
        if (bean == null) {
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setParameter(bean);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.image)
        ImageView image;
        @InjectView(R.id.content_tv)
        TextView contentTv;
        @InjectView(R.id.money_tv)
        TextView moneyTv;
        @InjectView(R.id.sell_num_tv)
        TextView sellNumTv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }

        public void setParameter(MerchantTabGoodsBean bean) {
            ImageHelper.load(context, bean.getImgs(), image, null, true, R.mipmap.default_pic, R.mipmap.default_pic);
            contentTv.setText(bean.getGoodsname());
            moneyTv.setText(String.format(context.getString(R.string.price), bean.getPrice()));
            sellNumTv.setText(String.format(context.getString(R.string.have_sales), bean.getSales()));
        }
    }

}
