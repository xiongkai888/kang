package com.lanmei.kang.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.bean.InventoryListBean;
import com.xson.common.adapter.SwipeRefreshAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 库存列表
 */
public class InventoryListAdapter extends SwipeRefreshAdapter<InventoryListBean> {


    public InventoryListAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_inventory, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        final InventoryListBean bean = getItem(position);
        if (bean == null){
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setParameter(bean);
//        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.goods_no_tv)
        TextView goodsNoTv;
        @InjectView(R.id.goods_name_tv)
        TextView goodsNameTv;
        @InjectView(R.id.inventory_num_tv)
        TextView inventoryNumTv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }

        public void setParameter(InventoryListBean bean) {
            goodsNoTv.setText(bean.getBarcode());
            goodsNameTv.setText(bean.getGoodsname());
            inventoryNumTv.setText(bean.getKucun());
        }
    }
}
