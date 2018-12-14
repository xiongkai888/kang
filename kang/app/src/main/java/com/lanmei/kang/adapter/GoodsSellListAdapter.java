package com.lanmei.kang.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.bean.GoodsSellListBean;
import com.lanmei.kang.util.FormatTime;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.helper.ImageHelper;
import com.xson.common.utils.StringUtils;
import com.xson.common.widget.CircleImageView;
import com.xson.common.widget.FormatTextView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 销售列表
 */
public class GoodsSellListAdapter extends SwipeRefreshAdapter<GoodsSellListBean> {

    private FormatTime time;

    public GoodsSellListAdapter(Context context) {
        super(context);
        time = new FormatTime(context);
        time.setApplyToTimeNoSecond();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_goods_sell_list, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        final GoodsSellListBean bean = getItem(position);
        if (bean == null) {
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setParameter(bean,position);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                IntentUtil.startActivity(context, MerchantIntroduceActivity.class, bean.getUid());
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.ll_items)
        LinearLayout layout;
        @InjectView(R.id.delete_tv)
        TextView deleteTv;

        @InjectView(R.id.order_no_tv)
        TextView orderNoTv;
        @InjectView(R.id.time_tv)
        TextView timeTv;
        @InjectView(R.id.num_tv)
        TextView numTv;
        @InjectView(R.id.total_price_tv)
        FormatTextView totalPriceTv;

        @InjectView(R.id.pic_iv)
        CircleImageView picIv;
        @InjectView(R.id.name_tv)
        TextView nameTv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }

        public void setParameter(final GoodsSellListBean bean,final int position) {
            layout.removeAllViews();

            orderNoTv.setText(String.format(context.getString(R.string.order_no), bean.getOrder_no()));
            timeTv.setText(time.formatterTime(bean.getAddtime()));
            numTv.setText(String.format(context.getString(R.string.goods_number), String.valueOf(bean.getNum())));
            totalPriceTv.setTextValue(bean.getTotal_price());

            ImageHelper.load(context, bean.getPic(), picIv, null, true, R.mipmap.default_pic, R.mipmap.default_pic);
            nameTv.setText(bean.getUsername());

            List<GoodsSellListBean.GoodsBean> goodsBeanList = bean.getGoods();
            int size = StringUtils.isEmpty(goodsBeanList) ? 0 : goodsBeanList.size();
            for (int i = 0; i < size; i++) {
                addView(i, goodsBeanList.get(i), size);
            }
            if (time.isToday(Long.valueOf(bean.getAddtime()))){
                deleteTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null){
                            listener.delete(bean.getId(),position,bean.getUid());
                        }
                    }
                });
                deleteTv.setVisibility(View.VISIBLE);
            }else {
                deleteTv.setVisibility(View.GONE);
            }

        }

        private void addView(int position, GoodsSellListBean.GoodsBean bean, int size) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_goods_sell_list_sub, null);
            if (position == size - 1) {
                view.findViewById(R.id.line_tv).setVisibility(View.GONE);
            }
            ((TextView)view.findViewById(R.id.goods_name_tv)).setText(bean.getGoodsname());
            ((TextView)view.findViewById(R.id.price_tv)).setText(String.format(context.getString(R.string.price),bean.getPrice()));
            ((TextView)view.findViewById(R.id.price_sub_tv)).setText(String.format(context.getString(R.string.price_sub),bean.getPrice()));
            ((TextView)view.findViewById(R.id.num_tv)).setText(String.format(context.getString(R.string.number),bean.getNum()));
            layout.addView(view);
        }
    }

    private DeleteSellGoodsListener listener;

    public void setDeleteSellGoodsListener(DeleteSellGoodsListener listener){
        this.listener = listener;
    }

    public interface DeleteSellGoodsListener{
        void delete(String id,int position,String uid);
    }

}
