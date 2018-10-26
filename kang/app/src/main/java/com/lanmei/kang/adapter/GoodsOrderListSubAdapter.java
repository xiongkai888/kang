package com.lanmei.kang.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.bean.GoodsOrderListBean;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.helper.ImageHelper;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 商品订单列表子项
 */
public class GoodsOrderListSubAdapter extends SwipeRefreshAdapter<GoodsOrderListBean.GoodsBean> {


    public GoodsOrderListSubAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_confirm_order, parent, false));
    }

    @Override
    public void onBindViewHolder2(final RecyclerView.ViewHolder holder, int position) {
        GoodsOrderListBean.GoodsBean bean = getItem(position);
        if (bean == null) {
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setParameter(bean);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    listener.onClick();
                }
            }
        });
    }

    private OnItemClickListener listener;

    public interface OnItemClickListener{
        void onClick();
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.items_icon_iv)
        ImageView itemsIconIv;
        @InjectView(R.id.title_tv)
        TextView titleTv;
        @InjectView(R.id.specifications_name_tv)
        TextView specificationsNameTv;
        @InjectView(R.id.price_num_tv)
        TextView priceNumTv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }

        public void setParameter(GoodsOrderListBean.GoodsBean bean) {
            ImageHelper.load(context, bean.getCover(), itemsIconIv, null, true, R.mipmap.default_pic, R.mipmap.default_pic);
            titleTv.setText(bean.getGoodsname());
            priceNumTv.setText(String.format(context.getString(R.string.goods_price_and_num), bean.getPrice(), bean.getNum()));
            specificationsNameTv.setText(com.xson.common.utils.StringUtils.isEmpty(bean.getSpecifications())?"":bean.getSpecifications());
        }

    }
}
