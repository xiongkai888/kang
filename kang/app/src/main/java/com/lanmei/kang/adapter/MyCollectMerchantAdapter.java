package com.lanmei.kang.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.bean.CollectBean;
import com.lanmei.kang.ui.merchant.activity.MerchantIntroduceActivity;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.helper.ImageHelper;
import com.xson.common.utils.IntentUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 我的收藏（商家）
 */
public class MyCollectMerchantAdapter extends SwipeRefreshAdapter<CollectBean> {


    public MyCollectMerchantAdapter(Context context) {
        super(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {

        // 列表  item_home_list
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_home, parent, false));
    }


    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {

        final CollectBean bean = getItem(position);
        if (bean == null) {
            return;
        }
        //这里写列表内容
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setParameter(bean);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtil.startActivity(context, MerchantIntroduceActivity.class,bean.getUid());
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

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }

        public void setParameter(CollectBean bean) {
            ImageHelper.load(context,bean.getFee_introduction(),picIv,null,true,R.mipmap.default_pic,R.mipmap.default_pic);
            labelTv.setText(bean.getArea());
            nameTv.setText(bean.getName());
            priceTv.setText(String.format(context.getString(R.string.price),bean.getMoney()));
            distanceTv.setText(bean.getDistance()+"km   "+bean.getAddress());
        }
    }

}
