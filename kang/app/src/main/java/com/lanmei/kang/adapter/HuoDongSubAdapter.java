package com.lanmei.kang.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.bean.NewsCategoryListBean;
import com.xson.common.adapter.SwipeRefreshAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 高端游学、、、、、
 */
public class HuoDongSubAdapter extends SwipeRefreshAdapter<NewsCategoryListBean> {
    private final Context context;
    public HuoDongSubAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_huo_dong, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        final NewsCategoryListBean bean = getItem(position);
        if (bean == null) {
            return;
        }
        ViewHolder viewHolder = (HuoDongSubAdapter.ViewHolder) holder;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.pic_iv)
        ImageView mPicIv;
        @InjectView(R.id.title_tv)
        TextView mTitleTv;//标题
        @InjectView(R.id.time_tv)
        TextView mTimeTv;//时间
        @InjectView(R.id.label_tv)
        TextView mLabelTv;//标签
        @InjectView(R.id.price_tv)
        TextView mPriceTv;//价格

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }
}
