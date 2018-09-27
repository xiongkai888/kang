package com.lanmei.kang.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lanmei.kang.R;
import com.lanmei.kang.bean.HomeBean;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.utils.L;

import butterknife.ButterKnife;


/**
 * 商品分类筛选
 */
public class GoodsClassifyAdapter extends SwipeRefreshAdapter<HomeBean> {



    public GoodsClassifyAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_goods_classify, parent, false));
    }


    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);

        }
    }

    @Override
    public int getCount() {
        return L.limit;
    }

}
