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
import com.lanmei.kang.bean.MerchantDetailsBean;
import com.lanmei.kang.ui.merchant.activity.MerchantItemsDetailsActivity;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.helper.ImageHelper;
import com.xson.common.utils.IntentUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 我的收藏（服务）
 */
public class MyCollectItemsAdapter extends SwipeRefreshAdapter<MerchantDetailsBean.GoodsBean> {


    public MyCollectItemsAdapter(Context context) {
        super(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {

        // 列表  item_home_list
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_home, parent, false));
    }


    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {

        final MerchantDetailsBean.GoodsBean bean = getItem(position);
        if (bean == null) {
            return;
        }
        //这里写列表内容
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setParameter(bean);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("bean",bean);
                IntentUtil.startActivity(context, MerchantItemsDetailsActivity.class,bundle);
            }
        });
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.pic_iv)
        ImageView picIv;
        @InjectView(R.id.label_tv)
        TextView labelTv;
        @InjectView(R.id.name_tv)
        TextView nameTv;
        @InjectView(R.id.price_tv)
        TextView priceTv;
        @InjectView(R.id.distance_tv)
        TextView distanceTv;
        @InjectView(R.id.index_iv)
        ImageView indexIv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }

        public void setParameter(MerchantDetailsBean.GoodsBean bean) {
            ImageHelper.load(context,bean.getImg(),picIv,null,true,R.mipmap.default_pic,R.mipmap.default_pic);
            labelTv.setVisibility(View.GONE);
            nameTv.setText(bean.getPlace_name());
            priceTv.setText(String.format(context.getString(R.string.price),bean.getSell_price()));
            distanceTv.setVisibility(View.GONE);
            indexIv.setVisibility(View.GONE);
        }
    }

}
